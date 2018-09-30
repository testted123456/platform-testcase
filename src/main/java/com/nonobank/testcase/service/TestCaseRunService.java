package com.nonobank.testcase.service;

import java.util.List;
import java.util.Map;

import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;

public interface TestCaseRunService {
	
	/**
	 * 
	 * @param resultHistory
	 * @param testCase 待执行case
	 * @param testCaseInterfaces 待执行case中的接口
	 * @param map 上下文变量
	 * @param sessionId 
	 */
	public boolean runCase(ResultHistory resultHistory, Integer tcId, List<Integer> tciIds, Map<String, Object> map, String sessionId);

	//起线程执行case
	public void asyncRunCase(ResultHistory resultHistory, Integer tcId, List<Integer> tciIds, Map<String, Object> map, String sessionId);

}
