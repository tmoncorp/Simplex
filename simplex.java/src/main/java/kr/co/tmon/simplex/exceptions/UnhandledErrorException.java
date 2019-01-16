package kr.co.tmon.simplex.exceptions;

public class UnhandledErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnhandledErrorException() {
        super();
    }

    public UnhandledErrorException(String message) {
        super(message);
    }

    public UnhandledErrorException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
