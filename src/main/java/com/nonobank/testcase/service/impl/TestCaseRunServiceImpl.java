package com.nonobank.testcase.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.entity.Env;
import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.ApiRunService;
import com.nonobank.testcase.service.EnvService;
import com.nonobank.testcase.service.GlobalVariableService;
import com.nonobank.testcase.service.ResultDetailService;
import com.nonobank.testcase.service.ResultHistoryService;
import com.nonobank.testcase.service.TestCaseRunService;
import com.nonobank.testcase.service.TestCaseService;

@Service
public class TestCaseRunServiceImpl implements TestCaseRunService {
	
	public static Logger logger = LoggerFactory.getLogger(TestCaseRunServiceImpl.class);
	
	@Autowired
	TestCaseService testCaseService;
	
	@Autowired
	WebSocket webSocket;
	
	@Autowired
	ApiRunService apiRunService;
	
	@Autowired
	EnvService envService;
	
	@Autowired
	ResultDetailService resultDetailService;
	
	@Autowired
	ResultHistoryService resultHistoryService;
	
	@Autowired
	GlobalVariableService globalVariableService;

	@Override
	public boolean runCase(ResultHistory resultHistory, Integer tcId, 
			List<Integer> tciIds,
			Map<String, Object> map,
			String sessionId) {
		
		boolean result = true;
		
		TestCase testCase = testCaseService.findById(tcId);
		
		webSocket.sendMsgTo("### " + "开始执行用例：" + testCase.getName(), sessionId);
		
		logger.info("开始执行用例，id：{}", tcId);
		
		List<TestCaseInterface> testCaseInterfaces = testCase.getTestCaseInterfaces();
		
		Integer totalSize = testCaseInterfaces.size();
		
		resultHistory.setTotalSize(totalSize);
		
		if(resultHistory.getId() == null){//不是执行group
			resultHistoryService.add(resultHistory);
		}
		
		List<TestCaseInterface> tcisToBeRun = testCaseInterfaces.stream().filter(x->tciIds.contains(x.getId()))
				.sorted(Comparator.comparing(TestCaseInterface::getOrderNo))
				.collect(Collectors.toList());
		
		String envName = testCase.getEnv();
		
		Env env = envService.findByName(envName);
		
		for(TestCaseInterface tci : tcisToBeRun){
			boolean isflow = false;
			
			if(null != testCase.getCaseType()){
				 isflow = testCase.getCaseType();
			}
			
			ResultDetail resultDetail = new ResultDetail();
			resultDetail.setApiId(tci.getInterfaceId());
			resultDetail.setTcId(tcId);
			resultDetail.setResultHistory(resultHistory);
			resultDetail.setCreatedTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
			resultDetail.setTcName(testCase.getName());
			
			if(isflow == false){//不是流程用例，没有上下文
				map = globalVariableService.getAllVars();
			}
			
			try{
				result = apiRunService.runApi(resultHistory, resultDetail, testCase, tci, env, map, sessionId);
			}catch(Exception e){
				webSocket.sendItem("用例执行异常：" + e.getMessage(), sessionId);
				result = false;
			}
			
			resultDetailService.add(resultDetail);
			
			if(isflow == true && result == false){
//				webSocket.sendH5("用例执行结束...", sessionId);
				break;
			}else{
				continue;
			}
		}
		
		webSocket.sendH6("用例执行结束...", sessionId);
		logger.info("用例执行完成");
		
		return result;
	}

	@Override
	@Async
	public void asyncRunCase(ResultHistory resultHistory, Integer tcId, List<Integer> tciIds, Map<String, Object> map,
			String sessionId) {
		boolean result = runCase(resultHistory, tcId, tciIds, map, sessionId);
	}

}
