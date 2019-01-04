package com.nonobank.testcase.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseFront;

public interface TestCaseService {
	
	TestCase findById(Integer id);
	
	List<TestCase> findByPId(Integer pId);
	
	TestCase add(String userName, TestCaseFront testCaseFront, boolean type);
	
	TestCase update(String userName, TestCaseFront testCaseFront);
	
	Result deleteTestCase(String userName, Integer id);
	
	void deleteTestCaseDir(String userName, Integer id);
	
	List<JSONObject> searchCases(String name, String createdBy, String apiName, String urlAddress);
}
