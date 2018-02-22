package com.nonobank.testcase.dataProvider.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Return {
	String type();
	String desc();
}
