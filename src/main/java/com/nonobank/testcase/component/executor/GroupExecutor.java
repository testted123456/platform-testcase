package com.nonobank.testcase.component.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.ResultHistoryService;
import com.nonobank.testcase.service.TestCaseInterfaceService;
import com.nonobank.testcase.service.TestCaseService;

@Component
@EnableAsync
public class GroupExecutor {
	
	public static Logger logger = LoggerFactory.getLogger(GroupExecutor.class);
	
	@Autowired
	TestCaseExecutor testCaseExecutor;
	
	@Autowired
	TestCaseInterfaceService testCaseInterfaceService;
	
	@Autowired
	TestCaseService testCaseService;
	
	@Autowired
	ResultHistoryService resultHistoryService;
	
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

	@Async
	public void runGroup(Integer groupId, String env, Integer totalSize, List<Integer> tcIDs){
		try{
			
			logger.info("开始执行group，id：{}", groupId);
			
			int size = tcIDs.size();
			Map<String, Integer> groupMap = new HashMap<String, Integer>();
			groupMap.put("totalCount", size);
			groupMap.put("executedCount", 0);
			
			ResultHistory resultHistory = resultHistoryService.add(groupId, null, tcIDs.toString(), totalSize);
			map.put(groupId, groupMap);
			
			Map<String, Object> varMap = new HashMap<String, Object>();
			
			for(Integer tcId : tcIDs){
				TestCase tc = testCaseService.findById(tcId);
				String name = tc.getName();
				logger.info("开始执行用例：{}", name);
				List<TestCaseInterface> tcis = testCaseInterfaceService.findByTestCaseId(tcId);
				
				try{
					testCaseExecutor.runCase(resultHistory, tc.getId(), null, env, tcis, varMap);
				}catch(Exception e){
					e.printStackTrace();
					logger.error("group中case执行抛异常，case：" + name);
				}
				
				int executedCount = map.get(groupId).get("executedCount");
				groupMap.put("executedCount", ++executedCount);
				logger.info("用例:{}执行完成", name);
			}
			
			logger.info("group执行完成，id：{}", groupId);
		}catch(Exception e){
			logger.error("group执行抛异常，groupId:" + groupId);
		}
		
		map.remove(groupId);
	}
	
}
