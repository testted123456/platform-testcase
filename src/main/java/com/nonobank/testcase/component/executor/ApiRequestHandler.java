package com.nonobank.testcase.component.executor;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.utils.JSONUtils;

@Component
public class ApiRequestHandler {
	
	public static Logger logger = LoggerFactory.getLogger(ApiRequestHandler.class);
	
	 @Autowired
	 WebSocket webSocket;
	 
	 @Autowired
	 ApiHandlerUtils apiHandlerUtils;
	
	/**
	 * 替换请求消息中的变量
	 * @param map
	 * @param request
	 * @return
	 * @throws ExecutorException 
	 */
	public String handleRequest(Map<String, Object> map, String request, String sessionId) {
		request = ApiHandlerUtils.removeCRLF(request);
		webSocket.sendH6("处理request", sessionId);
		webSocket.sendItem("处理前的request", sessionId);
		
		if(JSONUtils.isJsonArray(request) || JSONUtils.isJsonObject(request)){
			webSocket.sendJson(request, sessionId);
		}else{
			webSocket.sendVar("```", sessionId);
			webSocket.sendVar(request, sessionId);
			webSocket.sendVar("```", sessionId);
		}
		
		if(null != request){
			Map<Boolean, String> resultMap = apiHandlerUtils.handleVariable(map, request);
			
			if(resultMap.containsKey(true)){
				webSocket.sendItem("处理后的request", sessionId);
				request = resultMap.get(true);
				
				if(JSONUtils.isJsonArray(request) || JSONUtils.isJsonObject(request)){
					webSocket.sendJson(request, sessionId);
				}else{
					webSocket.sendVar("```", sessionId);
					webSocket.sendVar(request, sessionId);
					webSocket.sendVar("```", sessionId);
				}
				
			}else if(resultMap.containsKey(false)){
				String errorMsg = resultMap.get(false);
				logger.error("请求消息体替换变量失败，失败原因：" + errorMsg);
				webSocket.sendVar("```", sessionId);
				webSocket.sendVar(errorMsg, sessionId);
				webSocket.sendVar("```", sessionId);
				throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), errorMsg);
			}
		}
		
		return request;
	}

}
