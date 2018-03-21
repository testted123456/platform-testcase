package com.nonobank.testcase.component.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.ws.WebSocket;

@Component
public class ApiExecutor {
	
	public static Logger logger = LoggerFactory.getLogger(ApiExecutor.class);
	
	@Autowired
	ApiHandler apiHandler;
	
	@Autowired
	WebSocket webSocket;
	
	/**
	 * 处理自定义变量
	 * @param map
	 * @param variables
	 * @throws ExecutorException 
	 */
	public void handleVariableBeforeExec(Map<String, Object> map, String variables, List<String> list) {
		variables = apiHandler.removeCRLF(variables);
		JSONArray varJsonArray = JSONArray.parseArray(variables);
		
		for(Object obj : varJsonArray){
			JSONObject jsonObj = JSONObject.parseObject(obj.toString());
			String varName = jsonObj.getString("varName");
			String varValue = jsonObj.getString("varValue");
			
			if(null != varName && null != varValue){
				logger.info("开始处理变量:" + varName);
				
				list.add("开始处理变量:" + varName);
				
				webSocket.sendMsgTo("- " + "处理变量: " + varName + " : " + varValue, "123");
				
				Map<Boolean, String> resultMap = apiHandler.handleVariable(map, varValue);
				
				if(resultMap.containsKey(false)){//替换变量失败，抛异常
					String errorMsg = resultMap.get(false);
					logger.error("变量" + varName + "替换失败，原始值为：" + varValue + ", 失败原因：" + errorMsg);
					
					webSocket.sendMsgTo("- " + "变量：" + varName + "处理失败，" + "失败原因：" + errorMsg, "123");
					
					list.add("变量" + varName + "替换失败，原始值为：" + varValue + ", 失败原因：" + errorMsg);
					throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), errorMsg);
				}else{//变量替换没有报错
					
					if(resultMap.containsKey(true)){//变量替换成功，则替换方法
						String valueAfterReplace = resultMap.get(true);
						logger.info("变量" + varName + "替换成功，原始值为：" + varValue + ", 处理后值为：" + valueAfterReplace);
						
						list.add("变量" + varName + "替换成功，原始值为：" + varValue + ", 处理后值为：" + valueAfterReplace);
						webSocket.sendMsgTo("###变量：" + varName + "替换成功，原始值为：" + varValue + ", 处理后值为：" + valueAfterReplace, "123");
						
						if(apiHandler.matchMethod(varValue)){
							resultMap = apiHandler.handleMethod(valueAfterReplace);
						}
					}else{//没有变量要替换，则替换方法
						if(apiHandler.matchMethod(varValue)){
							resultMap = apiHandler.handleMethod(varValue);
						}
					}
					
					if(resultMap.containsKey(false)){//方法替换失败
						String errorMsg = resultMap.get(false);
						logger.error("变量" + varName + "中方法替换失败，原始值为：" + varValue + ", 失败原因为：" + errorMsg);
						
						list.add("变量" + varName + "中方法替换失败，原始值为：" + varValue + ", 失败原因为：" + errorMsg);
						webSocket.sendMsgTo( "###变量:" + varName + "中方法替换失败，原始值为：" + varValue + ", 失败原因为：" + errorMsg, "123");
						
						throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), errorMsg);
					}else{
						if(resultMap.containsKey(true)){//方法替换成功
							String valueAfterReplace = resultMap.get(true);
							logger.info("变量" + varName + "中方法替换成功，原始值为：" + varValue + ", 处理后值为：" + valueAfterReplace);
							
							list.add("变量" + varName + "中方法替换成功，原始值为：" + varValue + ", 处理后值为：" + valueAfterReplace);
							webSocket.sendMsgTo( "###变量:" + varName + "中方法替换成功，原始值为：" + varValue + ", 处理后值为：" + valueAfterReplace, "123");
							
							map.put(varName, valueAfterReplace);
						}else{
							if(!map.containsKey(varValue)){
								map.put(varName, varValue);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 替换请求消息中的变量
	 * @param map
	 * @param request
	 * @return
	 * @throws ExecutorException 
	 */
	public String handleRequestBeforeExec(Map<String, Object> map, String request) {
		request = apiHandler.removeCRLF(request);
		
		if(null != request){
			Map<Boolean, String> resultMap = apiHandler.handleReqestBody(map, request);
			
			if(resultMap.containsKey(true)){
				request = resultMap.get(true);
			}else if(resultMap.containsKey(false)){
				String errorMsg = resultMap.get(false);
				logger.error("请求消息体替换变量失败，失败原因：" + errorMsg);
				
				throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), errorMsg);
			}
		}
		
		return request;
	}
	
	/**
	 * 处理响应消息
	 * @param map
	 * @param request
	 * @param response
	 */
	public boolean handleResponseAfterExec(Map<String, Object> map, String expectedResponseBody, String actualResponseBody, String responseBodyType, List<String> list){
		expectedResponseBody = apiHandler.removeCRLF(expectedResponseBody);
		actualResponseBody = apiHandler.removeCRLF(actualResponseBody);
		boolean result = apiHandler.handleResponseBody(map, expectedResponseBody, actualResponseBody, responseBodyType, list);
		return result;
	}
	
	/**
	 * 处理断言
	 * @param map
	 * @param assertions
	 */
	public List<String> handleAssertionsAfterExec(Map<String, Object> map, String assertions){
		assertions = apiHandler.removeCRLF(assertions);
		List<String> list = new ArrayList<String>();
		JSONArray assertionsJsonArray = JSONArray.parseArray(assertions);
		
		assertionsJsonArray.forEach(x->{
			JSONObject jsonObj = JSONObject.parseObject(x.toString());
			String actualResult = jsonObj.getString("actualResult");
			String expectResult = jsonObj.getString("expectResult");
			String comparator = jsonObj.getString("comparator");
			
			logger.info("处理断言，预期值：" + expectResult + "，实际值：" + actualResult + "，比较符：" + comparator);
			
			//处理实际结果中的变量
			Map<Boolean, String> resultMap = apiHandler.handleVariable(map, actualResult);
			
			if(resultMap.containsKey(false)){
				logger.warn("处理实际值" + actualResult + "失败，失败原因：" + resultMap.get(false));
				
				list.add("处理实际值" + actualResult + "失败，失败原因：" + resultMap.get(false));
				webSocket.sendMsgTo("###处理实际值" + actualResult + "失败，失败原因：" + resultMap.get(false), "123");
				
			}else {
				if(resultMap.containsKey(true)){
					logger.info("处理实际值" + actualResult + "成功，处理后的值：" + resultMap.get(true));
					actualResult = resultMap.get(true);
					
					list.add("true：" + "处理实际值" + actualResult + "成功，处理后的值：" + resultMap.get(true));
					webSocket.sendMsgTo("###处理实际值" + actualResult + "成功，处理后的值：" + resultMap.get(true), "123");
				}
				
				//处理实际结果中的方法
				if(!resultMap.containsKey(false) && apiHandler.matchMethod(actualResult)){
					resultMap = apiHandler.handleMethod(actualResult);
					
					if(resultMap.containsKey(false)){
						logger.error("处理实际值" + actualResult + "失败，失败原因：" + resultMap.get(false));
						
						list.add("false：" +  "处理实际值" + actualResult + "失败，失败原因：" + resultMap.get(false));
						webSocket.sendMsgTo("###处理实际值" + actualResult + "失败，失败原因：" + resultMap.get(false), "123");
					}else if(resultMap.containsKey(true)){
						actualResult = resultMap.get(true);
						logger.info("处理实际值" + actualResult + "成功，处理后的值：" + resultMap.get(true));
						list.add("true：" + "处理实际值" + actualResult + "成功，处理后的值：" + resultMap.get(true));
						webSocket.sendMsgTo("###处理实际值" + actualResult + "成功，处理后的值：" + resultMap.get(true), "123");
					}
				}
			}
			
			//处理预期结果中的变量
			resultMap = apiHandler.handleVariable(map, expectResult);
			
			if(resultMap.containsKey(false)){
				logger.warn("处理预期值" + expectResult + "失败，失败原因：" + resultMap.get(false));
				
				list.add("处理预期值" + expectResult + "失败，失败原因：" + resultMap.get(false));
				webSocket.sendMsgTo("###处理预期值" + expectResult + "失败，失败原因：" + resultMap.get(false), "123");
			}else {
				if(resultMap.containsKey(true)){
					logger.info("处理预期值" + expectResult + "成功，处理后的值：" + resultMap.get(true));
					expectResult = resultMap.get(true);
					
					list.add("true：" + "处理预期值" + expectResult + "成功，处理后的值：" + resultMap.get(true));
					webSocket.sendMsgTo("###处理预期值" + expectResult + "成功，处理后的值：" + resultMap.get(true), "123");
				}
				
				//处理预期结果中的方法
				if(!resultMap.containsKey(false) && apiHandler.matchMethod(expectResult)){
					resultMap = apiHandler.handleMethod(expectResult);
					
					if(resultMap.containsKey(false)){
						logger.error("处理预期值" + expectResult + "失败，失败原因：" + resultMap.get(false));
						
						list.add("false：" +  "处理预期值" + expectResult + "失败，失败原因：" + resultMap.get(false));
						webSocket.sendMsgTo("###处理预期值" + expectResult + "失败，失败原因：" + resultMap.get(false), "123");
						
					}else if(resultMap.containsKey(true)){
						expectResult = resultMap.get(true);
						logger.info("处理预期值" + expectResult + "成功，处理后的值：" + resultMap.get(true));
						
						list.add("true：" + "处理预期值" + expectResult + "成功，处理后的值：" + resultMap.get(true));
						webSocket.sendMsgTo("###处理预期值" + expectResult + "成功，处理后的值：" + resultMap.get(true), "123");
					}
				}
			}
			
			switch (comparator) {
				case "=":
					double dactualResult = Double.parseDouble(actualResult);
					double dExpectResult = Double.parseDouble(expectResult);
					list.add("处理断言：" + dactualResult + " " + comparator + dExpectResult);
					
					if(dactualResult == dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						
						list.add("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendMsgTo("###预期结果：" + expectResult + "，实际结果：" + actualResult, "123");
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						
						list.add("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						webSocket.sendMsgTo("###预期结果：" + expectResult + "，实际结果：" + actualResult, "123");
					}
					break;
				case ">":
					dactualResult = Double.parseDouble(actualResult);
				    dExpectResult = Double.parseDouble(expectResult);
				    
					list.add("处理断言：" + dactualResult + " " + comparator + " " + dExpectResult);
					webSocket.sendMsgTo("###处理断言：" + dactualResult  + " " + comparator + " " + dExpectResult, "123");
					
					if(dactualResult > dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
					}
					break;
				case ">=":
					dactualResult = Double.parseDouble(actualResult);
				    dExpectResult = Double.parseDouble(expectResult);
					list.add("处理断言：" + dactualResult + " " + comparator + dExpectResult);
					
					if(dactualResult >= dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
					}
					break;
				case "<":
					dactualResult = Double.parseDouble(actualResult);
				    dExpectResult = Double.parseDouble(expectResult);
					list.add("处理断言：" + dactualResult + " " + comparator + dExpectResult);
					
					if(dactualResult < dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
					}
					break;
				case "<=":
					dactualResult = Double.parseDouble(actualResult);
				    dExpectResult = Double.parseDouble(expectResult);
					list.add("处理断言：" + dactualResult + " " + comparator + dExpectResult);
					
					if(dactualResult <= dExpectResult){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
					}
					break;
				case "equals":
					list.add("处理断言：" + actualResult + " " + comparator + expectResult);
					
					if(actualResult.trim().equals(expectResult.trim())){
						logger.info("true：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("true：预期结果：" + expectResult + "，实际结果：" + actualResult);			
					}else{
						logger.warn("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
						list.add("false：预期结果：" + expectResult + "，实际结果：" + actualResult);
					}
					break;
				case "contains":
					
					break;
				case "match":
					
					break;
				default:
					break;
			}
		});
		
		return list;
	}

}
