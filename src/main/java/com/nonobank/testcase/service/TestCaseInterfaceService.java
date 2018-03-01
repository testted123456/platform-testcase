package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.TestCaseInterface;

public interface TestCaseInterfaceService {
	
	TestCaseInterface findById(Integer id);
	
	List<TestCaseInterface> findByTestCaseId(Integer testCaseId);
	
	TestCaseInterface add(TestCaseInterface optTestCaseInterface);
	
	List<TestCaseInterface> add(List<TestCaseInterface> testCaseInterfaces);
	
	TestCaseInterface update(TestCaseInterface optTestCaseInterface);
	
	TestCaseInterface update(List<TestCaseInterface> optTestCaseInterfaces);

}
