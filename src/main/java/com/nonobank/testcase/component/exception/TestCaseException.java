package com.nonobank.testcase.component.exception;

import com.nonobank.testcase.component.result.ResultCode;

public class TestCaseException extends RuntimeException {

	private static final long serialVersionUID = 2495559366495945814L;
	private int code;

	public TestCaseException(ResultCode resultCode) {
		super(resultCode.getMsg());
		this.code = resultCode.getCode();
	}
	
	public TestCaseException(int code, String msg){
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
