package kr.co.tmon.simplex.sample.actions;

import io.reactivex.Observable;
import kr.co.tmon.simplex.actions.AbstractUnitAction;
import kr.co.tmon.simplex.reactivex.Unit;

public class SendSignal extends AbstractUnitAction<Unit>{

	@Override
	public Observable<Unit> process(Unit unit) {
		return Observable.just(new Unit()); 
	}

}
