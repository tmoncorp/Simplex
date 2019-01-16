package kr.co.tmon.simplex.sample.actions;

import io.reactivex.Observable;
import kr.co.tmon.simplex.actions.AbstractAction;
import kr.co.tmon.simplex.reactivex.Unit;

public class SendSignal extends AbstractAction<Unit>{

	@Override
	public Observable<Unit> process() {
		return Observable.just(new Unit()); 
	}

}
