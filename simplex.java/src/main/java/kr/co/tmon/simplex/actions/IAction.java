package kr.co.tmon.simplex.actions;

import io.reactivex.Observable;
import kr.co.tmon.simplex.channels.IChannel;


public interface IAction<TResult> {

	TransformedResult<TResult> transform(Throwable throwable);
		
	Observable<TResult> process();
	
	IChannel getDefaultChannel();

	IChannel zip(IChannel... channels);
}

