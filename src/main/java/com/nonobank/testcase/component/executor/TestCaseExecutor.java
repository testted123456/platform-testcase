package com.nonobank.testcase.component.executor;

import java.io.IOException;
import java.util.ArrayList;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.nonobank.testcase.component.config.HttpServerProperties;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.remoteEntity.RemoteApi;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.entity.SystemEnv;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.EnvService;
import com.nonobank.testcase.service.SystemEnvService;
import com.nonobank.testcase.service.TestCaseService;

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
	ApiHandler apiHandler;
	
	@Autowired
	EnvService envService;
	
	@Autowired
	SystemEnvService systemEnvService;
	
	@Autowired
	TestCaseService testCaseService;
	
	/**
	 * 处理接口url
	 * @param apiType
	 * @param domain
	 * @param system
	 * @param url
	 * @return
	 */
	public String handleUrl(String apiType, String domain, String system, String url){
		if(null != url && url.toLowerCase().startsWith("http")){
			return url;
		}
		
		if("0".equals(apiType)){
			return "http://" + domain + "/" + url;
		}else{
			return "https://" + domain + "/" + url;
		}
	}
	
	/**
	 * 执行用例中的接口
	 * @param sessionId
	 * @param envName
	 * @param map
	 * @param testCaseInterface
	 * @throws EntityException
	 * @throws ExecutorException 
	 * @throws HttpException 
	 * @throws com.nonobank.testcase.component.exception.HttpExecutorException 
	 */
	public void runApi(String sessionId, String envName, Map<String, Object> map, TestCaseInterface testCaseInterface){
		logger.info("开始执行api，id：" + testCaseInterface.getId());
		
		//查询api的协议、url
		Integer interfaceid = testCaseInterface.getInterfaceid();
		JSONObject apiRespOfJson = remoteApi.getApi(interfaceid);
		String postWay = apiRespOfJson.getString("postWay");
		String apiType =  apiRespOfJson.getString("apiType");
		String system = apiRespOfJson.getString("system");
		String apiName = apiRespOfJson.getString("name");
		String responseBodyType =  apiRespOfJson.getString("responseBodyType");
		
		webSocket.sendMsgTo(0, "***开始执行api：" + apiName + "***", "123");
		
		Optional<CloseableHttpResponse> resp = Optional.empty();
		String url = testCaseInterface.getUrlAddress();
		String requestBody = testCaseInterface.getRequestBody();
		String requestHeaders = testCaseInterface.getRequestHead();
		String variables = testCaseInterface.getVariables();
		String assertions = testCaseInterface.getAssertions();
		
		//处理接口url
		SystemEnv systemEnv = systemEnvService.findBySystemNameAndEnvName(system, envName);
		String domain = systemEnv.getDomain();
		String dns = systemEnv.getDns();
		url = handleUrl(apiType, domain, system, url);
		
		//处理自定义变量
		List<String> varList = new ArrayList<String>();
		apiHandler.handleVariableBeforeExec(map, variables, varList);
		
		//处理请求消息体
		apiHandler.handleRequestBeforeExec(map, requestBody);
		
		//发送请求 
		if(!"2".equals(apiType)){//http
			webSocket.sendMsgTo(0, "###发送请求：" + url, "123");
			resp = httpExecutor.exec(apiType, postWay, url, map, requestHeaders, requestBody);
		}else{//mq
			
		}
		
		Optional<String> expectedResponseBody = Optional.ofNullable(testCaseInterface.getResponseBody());
		
		//处理响应消息
		String actualResponseBody = resp.map(x->{
				try{
					return HttpClient.getResBody(x);
				}catch(IOException e){
					e.printStackTrace();
					throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getClass().getName());
				}catch(HttpException e){
					e.printStackTrace();
					throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getClass().getName());
				}
			 }).orElse("response is null");
		
		logger.info("响应消息为：" + actualResponseBody);
		webSocket.sendMsgTo(0, "###响应消息为：" + actualResponseBody, "123");
		
		expectedResponseBody.map(x->{
			 List<String> list = new ArrayList<String>();
			 boolean result = apiHandler.handleResponseAfterExec(map, x, actualResponseBody, responseBodyType, list);
			 return result;
		});
		 
		//处理断言
		logger.info("开始处理断言");
		apiHandler.handleAssertionsAfterExec(map, assertions);
		
//		webSocket.sendMsgTo(requestBody, sessionId);
	}
	
	@Async
	public void runCase(String sessionId, String env, List<TestCaseInterface> testCaseInterfaces) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(TestCaseInterface tcf : testCaseInterfaces){
			TestCase testCase = tcf.getTestCase();
			boolean isflow = testCase.getCaseType();
			
			try{
				runApi(sessionId, env, map, tcf);
			}catch(Exception e){
				if(isflow == true){
					throw new TestCaseException(ResultCode.EXCEPTION_ERROR);
				}else{
					e.printStackTrace();
					continue;
				}
			}
		}
		
		logger.info("用例执行完成");
	}
	
	public static void main(String [] args){
		System.out.println(JSON.toJSON("{\"aaa\":1}").toString());
	}
	
}
