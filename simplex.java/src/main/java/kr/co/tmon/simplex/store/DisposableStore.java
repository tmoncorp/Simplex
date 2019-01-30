package kr.co.tmon.simplex.store;

import java.util.Hashtable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import kr.co.tmon.simplex.actions.IAction;
import kr.co.tmon.simplex.actions.IActionBinderSet;
import kr.co.tmon.simplex.actions.descriptor.IDispatchDescriptor;
import kr.co.tmon.simplex.actions.descriptor.ISubscriptionDescriptor;

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
	public <TAction extends IAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<DescriptorFactory<TActionBinderSet>, IDispatchDescriptor<TAction, TParam, TResult>> descriptor) {
			store.dispatch(descriptor);
	}
	
	@Override
	public <TAction extends IAction<TParam, TResult>, TParam, TResult> Disposable subscribe(
			Function<DescriptorFactory<TActionBinderSet>, ISubscriptionDescriptor<TAction, TParam, TResult>> descriptor) {
		return addDisposable(store.subscribe(descriptor));
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
