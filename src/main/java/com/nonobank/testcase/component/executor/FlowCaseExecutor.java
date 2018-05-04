package com.nonobank.testcase.component.executor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.entity.FlowCase;
import com.nonobank.testcase.entity.FlowCaseTestCase;
import com.nonobank.testcase.entity.GlobalVariable;
import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.FlowCaseService;
import com.nonobank.testcase.service.GlobalVariableService;
import com.nonobank.testcase.service.ResultDetailService;
import com.nonobank.testcase.service.ResultHistoryService;
import com.nonobank.testcase.service.TestCaseService;

@Component
@EnableAsync
public class FlowCaseExecutor {
	
	public static Logger logger = LoggerFactory.getLogger(FlowCaseExecutor.class);
	
	@Autowired
	TestCaseExecutor testCaseExecutor;
	
	@Autowired
	WebSocket webSocket;
	
	@Autowired
	ResultHistoryService resultHistoryService;
	
	@Autowired
	GlobalVariableService globalVariableService;
	
	@Autowired
	ResultDetailService resultDetailService;
	
	@Autowired
	FlowCaseService flowCaseService;
	
	@Autowired
	TestCaseService testCaseService;
	
	@Async
	public void runFlowCase(String user, FlowCase flowCase){
		logger.info("开始执行flowCase，id：{}", flowCase.getId());
		
		String sessionId = null;
		
		if(null != user){
			sessionId = flowCase.getId() + user + "2";
		}
		
		webSocket.sendH5("执行用例：" + flowCase.getName(), sessionId);
		
		List<FlowCaseTestCase> flowCaseTestCases = flowCase.getFlowCaseTestCases();
		
		List<Integer> tcIds = flowCaseTestCases.stream().map(x->{
			return x.getTestCaseId();
		}).collect(Collectors.toList());
		
//		List<TestCase> tcs = flowCase.getTestCases();
//		List<Integer> tcIds = tcs.stream().map(x->{return x.getId();}).collect(Collectors.toList());
		String env = flowCase.getEnv();
		
		ResultHistory resultHistory = new ResultHistory();
		resultHistory.setCreatedTime(LocalDateTime.now());
		resultHistory.setTcId(flowCase.getId());
		resultHistory.setTcType('1');
		resultHistory.setTcIds(tcIds == null ? null : tcIds.toString());
		resultHistoryService.add(resultHistory);
		
		Map<String, Object> varMap = new HashMap<String, Object>();
		List<GlobalVariable> listOfGlobalVars = globalVariableService.getAll();
		listOfGlobalVars.forEach(g->{
			String name = g.getName();
			String value = g.getValue();
			varMap.put(name, value);	
		});
		
		for(Integer tcId : tcIds){
			TestCase tc = testCaseService.findById(tcId);
			testCaseExecutor.runCase(resultHistory, tc.getId(), sessionId, env, tc.getTestCaseInterfaces(), varMap);
		}
		
//		for(TestCase tc : tcs){
//			List<TestCaseInterface> tcis = tc.getTestCaseInterfaces();
//			testCaseExecutor.runCase(resultHistory, tc.getId(), sessionId, env, tcis, varMap);
//		}
		
		webSocket.sendH5("用例执行结束...", sessionId);
		
	}
	
	@Async
	public void runFlowCase(String user, Integer id, String env, Integer totalSize, List<TestCase> tcs){
			logger.info("开始执行flowCase，id：{}", id);
			
			String sessionId = null;
			
			if(null != user){
				sessionId = id + user + "2";
			}
			
			List<TestCaseInterface> tcis = new ArrayList<>();
			
			List<Integer> apiIds = new ArrayList<>();
			
			for(TestCase tc : tcs){
				tcis.addAll(tc.getTestCaseInterfaces());
			}
			
			ResultHistory resultHistory = new ResultHistory();
			resultHistory.setCreatedTime(LocalDateTime.now());
			resultHistory.setTcId(id);
			resultHistory.setTcType('1');
			
			if(null != apiIds){
				resultHistory.setApiIds(
						tcis.stream().map(x->{
							return x.getId();
						}).collect(Collectors.toList()).toString()
				);
			}
			
			resultHistory.setTotalSize(totalSize);
			resultHistoryService.add(resultHistory);
			
			Map<String, Object> varMap = new HashMap<String, Object>();
			List<GlobalVariable> listOfGlobalVars = globalVariableService.getAll();
			listOfGlobalVars.forEach(g->{
				String name = g.getName();
				String value = g.getValue();
				varMap.put(name, value);	
			});
				
//				testCaseExecutor.runCase(resultHistory, id, null, env, tcis, varMap);
			
			for(TestCaseInterface tci : tcis){
				ResultDetail resultDetail = new ResultDetail();
				resultDetail.setResultHistory(resultHistory);
				resultDetail.setTcId(id);
				resultDetail.setApiId(tci.getId());
				resultDetail.setCreatedTime(LocalDateTime.now());
				
				Optional<String> tcName = tcs.stream().filter(x->{
					return x.getId().equals(id);
				}).findFirst().map(x->{return x.getName();});
				
				resultDetail.setTcName(tcName.get());
				
				boolean res = true;
						
				try{
					res =
					testCaseExecutor.runApi(resultHistory, id, sessionId, env, varMap, tci, resultDetail);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				resultDetail.setResult(res);
				resultDetailService.add(resultDetail);
			}
			
	}
	
}
