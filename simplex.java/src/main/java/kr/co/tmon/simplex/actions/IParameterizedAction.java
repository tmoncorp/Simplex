package kr.co.tmon.simplex.actions;

import io.reactivex.Observable;

public interface IParameterizedAction<TParam, TResult> extends IAction<TResult> {
	
	Observable<TResult> process(TParam param);
	
	@Override
	default Observable<TResult> process() {
		return process(null);
	}
}
