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
import kr.co.tmon.simplex.actions.IParameterizedActionBinder;
import kr.co.tmon.simplex.annotations.Unsubscribe;
import kr.co.tmon.simplex.channels.Channel;
import kr.co.tmon.simplex.channels.IChannel;
import kr.co.tmon.simplex.reactivex.Unit;
import kr.co.tmon.simplex.utils.ChannelUtil;
import kr.co.tmon.simplex.utils.CloneUtil;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.IParameterizedAction;
import kr.co.tmon.simplex.actions.TransformedResult;

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
	public <TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin,
			Action end, 
			Function<TAction, IChannel> channel) {
			dispatchInner(action, parameter, begin, end, channel);
	}
	
	@Override
	public <TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin) {
		dispatchInner(action, parameter, begin, null, null);
	}
	
	@Override
	public <TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin,
			Action end) {
		dispatchInner(action, parameter, begin, end, null);
	}
	
	@Override
	public <TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Function<TAction, IChannel> channel) {
			dispatchInner(action, parameter, null, null, channel);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action, 
			Action begin, 
			Action end,
			Function<TAction, IChannel> channel) {
			dispatchInner(action, null, begin, end, channel);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin) {
		dispatchInner(action, null, begin, null, null);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin,
			Action end) {
		dispatchInner(action, null, begin, end, null);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action, 
			Function<TAction, IChannel> channel) {
			dispatchInner(action, null, null, null, channel);
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action, 
			Action onNext,
			boolean observerOnMainThread, 
			Function<Observable<Unit>, 
			Observable<Unit>> observable,
			Function<TAction, IChannel> channel) { 
		return subscribeInner(action, (o -> onNext.run()), observerOnMainThread, observable, channel, false);
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext) {
		return subscribeInner(action, (o -> onNext.run()), false, null, null, false);
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread) {
		return subscribeInner(action, (o -> onNext.run()), observerOnMainThread, null, null, false);
	}
	
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<Observable<Unit>, Observable<Unit>> observable) {
		return subscribeInner(action, (o -> onNext.run()), false, observable, null, false);
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread,
			Function<Observable<Unit>, Observable<Unit>> observable) {
		return subscribeInner(action, (o -> onNext.run()), observerOnMainThread, observable, null, false);
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<TAction, IChannel> channel,
			boolean observerOnMainThread) {
		return subscribeInner(action, (o -> onNext.run()), observerOnMainThread, null, channel, false);
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<Observable<Unit>, Observable<Unit>> observable,
			Function<TAction, IChannel> channel) {
		return subscribeInner(action, (o -> onNext.run()), false, observable, channel, false);
	}

	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action, 
			Consumer<TResult> onNext,
			boolean observerOnMainThread, 
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel, 
			boolean preventClone) {
		return subscribeInner(action, onNext, observerOnMainThread, observable, channel, preventClone);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext) {
		return subscribeInner(action, onNext, false, null, null, false);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread) {
		return subscribeInner(action, onNext, observerOnMainThread, null, null, false);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable) {
		return subscribeInner(action, onNext, false, observable, null, false);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			boolean preventClone) {
		return subscribeInner(action, onNext, observerOnMainThread, null, null, preventClone);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable) {
		return subscribeInner(action, onNext, observerOnMainThread, observable, null, false);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel) {
		return subscribeInner(action, onNext, observerOnMainThread, observable, channel, false);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<TAction, IChannel> channel,
			boolean observerOnMainThread) {
		return subscribeInner(action, onNext, observerOnMainThread, null, channel, false);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel) {
		return subscribeInner(action, onNext, false, observable, channel, false);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel,
			boolean preventClone) {
		return subscribeInner(action, onNext, false, observable, channel, preventClone);
	}
	
	@SuppressWarnings("unchecked")
	private <TAction extends IAction<TResult>, TResult, TActionBinder extends IActionBinder<?, TResult>> Disposable subscribeInner(
			Function<TActionBinderSet, TActionBinder> binderSet,
			Consumer<TResult> onNext,
			boolean observerOnMainThread, 
			Function<Observable<TResult>, 
			Observable<TResult>> observable,
			Function<TAction, IChannel> channel,
			boolean preventClone) {
		try {
			TActionBinder actionBinder = binderSet.apply(getActions());
			final TAction action = (TAction) actionBinder.getAction();
			
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
			
			//return o.subscribe(observer::onNext);
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
	
	
	@SuppressWarnings("unchecked")
	private <TAction extends IAction<TResult>, TParam, TResult, TActionBinder extends IActionBinder<?, TResult>> void dispatchInner(
			Function<TActionBinderSet, TActionBinder> binderSet,
			//final TAction action,
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
			
			TActionBinder actionBinder = binderSet.apply(getActions());
			final TAction action = (TAction) actionBinder.getAction();
			
			Observable<TResult> resultObservable = null;
			if (action instanceof IParameterizedAction<?, ?>) {
				resultObservable = ((IParameterizedAction<TParam, TResult>)action).process(parameter);
			} else {
				resultObservable = ((IAction<TResult>)action).process();
			}
			
			resultObservable = resultObservable
					.timeout(Simplex.DefaultActionTimeoutMilliseconds, TimeUnit.MILLISECONDS)
					.onErrorResumeNext(throwable -> {
						TransformedResult<TResult> transform = ((IAction<TResult>)action).transform(throwable);
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
	
	private <TResult> void createChannelIfNull(IAction<TResult> action) throws IllegalArgumentException, IllegalAccessException {
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
	private <TResult> Subject<ChannelWrapper<TResult>> getOrAddObservableSource(IAction<TResult> action) throws Exception {
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

	private <TAction extends IAction<TResult>, TParam, TResult> IChannel getChannel(
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