package kr.co.tmon.simplex.sample.actions;


import io.reactivex.Observable;
import kr.co.tmon.simplex.actions.AbstractAction;
import kr.co.tmon.simplex.channels.IChannel;

public class GetIntegerString extends AbstractAction<Integer, String>{

	public IChannel ExCh1;
	
	public IChannel ExCh2;
	
	@Override
	public Observable<String> process(Integer param) {
		return Observable.<String>just(param.toString());
	}
	
}
