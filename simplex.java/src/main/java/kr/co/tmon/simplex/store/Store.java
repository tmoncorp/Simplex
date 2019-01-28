package kr.co.tmon.simplex.store;


import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import kr.co.tmon.simplex.Simplex;
import kr.co.tmon.simplex.actions.IActionBinder;
import kr.co.tmon.simplex.actions.IActionBinderSet;
import kr.co.tmon.simplex.annotations.Unsubscribe;
import kr.co.tmon.simplex.channels.Channel;
import kr.co.tmon.simplex.channels.IChannel;
import kr.co.tmon.simplex.reactivex.Unit;
import kr.co.tmon.simplex.utils.ChannelUtil;
import kr.co.tmon.simplex.utils.CloneUtil;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.TransformedResult;
import kr.co.tmon.simplex.actions.descriptor.IDispatchDescriptor;
import kr.co.tmon.simplex.actions.descriptor.ISubscriptionDescriptor;

public class Store<TActionBinderSet extends IActionBinderSet> implements IActionStore<TActionBinderSet> {

	private Class<TActionBinderSet> clazz;
	
	private TActionBinderSet actions;
	
	private Hashtable<String, Object> observableSources;
	
	public Store(Class<TActionBinderSet> clazz) {
		this.clazz = clazz;
		this.observableSources = new Hashtable<String, Object>();
	}
	
	protected TActionBinderSet getActions() throws InstantiationException, IllegalAccessException {
		if (actions == null) {
			actions = clazz.newInstance();
		}

		return actions;
	}
	
	@Override
	public <TAction extends IAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<DescriptorFactory<TActionBinderSet>, IDispatchDescriptor<TAction, TParam, TResult>> descriptor) {
		try {
			DispatchDescriptor<TAction, TParam, TResult> desc 
				= (DispatchDescriptor<TAction, TParam, TResult>)descriptor.apply(new DescriptorFactory<TActionBinderSet>(getActions()));
			
			dispatchInner(
					desc.getAction(), 
					desc.getParameter(), 
					desc.getBegin(), 
					desc.getEnd(), 
					desc.getChannel());
			
		} catch (Exception e) {
			Simplex.ExceptionSubject.onNext(e);
			Simplex.Logger.write(e);
		}
	}
	
	
	@Override
	public <TAction extends IAction<TParam, TResult>, TParam, TResult> Disposable subscribe(
			Function<DescriptorFactory<TActionBinderSet>, ISubscriptionDescriptor<TAction, TParam, TResult>> descriptor) { 
		
		Disposable disposable = null;
		try {
			SubscriptionDescriptor<TAction, TParam, TResult> desc 
				= (SubscriptionDescriptor<TAction, TParam, TResult>)descriptor.apply(new DescriptorFactory<TActionBinderSet>(getActions()));
			
			disposable = subscribeInner(
					desc.getAction(), 
					desc.getOnNext(), 
					desc.getObserveOnMainThread(),
					desc.getObservable(),
					desc.getChannel(),
					desc.getPreventClone());
			
		} catch (Exception e) {
			Simplex.ExceptionSubject.onNext(e);
			Simplex.Logger.write(e);
		}
		return disposable;
	}
	
	private <TAction extends IAction<TParam, TResult>, TParam, TResult, TActionBinder extends IActionBinder<?, TParam, TResult>> Disposable subscribeInner(
			final TAction action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread, 
			Function<Observable<TResult>,  Observable<TResult>> observable,
			Function<TAction, IChannel> channel,
			boolean preventClone) {
		try {
			
			createChannelIfNull(action);		
						
			Subject<ChannelWrapper<TResult>> wrapperSource = getOrAddObservableSource(action);
			Observable<TResult> source = wrapperSource
				.filter(w -> {
					IChannel ch = getChannel(action, channel);
					for(IChannel targetChannel : ChannelUtil.extract(ch)) {
						for(IChannel sourceChannel : ChannelUtil.extract(w.getChannel())) {
							if (targetChannel.getId().equals(sourceChannel.getId())) {
								return true;
							}
						}
					}
					return false;
				})
				.flatMap(mapper -> Observable.just(mapper.getResult()));
			
			Observable<TResult> o = source;
			if (observable != null) {
				o = observable.apply(source);
			}
			
			if (observerOnMainThread) {
				o = o.observeOn(Simplex.MainThreadScheduler);
			}
			
			return o
					.map(x -> {
						TResult value = x;
						try
				        {
				            //프리미티브, 열거형 타입이거나, 복사 방지 옵션이 설정되었으면 복사하지 않음
							//레퍼런스 타입의 경우 데이터 사본을 전달
				            if (!(preventClone 
				            		|| value.getClass() == Unit.class
				            		|| value.getClass().isPrimitive()
				            		|| Collection.class.isAssignableFrom(value.getClass()))) {
				            	TResult cloned = CloneUtil.deepClone(value);
				                value = cloned;
				            }
				        }
				        catch (Exception ex)
				        {
				            Simplex.ExceptionSubject.onNext(ex);
				            Simplex.Logger.write(ex);
				        }
						return x;
					})
					.subscribe(onNext);
						
		} catch (Exception e) {
			Simplex.ExceptionSubject.onNext(e);
			Simplex.Logger.write(e);
		}
		
		return null;
	}
	
	
	private <TAction extends IAction<TParam, TResult>, TParam, TResult, TActionBinder extends IActionBinder<?, TParam, TResult>> void dispatchInner(
			final TAction action,
			final TParam parameter,
			Action begin,
			Action end, 
			Function<TAction, IChannel> channel) {
		if (begin != null) {
			Simplex.MainThreadScheduler.scheduleDirect(() -> {
				try {
					begin.run();
				} catch (Exception e) {
					Simplex.Logger.write(e);
				}
			});
		}
			
		Action doCompleted = () -> Simplex.MainThreadScheduler.scheduleDirect(() -> {
			if (end != null) {
				try {
					end.run();
				} catch (Exception e1) {
					Simplex.Logger.write(e1);
				}
			}
		});
		
		try {
			
			Observable<TResult> resultObservable = null;
			resultObservable = ((IAction<TParam, TResult>)action).process(parameter);
			
			resultObservable = resultObservable
					.timeout(Simplex.DefaultActionTimeoutMilliseconds, TimeUnit.MILLISECONDS)
					.onErrorResumeNext(throwable -> {
						TransformedResult<TResult> transform = ((IAction<TParam, TResult>)action).transform(throwable);
						if (transform.isTransformed()) {
							Simplex.Logger.write("예외가 발생되지 않고 데이터로 트랜스폼되어 배출되었습니다.");
							return Observable.just(transform.getResult());
						}
						Simplex.Logger.write(throwable);
                        Simplex.ExceptionSubject.onNext(new Exception(throwable));
						return Observable.empty();
					})
					.doOnComplete(doCompleted);
			
			if (action.getClass().getAnnotation(Unsubscribe.class) != null) {
				//더비
				resultObservable.subscribe();
			} else {
				//채널이 생성되지 않았으면 기본 Channel클래스로 생성
                createChannelIfNull(action);
                
                //액션에 해당하는 source 선택
                Subject<ChannelWrapper<TResult>> observer = this.getOrAddObservableSource(action);
                
                //채널 추가
                Observable<ChannelWrapper<TResult>> ob 
                	= resultObservable.flatMap(x -> {
                		return Observable.just(new ChannelWrapper<TResult>(getChannel(action, channel), x));	
                	});

                ob.subscribe(observer::onNext);
                
			}
		} catch(Exception ex) {
			Simplex.ExceptionSubject.onNext(ex);
			Simplex.Logger.write(ex);
			try {
				doCompleted.run();
			} catch (Exception e) {
				Simplex.Logger.write(ex);
			}
		}
	}
	
	private <TParam, TResult> void createChannelIfNull(IAction<TParam, TResult> action) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = action.getClass().getDeclaredFields();
		for(Field field : fields) {
			if (field.getType() == IChannel.class
					&& field.get(action) == null) {
				field.setAccessible(true);
				field.set(action, new Channel());
			}
		}
	}
			
	@SuppressWarnings("unchecked")
	private <TParam, TResult> Subject<ChannelWrapper<TResult>> getOrAddObservableSource(IAction<TParam, TResult> action) throws Exception {
		Subject<ChannelWrapper<TResult>> subject = null;
		
		// Default채널의 Id를 키로 등록한다.
        String key = action.getDefaultChannel().getId();
        
        if (observableSources.containsKey(key)) {       	
        	subject = (Subject<ChannelWrapper<TResult>>)observableSources.get(key);
        } else {
        	subject = PublishSubject.create();
        	observableSources.put(key, subject);
        }
        return subject;
	}

	private <TAction extends IAction<TParam, TResult>, TParam, TResult> IChannel getChannel(
			TAction action,
			Function<TAction, IChannel> channel) throws Exception {
		IChannel ch = null;
        if (channel != null)
        	ch = channel.apply(action);
        
        if (ch == null)
        	ch = action.getDefaultChannel();
        
        return ch;
	}
	
	public DisposableStore<TActionBinderSet> toDisposableStore(String subscriberId) {
		return new DisposableStore<>(this, subscriberId);
	}
}

class ChannelWrapper<TResult> {
	private IChannel channel;
	
	private TResult result;

	public IChannel getChannel() {
		return channel;
	}

	public void setChannel(IChannel channel) {
		this.channel = channel;
	}

	public TResult getResult() {
		return result;
	}

	public void setResult(TResult result) {
		this.result = result;
	}
	
	ChannelWrapper(IChannel channel, TResult result) {
		this.channel = channel;
		this.result = result;
	}
}