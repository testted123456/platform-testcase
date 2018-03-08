package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.entity.TestCaseInterfaceFront;

public interface TestCaseInterfaceService {
	
	TestCaseInterface findById(Integer id);
	
	List<TestCaseInterface> findByTestCaseId(Integer testCaseId);
	
	void add(List<TestCaseInterfaceFront> tcifs);
	
	void update(List<TestCaseInterfaceFront> tcifs);

}
