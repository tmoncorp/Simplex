package kr.co.tmon.simplex.store;

import java.util.Hashtable;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.IParameterizedAction;
import kr.co.tmon.simplex.actions.IActionBinder;
import kr.co.tmon.simplex.actions.IActionBinderSet;
import kr.co.tmon.simplex.actions.IParameterizedActionBinder;
import kr.co.tmon.simplex.channels.IChannel;
import kr.co.tmon.simplex.reactivex.Unit;

public class DisposableStore<TActionBinderSet extends IActionBinderSet> implements IActionStore<TActionBinderSet>, Disposable {

	private IActionStore<TActionBinderSet> store;
	
	public IActionStore<TActionBinderSet> getStore() {
		return store;
	}
	
	private String subscriberId;
	
	private Hashtable<String, CompositeDisposable> managedDisposables;
	
	public String getSubscriberId() {
		return subscriberId;
	}
	
	private DisposableStore() {
		managedDisposables = new Hashtable<>();
	}
	
	public DisposableStore(IActionStore<TActionBinderSet> store, String subscriberId) {
		this();
		this.store = store;
		this.subscriberId = subscriberId;
	}
	
	@Override
	public <TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action, 
			TParam parameter,
			Action begin, 
			Action end, 
			Function<TAction, IChannel> channel) {
		store.dispatch(action, parameter, begin, end, channel);
	}
	
	@Override
	public <TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin) {
		store.dispatch(action, parameter, begin, null, null);
	}
	
	@Override
	public <TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin,
			Action end) {
		store.dispatch(action, parameter, begin, end, null);
	}
	
	@Override
	public <TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Function<TAction, IChannel> channel) {
		store.dispatch(action, parameter, null, null, channel);
	}

	@Override
	public <TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action, 
			Action begin, 
			Action end,
			Function<TAction, IChannel> channel) {
		store.dispatch(action, begin, end, channel);		
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin) {
		store.dispatch(action, begin, null, null);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin,
			Action end) {
		store.dispatch(action, begin, end, null);
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action, 
			Function<TAction, IChannel> channel) {
		store.dispatch(action, null, null, channel);
	}

	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action, 
			Action onNext,
			boolean observerOnMainThread, 
			Function<Observable<Unit>,
			Observable<Unit>> observable,
			Function<TAction, IChannel> channel) {
		return addDisposable(store.subscribe(action, onNext, observerOnMainThread, observable, channel));
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext) {
		return addDisposable(store.subscribe(action, onNext, false, null, null));
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread) {
		return addDisposable(store.subscribe(action, onNext, observerOnMainThread, null, null));
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<Observable<Unit>, Observable<Unit>> observable) {
		return addDisposable(store.subscribe(action, onNext, false, observable, null));
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread,
			Function<Observable<Unit>, Observable<Unit>> observable) {
		return addDisposable(store.subscribe(action, onNext, observerOnMainThread, observable, null));
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<TAction, IChannel> channel,
			boolean observerOnMainThread) {
		return addDisposable(store.subscribe(action, onNext, observerOnMainThread, null, channel));
	}
	
	@Override
	public <TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<Observable<Unit>, Observable<Unit>> observable,
			Function<TAction, IChannel> channel) {
		return addDisposable(store.subscribe(action, onNext, false, observable, channel));
	}

	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action, 
			Consumer<TResult> onNext,
			boolean observerOnMainThread, 
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel,
			boolean preventClone) {
		return addDisposable(store.subscribe(action, onNext, observerOnMainThread, observable, channel, preventClone));
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext) {
		return addDisposable(store.subscribe(action, onNext, false, null, null));
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread) {
		return addDisposable(store.subscribe(action, onNext, observerOnMainThread, null, null));
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable) {
		return addDisposable(store.subscribe(action, onNext, false, observable, null));
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			boolean preventClone) {
		return addDisposable(store.subscribe(action, onNext, observerOnMainThread, null, null, preventClone));
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable) {
		return addDisposable(store.subscribe(action, onNext, observerOnMainThread, observable, null));
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel) {
		return addDisposable(store.subscribe(action, onNext, observerOnMainThread, observable, channel));
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<TAction, IChannel> channel,
			boolean observerOnMainThread) {
		return addDisposable(store.subscribe(action, onNext,observerOnMainThread, null, channel));
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel) {
		return addDisposable(store.subscribe(action, onNext, false, observable, channel));
	}
	
	@Override
	public <TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel,
			boolean preventClone) {
		return addDisposable(store.subscribe(action, onNext, false, observable, channel, preventClone));
	}

	@Override
	public void dispose() {
		if (managedDisposables.containsKey(subscriberId)) {
			CompositeDisposable disposable = managedDisposables.remove(subscriberId);
			if (!disposable.isDisposed()) {
				disposable.dispose();
			}
		}
	}

	@Override
	public boolean isDisposed() {
		if (managedDisposables.containsKey(subscriberId)) {
			CompositeDisposable disposable = managedDisposables.get(subscriberId);
			return disposable.isDisposed();
		}
		return true;
	}
	
	private Disposable addDisposable(Disposable disposable) {
		if (disposable == null) {
			return null;
		}
		
		if (managedDisposables.containsKey(subscriberId)) {
			CompositeDisposable disposables = managedDisposables.get(subscriberId);
			if (!disposables.isDisposed()) {
				disposables.add(disposable);
			}
		} else {
			managedDisposables.put(subscriberId, new CompositeDisposable(disposable));
		}
		return disposable;
	}

}
