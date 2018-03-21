package com.nonobank.testcase.component.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.nonobank.testcase.component.config.HttpServerProperties;
import com.nonobank.testcase.component.exception.CaseExecutionException;
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
	
//	@Autowired
//	ApiExecutor apiExecutor;
	
	@Autowired
	EnvService envService;
	
	@Autowired
	SystemEnvService systemEnvService;
	
	@Autowired
	TestCaseService testCaseService;
	
	@Autowired
	ApiVariablesHandler apiVariablesHandler;
	
	@Autowired
	ApiRequestHandler apiRequestHandler;
	
	@Autowired
	ApiResponseHandler apiResponseHandler;
	
	@Autowired
	ApiAssertionsHandler apiAssertionsHandler;
	
	@Autowired
    ApiHandlerUtils apiHandlerUtils;
	
	/**
	 * 处理接口url
	 * @param apiType
	 * @param domain
	 * @param system
	 * @param url
	 * @return
	 */
	public String handleUrl(String apiType, String domain, String system, String url){
		String pattern = "([H|h][T|t][T|t][P|p][S|s]*)://(.+?)/(.*)";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(url);
		
		if(m.find() && m.groupCount() == 3){
			url = m.group(3);
		}
		
		if(url.startsWith("/")){
			url = domain + url;
		}else{
			url =  domain + "/" + url;
		}
		
		if("0".equals(apiType)){
				return "http://" + url;
		}else{
			return "https://" + url;
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
	public void runApi(String sessionId, String env, Map<String, Object> map, TestCaseInterface testCaseInterface){
		logger.info("开始执行api，id：" + testCaseInterface.getId());
		
		//查询api的协议、url
		Integer interfaceid = testCaseInterface.getInterfaceId();
		JSONObject apiRespOfJson = remoteApi.getApi(interfaceid);
		String postWay = apiRespOfJson.getString("postWay");
		String apiType =  apiRespOfJson.getString("apiType");
		String system = apiRespOfJson.getString("system");
		String apiName = apiRespOfJson.getString("name");
		String responseBodyType = apiRespOfJson.getString("responseBodyType");
		
		webSocket.sendH5("执行接口：" + apiName, sessionId);
		
		CloseableHttpResponse resp = null;
		String url = testCaseInterface.getUrlAddress();
		String requestBody = testCaseInterface.getRequestBody();
		String requestHeaders = testCaseInterface.getRequestHead();
		String variables = testCaseInterface.getVariables();
		String assertions = testCaseInterface.getAssertions();
		
		//处理自定义变量
		if(null != variables){
			apiVariablesHandler.handleAllVariables(map, variables, sessionId, env);
		}
		
		//处理接口url
		if(!"thirdparty".equals(system)){
			SystemEnv systemEnv = systemEnvService.findBySystemNameAndEnvName(system, env);
			String domain = systemEnv.getDomain();
			String dns = systemEnv.getDns();
			url = handleUrl(apiType, domain, system, url);
		}
		
		if(apiHandlerUtils.variableMatched(url) == true){
			Map<Boolean, String> urlMap = apiHandlerUtils.handleVariable(map, url);
			if(urlMap.containsKey(true)){
				url = urlMap.get(true);
			}
		}
		
		webSocket.send6("接口地址", sessionId);
		webSocket.sendItem(url, sessionId);
		
		//处理请求消息体
		if(null != requestBody){
			apiRequestHandler.handleRequest(map, requestBody, sessionId);
		}
		
		//发送请求 
		switch (apiType) {
		case "0"://http
			if("0".equals(postWay)){//get
				resp = httpExecutor.doHttpGet(url, map, requestHeaders, requestBody);
			}else{//post
				resp = httpExecutor.doHttpPost(url, map, requestHeaders, requestBody);
			}
			break;
		case "1"://https
			if("0".equals(postWay)){//get
				resp = httpExecutor.doHttpsGet(url, map, requestHeaders, requestBody);
			}else{//post
				resp = httpExecutor.doHttpsPost(url, map, requestHeaders, requestBody);
			}
			break;
		case "2":
			break;
		default:
			break;
		}
		
		String expectedResponseBody = testCaseInterface.getResponseBody();
		
		//处理响应消息
		String actualResponseBody = null;
		
		if(resp != null){
			try {
				actualResponseBody = HttpClient.getResBody(resp);
			} catch (Exception e) {
				throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), e.getMessage());
			} 
		}
		
		logger.info("响应消息为：" + actualResponseBody);

		if(null != expectedResponseBody){
			 apiResponseHandler.handleResponseBody(map, expectedResponseBody, actualResponseBody, responseBodyType, sessionId);
		}
		
		//处理断言
		logger.info("开始处理断言");
		apiAssertionsHandler.handleAssertions(map, assertions, sessionId, env);
	}
	
	@Async
	public void runCase(String sessionId, String env, List<TestCaseInterface> testCaseInterfaces) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(TestCaseInterface tcf : testCaseInterfaces){
			TestCase testCase = tcf.getTestCase();
			boolean isflow = testCase.getCaseType();
			
			try{
				runApi("123", env, map, tcf);
			}catch(Exception e){
				webSocket.sendVar("接口测试发生异常：" + e.getMessage(), "123");
				
				if(isflow == true){
					throw new TestCaseException(ResultCode.EXCEPTION_ERROR);
				}else{
					e.printStackTrace();
					continue;
				}
			}
		}
		
		webSocket.send6("用例执行完成", "123");
		logger.info("用例执行完成");
	}
	
}
