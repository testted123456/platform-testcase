package com.nonobank.testcase.component.executor;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.ws.WebSocket;

@Component
public class ApiAssertionsHandler {
	
	public static Logger logger = LoggerFactory.getLogger(ApiAssertionsHandler.class);
	
    @Autowired
    WebSocket webSocket;
    
    @Autowired
    ApiHandlerUtils apiHandlerUtils;

	public boolean handleAssertions(Map<String, Object> map, String assertions, String sessionId, String env){
		assertions = ApiHandlerUtils.removeCRLF(assertions);
		JSONArray assertionsJsonArray = JSONArray.parseArray(assertions);
		webSocket.send6("处理断言", sessionId);
		boolean res = true;
		
		for(Object object : assertionsJsonArray){
			JSONObject jsonObj = JSONObject.parseObject(object.toString());
			String actualResult = jsonObj.getString("actualResult");
			String expectResult = jsonObj.getString("expectResult");
			String comparator = jsonObj.getString("comparator");
			
			logger.info("处理断言，预期值：" + expectResult + "，实际值：" + actualResult + "，比较符：" + comparator);
			webSocket.sendItem("**断言**：" + expectResult + " " + comparator + " " + actualResult, sessionId);
			
			//处理实际结果中的变量
			Map<Boolean, String> resultMap = apiHandlerUtils.handleVariable(map, actualResult);
			
			if(resultMap.containsKey(false)){
				logger.warn("处理实际值" + actualResult + "失败，失败原因：" + resultMap.get(false));
				webSocket.sendVar("处理变量" + actualResult + "失败，失败原因：" + resultMap.get(false), sessionId);
				continue;
			}else {
				if(resultMap.containsKey(true)){
					logger.info("处理实际值" + actualResult + "成功，处理后的值：" + resultMap.get(true));
					actualResult = resultMap.get(true);
				}
				
				//处理实际结果中的方法
				if(!resultMap.containsKey(false) && apiHandlerUtils.methodMatched(actualResult)){
					resultMap = apiHandlerUtils.handleMethod(actualResult, env);
					
					if(resultMap.containsKey(false)){
						logger.warn("处理实际值" + actualResult + "失败，失败原因：" + resultMap.get(false));
						webSocket.sendVar("处理变量" + actualResult + "失败，失败原因：" + resultMap.get(false), sessionId);
						continue;
					}else if(resultMap.containsKey(true)){
						actualResult = resultMap.get(true);
						logger.info("处理实际值" + actualResult + "成功，处理后的值：" + resultMap.get(true));
						webSocket.sendVar("处理实际值" + actualResult + "成功，处理后的值：" + resultMap.get(true), sessionId);
					}
				}else if(resultMap.containsKey(true)){
					webSocket.sendVar(expectResult + " " + comparator + " " + actualResult, sessionId);
				}
			}
			
			//处理预期结果中的变量
			resultMap = apiHandlerUtils.handleVariable(map, expectResult);
			
			if(resultMap.containsKey(false)){
				logger.warn("处理预期值" + expectResult + "失败，失败原因：" + resultMap.get(false));
				webSocket.sendMsgTo("处理变量" + expectResult + "失败，失败原因：" + resultMap.get(false), sessionId);
				continue;
			}else {
				if(resultMap.containsKey(true)){
					logger.info("处理预期值" + expectResult + "成功，处理后的值：" + resultMap.get(true));
					expectResult = resultMap.get(true);
				}
				
				//处理预期结果中的方法
				if(!resultMap.containsKey(false) && apiHandlerUtils.methodMatched(expectResult)){
					resultMap = apiHandlerUtils.handleMethod(expectResult, env);
					
					if(resultMap.containsKey(false)){
						logger.warn("处理预期值" + expectResult + "失败，失败原因：" + resultMap.get(false));
						webSocket.sendMsgTo("处理变量" + expectResult + "失败，失败原因：" + resultMap.get(false), sessionId);
					}else if(resultMap.containsKey(true)){
						expectResult = resultMap.get(true);
						logger.info("处理预期值" + expectResult + "成功，处理后的值：" + resultMap.get(true));
					}
				}
			}
			
			switch (comparator) {
				case "=":
					double dactualResult = Double.parseDouble(actualResult);
					double dExpectResult = Double.parseDouble(expectResult);
					webSocket.sendVar("预期结果：" + expectResult, sessionId);
					webSocket.sendVar("实际结果：" + actualResult, sessionId);
					
					if(dactualResult == dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
						res = false;					
					}
					break;
				case ">":
					dactualResult = Double.parseDouble(actualResult);
				    dExpectResult = Double.parseDouble(expectResult);
				    
					if(dactualResult > dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
						res = false;
					}
					break;
				case ">=":
					dactualResult = Double.parseDouble(actualResult);
				    dExpectResult = Double.parseDouble(expectResult);
					
					if(dactualResult >= dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
						res = false;	
					}
					break;
				case "<":
					dactualResult = Double.parseDouble(actualResult);
				    dExpectResult = Double.parseDouble(expectResult);
					
					if(dactualResult < dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
						res = false;	
					}
					break;
				case "<=":
					dactualResult = Double.parseDouble(actualResult);
				    dExpectResult = Double.parseDouble(expectResult);
					
					if(dactualResult <= dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
						res = false;
					}
					break;
				case "equals":
					if(actualResult.trim().equals(expectResult.trim())){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: true", sessionId);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendVar("对比结果: " + "**FALSE**", sessionId);
						res = false;
					}
					break;
				case "contains":
					break;
				case "match":
					break;
				case "ignore":
					break;
				default:
					break;
			}
		}
		
		return res;
		
	}
}
