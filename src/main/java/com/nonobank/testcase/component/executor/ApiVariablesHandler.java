package com.nonobank.testcase.component.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.exception.CaseExecutionException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.ws.WebSocket;

@Component
public class ApiVariablesHandler {

	public static Logger logger = LoggerFactory.getLogger(ApiVariablesHandler.class);
	
    @Autowired
    WebSocket webSocket;
    
    @Autowired
    ApiHandlerUtils apiHandlerUtils;
    
    /**
     * 处理所有自定义变量
     * @param map 存放自定义变量
     * @param variables 所有自定义变量字符串
     * @param sessionId
     * @param env
     * @return
     */
	public Map<String, String> handleAllVariables(Map<String, Object> map, String variables, String sessionId, String env) {
		Map<String, String> HandledVariables = new HashMap<String, String>();
		
		variables = ApiHandlerUtils.removeCRLF(variables);
//		variables = ApiHandlerUtils.removeEnter(variables);
		JSONArray varJsonArray = JSONArray.parseArray(variables);
		String res = null;
		webSocket.sendH6("处理变量", sessionId);
		
		for(Object obj : varJsonArray){
			JSONObject jsonObj = JSONObject.parseObject(obj.toString());
			String varName = jsonObj.getString("varName");
			String varValue = jsonObj.getString("varValue");
			
			if(null != varName && null != varValue){
				res = "处理变量: **" + varName + "** : " + varValue;
				logger.info(res);
				webSocket.sendItem(res, sessionId);
				
				Map<Boolean, String> resultMap = apiHandlerUtils.handleVariable(map, varValue);
				
				if(resultMap.containsKey(false)){//替换变量失败，抛异常
					String errorMsg = resultMap.get(false);
					res = "变量" + varName + "替换失败，失败原因：" + errorMsg;
					logger.error(res);
					webSocket.sendVar(res, sessionId);
					HandledVariables.put(varName, errorMsg);
					throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), res);
				}else{//变量替换没有报错
					if(resultMap.containsKey(true)){//变量替换成功，则替换方法
						String valueAfterReplace = resultMap.get(true);
						res = "变量" + varName + "替换成功，处理后值为：" + valueAfterReplace;
						logger.info(res);
						webSocket.sendVar(res, sessionId);
						HandledVariables.put(varName, valueAfterReplace);
						
						if(apiHandlerUtils.methodMatched(varValue)){//变量替换成功后，替换方法
							resultMap = apiHandlerUtils.handleMethod(valueAfterReplace, env);
						}
					}else{//没有变量要替换，则替换方法
						
						if(apiHandlerUtils.methodMatched(varValue)){
							resultMap = apiHandlerUtils.handleMethod(varValue, env);
						}
					}
					
					if(resultMap.containsKey(false)){//方法替换失败
						String errorMsg = resultMap.get(false);
						res = "变量" + varName + "中方法替换失败，失败原因为：" + errorMsg;
						logger.error(res);
//						webSocket.sendMsgTo(res, sessionId);
						HandledVariables.put(varName, errorMsg);
						throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), res);
					}else{
						if(resultMap.containsKey(true)){//方法替换成功
							String valueAfterReplace = resultMap.get(true);
							res = "变量" + varName + "中方法替换成功，处理后值为：" + valueAfterReplace;
							webSocket.sendMsgTo(res, sessionId);
							
							if(varName.contains(",")){//赋值多个变量
								String [] varArray = varName.split(",");
								
								Pattern p = Pattern.compile("\\[" + "(.+?)" + "\\]");
								Matcher m = p.matcher(valueAfterReplace);
								
								while(m.find()){
									valueAfterReplace = m.group(1);
								}
								
								String [] valueArray = valueAfterReplace.split(",");
								
								if(varArray.length != valueArray.length){
									throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), 
											varName + ": 变量个数和获得值个数不一致");
								}else{
									p = Pattern.compile("\"" + "(.+?)" + "\"");
									
									for(int i=0;i<varArray.length;i++){
										m = p.matcher(valueArray[i]);
										while(m.find()){
											valueArray[i] = m.group(1);
										}
										HandledVariables.put(varArray[i], valueArray[i]);
										map.put(varArray[i], valueArray[i]);
									}
								}
							}else{
								HandledVariables.put(varName, valueAfterReplace);
								map.put(varName, valueAfterReplace);
							}
							
						}else{//没有方法要替换
//							HandledVariables.put(varName, varValue);
							
							if(!map.containsKey(varValue)){
//								map.put(varName, varValue);
								if(varName.contains(",")){//赋值多个变量
									String [] varArray = varName.split(",");
									
									Pattern p = Pattern.compile("\\[" + "(.+?)" + "\\]");
									Matcher m = p.matcher(varValue);
									
									while(m.find()){
										varValue = m.group(1);
									}
									
									String [] valueArray = varValue.split(",");
									
									if(varArray.length != valueArray.length){
										throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), 
												varName + ": 变量个数和获得值个数不一致");
									}else{
										for(int i=0;i<varArray.length;i++){
											HandledVariables.put(varArray[i], valueArray[i]);
											map.put(varArray[i], valueArray[i]);
										}
									}
								}else{
									HandledVariables.put(varName, varValue);
									map.put(varName, varValue);
								}
							}
						}
					}
				}
			}
		}
		
		return HandledVariables;
	}
	
	public static void main(String [] args){
		String var = "\"var1\"";
		
		Pattern p2 = 
//				Pattern.compile("(.+?)(,|$)");
				Pattern.compile("\"" + "(.+?)" + "\"");
//				Pattern.compile("(.+?)([,(.+?)]|$)");
		
		Matcher m2 = p2.matcher(var);
		System.out.println(m2.groupCount());
		
		while(m2.find()){
			System.out.println(m2.group(1));
//			System.out.println(m2.group(2));
		}
	
	}

}
