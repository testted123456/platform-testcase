package com.nonobank.testcase.service;

import java.util.List;
import java.util.Optional;
import com.nonobank.testcase.component.MessageEntity;
import com.nonobank.testcase.entity.TestCase;

public interface TestCaseService {
	
	TestCase findById(Integer id);
	
	List<TestCase> findByPId(Integer pId);
	
	MessageEntity add(Optional<TestCase> optTestCase, boolean type);
	
	MessageEntity update(Optional<TestCase> optTestCase);
	
	MessageEntity delete(Optional<TestCase> optTestCase);

}
