package com.nonobank.testcase.component.executor;

import java.util.HashMap;
import java.util.Map;

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
	 * 处理自定义变量
	 * @param map
	 * @param variables
	 * @throws ExecutorException 
	 */
	public Map<String, String> handleAllVariables(Map<String, Object> map, String variables, String sessionId, String env) {
		Map<String, String> HandledVariables = new HashMap<String, String>();
		
		variables = ApiHandlerUtils.removeCRLF(variables);
		JSONArray varJsonArray = JSONArray.parseArray(variables);
		String res = null;
		webSocket.send6("处理变量", sessionId);
		
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
						webSocket.sendMsgTo(res, sessionId);
						HandledVariables.put(varName, errorMsg);
						throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), res);
					}else{
						if(resultMap.containsKey(true)){//方法替换成功
							String valueAfterReplace = resultMap.get(true);
							res = "变量" + varName + "中方法替换成功，处理后值为：" + valueAfterReplace;
							webSocket.sendMsgTo(res, sessionId);
							HandledVariables.put(varName, valueAfterReplace);
							map.put(varName, valueAfterReplace);
						}else{//没有方法要替换
							HandledVariables.put(varName, varValue);
							
							if(!map.containsKey(varValue)){
								map.put(varName, varValue);
							}
						}
					}
				}
			}
		}
		
		return HandledVariables;
	}

}
