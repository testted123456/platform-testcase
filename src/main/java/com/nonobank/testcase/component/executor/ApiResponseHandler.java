package com.nonobank.testcase.component.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.utils.JSONUtils;

@Component
public class ApiResponseHandler {

	public static Logger logger = LoggerFactory.getLogger(ApiResponseHandler.class);
	
	private final static Pattern JSONARRAY_PATTERN = Pattern.compile("^\\[.*?\\]$");
    
    private final static Pattern JSONOBJECT_PATTERN = Pattern.compile("^\\{.*?\\}$");
    
    private final static Pattern SAVE_PATTERN = Pattern.compile("\\&\\{(\\w*\\d*\\.*)\\}");
	
	 @Autowired
	 WebSocket webSocket;
	 
	 @Autowired
	 ApiHandlerUtils apiHandlerUtils;
	 
	 public boolean handleResponseBody(Map<String, Object> map, String expectedResponseBody, String actualResponseBody, String responseBodyType, 
			 Map<String, String> handledResponse, 
			 String sessionId){
	    	logger.info("开始处理响应消息");
	    	webSocket.sendH6("处理response", sessionId);
	    	webSocket.sendItem("预期结果", sessionId);
			
			if(JSONUtils.isJsonArray(expectedResponseBody) || JSONUtils.isJsonObject(expectedResponseBody)){
				webSocket.sendJson(expectedResponseBody, sessionId);
			}else{
				webSocket.sendVar("```", sessionId);
				webSocket.sendVar(expectedResponseBody, sessionId);
				webSocket.sendVar("```", sessionId);
			}
			
			webSocket.sendItem("实际结果", sessionId);
			
			if(JSONUtils.isJsonArray(actualResponseBody) || JSONUtils.isJsonObject(actualResponseBody)){
//				try {
//					webSocket.sendJson(JSONUtils.format(actualResponseBody), sessionId);
//				} catch (JsonParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JsonMappingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				webSocket.sendJson(actualResponseBody, sessionId);
				
			}else{
				webSocket.sendVar("```", sessionId);
				webSocket.sendVar(actualResponseBody, sessionId);
				webSocket.sendVar("```", sessionId);
			}
			
	    	boolean result = true;
	    	String res = null;
	    	
	    	if("1".equals(responseBodyType)){//text响应消息
	    		Matcher matcher = SAVE_PATTERN.matcher(expectedResponseBody);
	    		
	    		if(matcher.matches()){
	    			if(matcher.find()){
	    				map.put(matcher.group(1), actualResponseBody);
	    			}
	    			
	    			logger.info("true: 已保存变量：" + matcher.group(1) + " ，变量值为：" + actualResponseBody);
	    			
	    			webSocket.sendMsgTo("###已保存变量：" + matcher.group(1) + " ，变量值为：" + actualResponseBody, "123");
	    			handledResponse.put(matcher.group(1), actualResponseBody);
	    			return result;
	    		}
	    		
	    		if(expectedResponseBody.equals(actualResponseBody)){
	    			logger.info("true: 预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody);
	    			webSocket.sendMsgTo("###预期值:" + expectedResponseBody + " ，实际值:" + actualResponseBody, "123");
	    			handledResponse.put(expectedResponseBody, actualResponseBody);
	    		}else{
	    			logger.info("false: 预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody);
	    			webSocket.sendMsgTo("###预期结果:" + expectedResponseBody + " ,实际结果:" + actualResponseBody, "123");
	    			handledResponse.put(expectedResponseBody, actualResponseBody);
	    			result = false;
	    		}
	    		
	    		return result;
	    	}
	    	
	    	expectedResponseBody = ApiHandlerUtils.removeCRLF(expectedResponseBody);
	    	//json响应消息
	    	if(JSONOBJECT_PATTERN.matcher(expectedResponseBody).matches()){
	    		if(JSONOBJECT_PATTERN.matcher(actualResponseBody).matches()){
	    			Map<String, Boolean> resultOfMap = new HashMap<>();
	    			resultOfMap.put("result", true);
	    			
	    			apiHandlerUtils.compareJsonObj(JSONObject.parseObject(expectedResponseBody), JSONObject.parseObject(actualResponseBody), map, 
	    					handledResponse, 
	    					sessionId, resultOfMap);
	    			return resultOfMap.get("result");
	    			
	    		}else{
	    			logger.error("响应消息不是json格式");
	    			webSocket.sendItem("响应消息不是json格式", sessionId);
	    			result = false;
	    		}
	    	}
	    	
	    	return result;
	    }
}
