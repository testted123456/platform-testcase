package com.nonobank.testcase.exception;

public class HttpExecutorException extends Exception {

	private static final long serialVersionUID = 1L;

	public HttpExecutorException() {
		super();
	}

	public HttpExecutorException(String message) {
		super(message);
	}

	public HttpExecutorException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpExecutorException(Throwable cause) {
		super(cause);
	}

	protected HttpExecutorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	@Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
