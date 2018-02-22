package com.nonobank.testcase.exception;

public class ExecutorException extends Exception {

	private static final long serialVersionUID = 1L;

	public ExecutorException() {
		super();
	}

	public ExecutorException(String message) {
		super(message);
	}

	public ExecutorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecutorException(Throwable cause) {
		super(cause);
	}

	protected ExecutorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	@Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
