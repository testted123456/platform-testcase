package com.nonobank.testcase.component.executor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.nonobank.testcase.component.config.HttpServerProperties;
import com.nonobank.testcase.component.remoteEntity.RemoteApi;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.exception.EntityException;
import com.nonobank.testcase.exception.ExecutorException;
import com.nonobank.testcase.exception.HttpExecutorException;

@Component
@EnableAsync
public class TestCaseExecutor {
	
	public static Logger logger = LoggerFactory.getLogger(TestCaseExecutor.class);
	
	@Autowired
	HttpServerProperties httpServerProperties;
	
	@Autowired
	WebSocket webSocket;
	
	@Autowired
	HttpExecutor httpExecutor;
	
	@Autowired
	RemoteApi remoteApi;
	
	@Autowired
	VariableHandler variableHandler;
	
	@Async
	public void runCase(String sessionId, String env, List<TestCaseInterface> testCaseInterfaces) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(TestCaseInterface tcf : testCaseInterfaces){
			try {
				runApi(sessionId, env, map, tcf);
			} catch (EntityException e) {
				e.printStackTrace();
			} catch(ExecutorException e){
				e.printStackTrace();
			} catch(HttpExecutorException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 处理自定义变量
	 * @param map
	 * @param variables
	 * @throws ExecutorException 
	 */
	public void handleVariableBeforeExec(Map<String, Object> map, String variables) throws ExecutorException{
		JSONArray varJsonArray = JSONArray.parseArray(variables);
		
		for(Object obj : varJsonArray){
			JSONObject jsonObj = JSONObject.parseObject(obj.toString());
			String varName = jsonObj.getString("varName");
			String varValue = jsonObj.getString("varValue");
			
			if(null != varName && null != varValue){
				logger.info("开始处理变量:" + varName);
				Map<Boolean, String> resultMap = variableHandler.handleVariable(map, varValue);
				
				if(resultMap.containsKey(false)){//替换变量失败，抛异常
					String errorMsg = resultMap.get(false);
					logger.error("变量" + varName + "替换失败，原始值为：" + varValue + ", 失败原因：" + errorMsg);
					throw new ExecutorException(errorMsg);
				}else{//变量替换没有报错
					
					if(resultMap.containsKey(true)){//变量替换成功，则替换方法
						String valueAfterReplace = resultMap.get(true);
						logger.info("变量" + varName + "替换成功，原始值为：" + varValue + ", 处理后值为：" + valueAfterReplace);
						resultMap = variableHandler.handleMethod(valueAfterReplace);
					}else{//没有变量要替换，则替换方法
						resultMap = variableHandler.handleMethod(varValue);
					}
					
					if(resultMap.containsKey(true)){//方法替换成功
						String valueAfterReplace = resultMap.get(true);
						logger.info("变量" + varName + "中方法替换成功，原始值为：" + varValue + ", 处理后值为：" + valueAfterReplace);
						map.put(varName, valueAfterReplace);
					}else if(resultMap.containsKey(false)){//方法替换失败
						String errorMsg = resultMap.get(false);
						logger.error("变量" + varName + "中方法替换失败，原始值为：" + varValue + ", 失败原因为：" + errorMsg);
						throw new ExecutorException(errorMsg);
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
	public String handleRequestBeforeExec(Map<String, Object> map, String request) throws ExecutorException{
		if(null != request){
			Map<Boolean, String> resultMap = variableHandler.handleReqestBody(map, request);
			
			if(resultMap.containsKey(true)){
				request = resultMap.get(true);
			}else if(resultMap.containsKey(false)){
				String error_msg = resultMap.get(false);
				logger.error("请求消息体替换变量失败，失败原因：" + error_msg);
				throw new ExecutorException(error_msg);
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
	public List<String> handleResponseAfterExec(Map<String, Object> map, String expectedResponseBody, String actualResponseBody, String responseBodyType){
		List<String> list = variableHandler.handleResponseBody(map, expectedResponseBody, actualResponseBody, responseBodyType);
		return list;
	}
	
	/**
	 * 处理断言
	 * @param map
	 * @param assertions
	 */
	public void handleAssertionsAfterExec(Map<String, Object> map, String assertions){
		JSONArray assertionsJsonArray = JSONArray.parseArray(assertions);
		
		assertionsJsonArray.forEach(x->{
			JSONObject jsonObj = JSONObject.parseObject(x.toString());
			String actualResult = jsonObj.getString("actualResult");
			String expectResult = jsonObj.getString("expectResult");
			String comparator = jsonObj.getString("comparator");
			Map<Boolean, String> resultMap = variableHandler.handleVariable(map, actualResult);
			
			if(resultMap.get(true) != null){
				actualResult = resultMap.get(true);
				resultMap = variableHandler.handleMethod(actualResult);
				
				if(resultMap.containsKey(true)){
					actualResult = resultMap.get(true);
				}else{
					
				}
			}else{
				
			}
			
			resultMap = variableHandler.handleVariable(map, expectResult);
			
			if(resultMap.get(true) != null){
				expectResult = resultMap.get(true);
				resultMap = variableHandler.handleMethod(expectResult);
				
				if(resultMap.containsKey(true)){
					expectResult = resultMap.get(true);
				}
			}else{
				
			}
			
			switch (comparator) {
			case "=":
				if(Float.parseFloat(actualResult) == Float.parseFloat(expectResult)){
					
				}else{
					
				}
				break;
			case ">":
				if(Float.parseFloat(actualResult) > Float.parseFloat(expectResult)){
									
				}else{
					
				}
				break;
			case ">=":
				if(Float.parseFloat(actualResult) >= Float.parseFloat(expectResult)){
									
				}else{
					
				}
				break;
			case "<":
				if(Float.parseFloat(actualResult) < Float.parseFloat(expectResult)){
									
				}else{
					
				}
				break;
			case "<=":
				if(Float.parseFloat(actualResult) <= Float.parseFloat(expectResult)){
									
				}else{
					
				}
				break;
			case "equals":
				if(actualResult.trim().equals(expectResult.trim())){
									
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
	}
	
	/**
	 * 执行用例中的接口
	 * @param sessionId
	 * @param env
	 * @param map
	 * @param testCaseInterface
	 * @throws EntityException
	 * @throws ExecutorException 
	 * @throws HttpException 
	 * @throws com.nonobank.testcase.exception.HttpExecutorException 
	 */
	public void runApi(String sessionId, String env, Map<String, Object> map, TestCaseInterface testCaseInterface)
	throws EntityException, ExecutorException, HttpExecutorException{
		logger.info("开始执行api，id：" + testCaseInterface.getId());
		
		//查询api的协议、url
		Integer interfaceid = testCaseInterface.getInterfaceid();
		Optional<JSONObject> apiRespOfJson = remoteApi.getApi(interfaceid);
		String postWay = apiRespOfJson.map(x->x.getString("postWay")).orElseThrow(()->new EntityException("获取远程api失败， api id: " + interfaceid));
		String apiType = apiRespOfJson.map(x->x.getString("apiType")).orElseThrow(()->new EntityException("获取远程api失败， api id: " + interfaceid));
		String responseBodyType = apiRespOfJson.map(x->x.getString("requestBodyType")).orElseThrow(()->new EntityException("获取远程api失败， api id: " + interfaceid));
		Optional<CloseableHttpResponse> resp = Optional.empty();
		Optional<String> url = Optional.ofNullable(testCaseInterface.getUrlAddress());
		Optional<String> requestBody = Optional.ofNullable(testCaseInterface.getRequestBody());
		Optional<String> requestHeaders = Optional.ofNullable(testCaseInterface.getRequestHead());
		Optional<String> variables = Optional.ofNullable(testCaseInterface.getVariables());
		Optional<String> assertions = Optional.ofNullable(testCaseInterface.getAssertions());
		
		//处理自定义变量
		if(variables.isPresent()){
			handleVariableBeforeExec(map, variables.get());
		}
		
		//处理请求消息体
		if(requestBody.isPresent()){
			handleRequestBeforeExec(map, requestBody.get());
		}
		
		//发送请求 
		if(!"2".equals(apiType)){//http
			resp = httpExecutor.exec(apiType, postWay, url.get(), map, requestHeaders.get(), requestBody.get());
		}else{//mq
			
		}
		
		Optional<String> expectedResponseBody = Optional.ofNullable(testCaseInterface.getResponseBody());
		
		//处理响应消息
		String actualResponseBody = resp.map(x->{
				try{
					return HttpClient.getResBody(x);
				}catch(IOException e){
					return e.getMessage();
				}catch(HttpException e){
					return e.getMessage();
				}
			 }).orElse("response is null");
		
		 Optional<List<String>> optList = expectedResponseBody.map(x->{
			 List<String> list = handleResponseAfterExec(map, x, actualResponseBody, responseBodyType);
			 return list;
		});
		 
		//处理断言
		if(assertions.isPresent()){
			handleAssertionsAfterExec(map, assertions.get());
		}
		
//		webSocket.sendMsgTo(requestBody, sessionId);
	}
	
	public static void main(String [] args){
		String a = "1.0";
		String b = "1.000001";
		System.out.println(Float.parseFloat(a) == Float.parseFloat(b));
        
	}

}
