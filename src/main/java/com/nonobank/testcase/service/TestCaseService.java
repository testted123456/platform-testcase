package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseFront;

public interface TestCaseService {
	
	TestCase findById(Integer id);
	
	List<TestCase> findByPId(Integer pId);
	 
	TestCase add(String userName, TestCaseFront testCaseFront, boolean type);
	
	TestCase update(String userName, TestCaseFront testCaseFront);
	
	TestCase deleteTestCase(String userName, Integer id);
	
	void deleteTestCaseDir(String userName, Integer id);
}
