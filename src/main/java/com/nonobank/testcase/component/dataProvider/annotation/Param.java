package com.nonobank.testcase.component.dataProvider.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
	String [] type();
	String [] name();
	String [] desc();
}
