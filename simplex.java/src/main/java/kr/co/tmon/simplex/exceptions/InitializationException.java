package kr.co.tmon.simplex.exceptions;

public class InitializationException extends RuntimeException {

	private static final long serialVersionUID = 5840178420236096421L;

	public InitializationException() {
        super();
    }

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
