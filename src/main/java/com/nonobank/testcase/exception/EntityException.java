package com.nonobank.testcase.exception;

import java.util.ArrayList;
import java.util.List;

public class EntityException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityException() {
		super();
	}

	public EntityException(String message) {
		super(message);
	}

	public EntityException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityException(Throwable cause) {
		super(cause);
	}

	protected EntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	@Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
