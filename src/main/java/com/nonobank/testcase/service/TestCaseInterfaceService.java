package com.nonobank.testcase.service;

import java.util.List;
import java.util.Optional;
import com.nonobank.testcase.component.MessageEntity;
import com.nonobank.testcase.entity.TestCaseInterface;

public interface TestCaseInterfaceService {
	
	TestCaseInterface findById(Integer id);
	
	List<TestCaseInterface> findByTestCaseId(Integer testCaseId);
	
	MessageEntity add(Optional<TestCaseInterface> optTestCaseInterface);
	
//	MessageEntity add(List<Optional<TestCaseInterface>> optTestCaseInterfaces);
	
	MessageEntity add(List<TestCaseInterface> testCaseInterfaces);
	
	MessageEntity update(Optional<TestCaseInterface> optTestCaseInterface);
	
	MessageEntity update(List<Optional<TestCaseInterface>> optTestCaseInterfaces);
	
	

}
