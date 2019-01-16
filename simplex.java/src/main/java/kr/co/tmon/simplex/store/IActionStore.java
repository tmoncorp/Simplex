package kr.co.tmon.simplex.store;

import io.reactivex.Observable;
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

public interface IActionStore<TActionBinderSet extends IActionBinderSet> {

	/**
	 * 
	 * @param action
	 * @param parameter
	 * @param begin
	 * @param end
	 * @param channel
	 */
	<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin,
			Action end,
			Function<TAction, IChannel> channel);
	
	/**
	 * 
	 * @param action
	 * @param parameter
	 * @param begin
	 */
	<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin);
	
	<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Action begin,
			Action end);
	
	<TAction extends IParameterizedAction<TParam, TResult>, TParam, TResult> void dispatch(
			Function<TActionBinderSet, IParameterizedActionBinder<TAction, TParam, TResult>> action,
			TParam parameter,
			Function<TAction, IChannel> channel);
		
	<TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin,
			Action end,
			Function<TAction, IChannel> channel);
	
	<TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin);
	
	<TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Action begin,
			Action end);
	
	<TAction extends IAction<TResult>, TResult> void dispatch(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Function<TAction, IChannel> channel);
	
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread,
			Function<Observable<Unit>, Observable<Unit>> observable,
			Function<TAction, IChannel> channel);
	
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext);
	
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread);
	
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<Observable<Unit>, Observable<Unit>> observable);
		
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			boolean observerOnMainThread,
			Function<Observable<Unit>, Observable<Unit>> observable);
	
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<TAction, IChannel> channel,
			boolean observerOnMainThread);
	
	<TAction extends IAction<Unit>> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, Unit>> action,
			Action onNext,
			Function<Observable<Unit>, Observable<Unit>> observable,
			Function<TAction, IChannel> channel);

	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel,
			boolean preventClone);
	
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext);
	
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread);
	
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable);

	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			boolean preventClone);
	
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable);
	
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			boolean observerOnMainThread,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel);
	
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<TAction, IChannel> channel,
			boolean observerOnMainThread);
	
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel);
	
	<TAction extends IAction<TResult>, TResult> Disposable subscribe(
			Function<TActionBinderSet, IActionBinder<TAction, TResult>> action,
			Consumer<TResult> onNext,
			Function<Observable<TResult>, Observable<TResult>> observable,
			Function<TAction, IChannel> channel,
			boolean preventClone);
}


