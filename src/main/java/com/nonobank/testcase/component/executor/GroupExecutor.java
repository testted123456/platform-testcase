package com.nonobank.testcase.component.executor;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.entity.FlowCase;
import com.nonobank.testcase.entity.GlobalVariable;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.FlowCaseService;
import com.nonobank.testcase.service.GlobalVariableService;
import com.nonobank.testcase.service.ResultHistoryService;
import com.nonobank.testcase.service.TestCaseInterfaceService;
import com.nonobank.testcase.service.TestCaseRunService;
import com.nonobank.testcase.service.TestCaseService;

@Component
@EnableAsync
public class GroupExecutor {
	
	public static Logger logger = LoggerFactory.getLogger(GroupExecutor.class);
	
	@Autowired
	TestCaseExecutor testCaseExecutor;
	
	@Autowired
	FlowCaseExecutor flowCaseExecutor;
	
	@Autowired
	TestCaseInterfaceService testCaseInterfaceService;
	
	@Autowired
	TestCaseService testCaseService;
	
	@Autowired
	TestCaseRunService testCaseRunService;
	
	@Autowired
	FlowCaseService flowCaseService;
	
	@Autowired
	ResultHistoryService resultHistoryService;
	
	@Autowired
	GlobalVariableService globalVariableService;
	
	Map<Integer, Map<String, Integer>> map = new ConcurrentHashMap<Integer, Map<String, Integer>>();
	
	public Map<Integer, Map<String, Integer>> getMap(){
		return this.map;
	}
	
	/**
	 * 判断group是否在执行
	 * @param groupId
	 * @return
	 */
	public boolean checkGroup(Integer groupId){
		return map.containsKey(groupId);
	}
	
	/**
	 * 获取group执行进度
	 * @param groupId
	 * @return
	 */
	public float getGroupStatus(Integer groupId){
		if(!map.containsKey(groupId)){
			return -1;
		}
		
		float totalCount = map.get(groupId).get("totalCount");
		float executedCount = map.get(groupId).get("executedCount");
		return executedCount/totalCount * 100;
	}
	
	public void setMap(Integer groupId, Map<String, Integer>groupMap){
		this.map.put(groupId, groupMap);
	}

	@Async
	public void runGroup(Integer groupId, String env, Integer totalSize, List<Object> tcs){
		try{
			
			logger.info("开始执行group，id：{}", groupId);
			
			int size = tcs.size();
			Map<String, Integer> groupMap = map.get(groupId);
			
			List<Integer> tcIDs = tcs.stream().map(x->{
				JSONObject jsonObj = (JSONObject)x;
				return jsonObj.getInteger("id");
			}).collect(Collectors.toList());
					
//			new HashMap<String, Integer>();
//			groupMap.put("totalCount", size);
//			groupMap.put("executedCount", 0);
			
			ResultHistory resultHistory = resultHistoryService.add(groupId, null, tcIDs.toString(), null, totalSize);
			map.put(groupId, groupMap);
			
			Map<String, Object> varMap = new HashMap<String, Object>();
			List<GlobalVariable> listOfGlobalVars = globalVariableService.getAll();
			listOfGlobalVars.forEach(g->{
				String name = g.getName();
				String value = g.getValue();
				varMap.put(name, value);	
			});
			
			for(Object obj : tcs){
				JSONObject tcOfJson = (JSONObject)obj;
				Integer tcId = tcOfJson.getInteger("id");
				Integer caseType = tcOfJson.getInteger("caseType");
				
				if(caseType == 1){//case
					TestCase tc = testCaseService.findById(tcId);
					String name = tc.getName();
					logger.info("开始执行用例：{}", name);
					List<TestCaseInterface> tcis = testCaseInterfaceService.findByTestCaseId(tcId);
					
					List<Integer> tciIds = tcis.stream().map(TestCaseInterface::getId).collect(Collectors.toList());
					
					try{
//						testCaseExecutor.runCase(resultHistory, tc.getId(), null, env, tcis, varMap);
						testCaseRunService.runCase(resultHistory, tcId, tciIds, varMap, null);
					}catch(Exception e){
						e.printStackTrace();
						logger.error("group中case执行抛异常，case：" + name);
					}
					
					logger.info("用例:{}执行完成", name);
				}else{//flowcase
					FlowCase flowCase = flowCaseService.getById(tcId);
					String name = flowCase.getName();
					logger.info("开始执行用例：{}", name);
					
					try{
						flowCaseExecutor.runFlowCase(null, flowCase);
//						flowCaseExecutor.runFlowCase(null, flowCase.getId(), flowCase.getEnv(), flowCase.getTestCases().size(), flowCase.getTestCases());
					}catch(Exception e){
						e.printStackTrace();
						logger.error("group中case执行抛异常，case：" + name);
					}
					
					logger.info("用例:{}执行完成", name);
				}
				
				int executedCount = map.get(groupId).get("executedCount");
				groupMap.put("executedCount", ++executedCount);
			}
			
//			for(Integer tcId : tcIDs){
//				TestCase tc = testCaseService.findById(tcId);
//				String name = tc.getName();
//				logger.info("开始执行用例：{}", name);
//				List<TestCaseInterface> tcis = testCaseInterfaceService.findByTestCaseId(tcId);
//				
//				try{
//					testCaseExecutor.runCase(resultHistory, tc.getId(), null, env, tcis, varMap);
//				}catch(Exception e){
//					e.printStackTrace();
//					logger.error("group中case执行抛异常，case：" + name);
//				}
//				
//				int executedCount = map.get(groupId).get("executedCount");
//				groupMap.put("executedCount", ++executedCount);
//				logger.info("用例:{}执行完成", name);
//			}
			
			logger.info("group执行完成，id：{}", groupId);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("group执行抛异常，groupId:" + groupId);
		}
		
		map.remove(groupId);
	}
	
}
