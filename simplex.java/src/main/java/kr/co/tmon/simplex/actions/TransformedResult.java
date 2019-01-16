package kr.co.tmon.simplex.actions;

public class TransformedResult<TResult> {
	
	private TResult result;
	
	private boolean isTransformed;
	
	public TResult getResult() {
		return result;
	}
	
	public boolean isTransformed( ) {
		return isTransformed;
	}
	
	public TransformedResult(boolean isTransformed, TResult result) {
		this.isTransformed = isTransformed;
		this.result = result;
	}
}
