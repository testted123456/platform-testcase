package com.nonobank.testcase.component.exception;

import com.nonobank.testcase.component.result.ResultCode;

public class CaseExecutionException extends RuntimeException {

	private static final long serialVersionUID = 494078532074928893L;
	private int code;

	public CaseExecutionException(ResultCode resultCode) {
		super(resultCode.getMsg());
		this.code = resultCode.getCode();
	}
	
	public CaseExecutionException(int code, String msg){
		super(msg);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
