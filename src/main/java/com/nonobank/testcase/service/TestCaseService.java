package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.TestCase;

public interface TestCaseService {
	
	TestCase findById(Integer id);
	
	List<TestCase> findByPId(Integer pId);
	 
	TestCase add(TestCase testCase, boolean type);
	
	TestCase update(TestCase testCase);
	
	TestCase delete(TestCase testCase);
}
