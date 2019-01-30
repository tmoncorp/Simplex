package kr.co.tmon.simplex.sample.actions;

import io.reactivex.Observable;
import kr.co.tmon.simplex.actions.AbstractAction;
import kr.co.tmon.simplex.channels.IChannel;

public class GetBoolean extends AbstractAction<Boolean, Boolean> {

	public IChannel ExCh1;
	
	public IChannel ExCh2;
	
	public IChannel ExCh3;
	
	@Override
	public Observable<Boolean> process(Boolean param) {
		return Observable.just(param);
	}
}
 