package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.TestCase;

public interface TestCaseService {
	
	TestCase findById(Integer id);
	
	List<TestCase> findByPId(Integer pId);
	 
	TestCase add(String userName, TestCase testCase, boolean type);
	
	TestCase update(String userName, TestCase testCase);
	
	TestCase deleteTestCase(String userName, Integer id);
	
	void deleteTestCaseDir(String userName, Integer id);
}
