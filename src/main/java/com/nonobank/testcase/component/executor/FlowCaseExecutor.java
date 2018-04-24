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

import com.nonobank.testcase.entity.GlobalVariable;
import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.GlobalVariableService;
import com.nonobank.testcase.service.ResultDetailService;
import com.nonobank.testcase.service.ResultHistoryService;

@Component
@EnableAsync
public class FlowCaseExecutor {
	
	public static Logger logger = LoggerFactory.getLogger(FlowCaseExecutor.class);
	
	@Autowired
	TestCaseExecutor testCaseExecutor;
	
	@Autowired
	ResultHistoryService resultHistoryService;
	
	@Autowired
	GlobalVariableService globalVariableService;
	
	@Autowired
	ResultDetailService resultDetailService;
	
	@Async
	public void runFlowCase(Integer id, String env, Integer totalSize, List<TestCase> tcs){
			logger.info("开始执行flowCase，id：{}", id);
			
			List<TestCaseInterface> tcis = new ArrayList<>();
			
			List<Integer> apiIds = new ArrayList<>();
			
			for(TestCase tc : tcs){
				tcis.addAll(tc.getTestCaseInterfaces());
			}
			
			ResultHistory resultHistory = new ResultHistory();
			resultHistory.setCreatedTime(LocalDateTime.now());
			resultHistory.setTcId(id);
			
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
					testCaseExecutor.runApi(resultHistory, id, "123", env, varMap, tci, resultDetail);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				resultDetail.setResult(res);
				resultDetailService.add(resultDetail);
			}
			
	}
	
}
