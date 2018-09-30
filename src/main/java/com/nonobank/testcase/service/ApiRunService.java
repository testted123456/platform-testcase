package com.nonobank.testcase.service;

import java.util.Map;

import com.nonobank.testcase.entity.Env;
import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;

public interface ApiRunService {

	/**
	 * @param resultHistory
	 * @param testCase 待执行case
	 * @param testCaseInterface 待执行api
	 * @param env 运行环境
	 * @param map 上下文变量
	 * @param sessionId
	 * @param resultDetail
	 * @return 返回api执行结果
	 */
	public boolean runApi(ResultHistory resultHistory, 
			ResultDetail resultDetail, 
			TestCase testCase, 
			TestCaseInterface testCaseInterface, 
			Env env, 
			Map<String, Object> map, 
			String sessionId);
}
