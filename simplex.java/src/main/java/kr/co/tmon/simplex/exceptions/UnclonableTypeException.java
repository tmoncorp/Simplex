package kr.co.tmon.simplex.exceptions;

public class UnclonableTypeException extends RuntimeException {

	private static final long serialVersionUID = 4361820653039146950L;

	public UnclonableTypeException() {
        super();
    }

    public UnclonableTypeException(String message) {
        super(message);
    }

    public UnclonableTypeException(String message, Throwable cause) {
        super(message, cause);
    }
	
}
