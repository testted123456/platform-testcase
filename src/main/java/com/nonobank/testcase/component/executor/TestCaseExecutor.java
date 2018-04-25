package com.nonobank.testcase.component.executor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
import com.nonobank.apps.NonoDnsResolver;
import com.nonobank.testcase.component.config.HttpServerProperties;
import com.nonobank.testcase.component.exception.CaseExecutionException;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.remoteEntity.RemoteApi;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.entity.Env;
import com.nonobank.testcase.entity.GlobalVariable;
import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.SystemCfg;
import com.nonobank.testcase.entity.SystemEnv;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.EnvService;
import com.nonobank.testcase.service.GlobalVariableService;
import com.nonobank.testcase.service.ResultDetailService;
import com.nonobank.testcase.service.ResultHistoryService;
import com.nonobank.testcase.service.SystemCfgService;
import com.nonobank.testcase.service.SystemEnvService;
import com.nonobank.testcase.service.TestCaseService;
import com.nonobank.testcase.utils.JSONUtils;
import com.nonobank.testcase.utils.SecurityUtil;

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
	SystemCfgService systemCfgService;
	
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
	
	@Autowired
	ResultHistoryService resultHistoryService;
	
	@Autowired
	ResultDetailService resultDetailService;
	
	@Autowired
	GlobalVariableService globalVariableService;
	
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
		
		if(!url.startsWith("/") && !domain.endsWith("/")){
			url = domain + "/" + url;
		}else{
			url =  domain + url;
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
	public boolean runApi(ResultHistory resultHistory , Integer tcId, String sessionId, String env, Map<String, Object> map, TestCaseInterface testCaseInterface, ResultDetail resultDetail){
		logger.info("开始执行api，id：" + testCaseInterface.getId());
		
		boolean result = true;;
		
		//查询api的协议、url
		Integer interfaceid = testCaseInterface.getInterfaceId();
		JSONObject apiRespOfJson = remoteApi.getApi(interfaceid);
		
		//0:get，1:post
		String postWay = apiRespOfJson.getString("postWay");
		
		//0:Http;1:Https;2:MQ
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
		
		resultDetail.setHeaders(requestHeaders);
		resultDetail.setApiName(testCaseInterface.getInterfaceName());
		resultDetail.setApiStepName(testCaseInterface.getStep());
		resultDetail.setUrl(testCaseInterface.getUrlAddress());
		
		//处理自定义变量
		if(null != variables){
			Map<String, String> HandledVariables = apiVariablesHandler.handleAllVariables(map, variables, sessionId, env);
			JSONArray jsonArray = new JSONArray();
			HandledVariables.forEach((k,v)->{
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("Key", k);
				jsonObj.put("Value", v);
				jsonArray.add(jsonObj);
			});
			resultDetail.setVariables(jsonArray.toJSONString());
		}
		
		//处理消息头
		if(null != requestHeaders && apiHandlerUtils.variableMatched(requestHeaders)){
			Map<Boolean, String> resultOfHeaders = apiHandlerUtils.handleVariable(map, requestHeaders);
			
			if(resultOfHeaders.containsKey(true)){
				requestHeaders = resultOfHeaders.get(true);
				webSocket.send6("请求消息头：", sessionId);
			}else{
				webSocket.send6("请求消息头处理失败：", sessionId);
			}
			
			webSocket.sendJson(requestHeaders, sessionId);
		}
		
		//环境dns
		NonoDnsResolver dnsResolver = new NonoDnsResolver();
				
		//处理接口url
		if(!"thirdparty".equals(system)){
			SystemCfg systemCfg = 
					systemCfgService.findByAlias(system);
		    Env e = envService.findByName(env);
		    
		    if(null != e){
		    	SystemEnv systemEnv = systemEnvService.findBySystemCfgIdAndEnvId(systemCfg.getId(), e.getId());
				
				if(null != systemEnv && null != systemEnv.getDomain()){
					String domain = systemEnv.getDomain();
					String dns = systemEnv.getDns();
					
					if(null != dns){
						dnsResolver.addResolve(domain, dns);
					}
					
					url = handleUrl(apiType, domain, system, url);
				}
		    }
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
			requestBody = apiRequestHandler.handleRequest(map, requestBody, sessionId);
		}
		
		if("大前端".equals(system) || "html5".equals(system)){//大前端加签
			try {
				requestBody = SecurityUtil.doSecurity(requestBody, "mzjk").toJSONString();
				
				if(JSONUtils.isJsonArray(requestBody) || JSONUtils.isJsonObject(requestBody)){
					webSocket.sendJson(requestBody, sessionId);
				}else{
					webSocket.sendVar("```", sessionId);
					webSocket.sendVar(requestBody, sessionId);
					webSocket.sendVar("```", sessionId);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), "大前端加签失败！");
			} 
		}
		
		resultDetail.setRequestBody(requestBody);
		
		//发送请求 
		switch (apiType) {
		case "0"://http
			if("0".equals(postWay)){//get
				resp = httpExecutor.doHttpGet(url, map, requestHeaders, requestBody, dnsResolver);
			}else{//post
				resp = httpExecutor.doHttpPost(url, map, requestHeaders, requestBody, dnsResolver);
			}
			break;
		case "1"://https
			if("0".equals(postWay)){//get
				resp = httpExecutor.doHttpsGet(url, map, requestHeaders, requestBody, dnsResolver);
			}else{//post
				resp = httpExecutor.doHttpsPost(url, map, requestHeaders, requestBody, dnsResolver);
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
		resultDetail.setActualResponseBody(actualResponseBody);

		if(null != expectedResponseBody){
			 Map<String, String> handledResponse = new HashMap<String, String>();
			 result = apiResponseHandler.handleResponseBody(map, expectedResponseBody, actualResponseBody, responseBodyType, 
					 handledResponse, 
					 sessionId);
			 resultDetail.setResponseBody(JSONObject.toJSONString(handledResponse));
		}else{
			webSocket.sendItem("实际结果", sessionId);
			
			if(JSONUtils.isJsonArray(actualResponseBody) || JSONUtils.isJsonObject(actualResponseBody)){
				webSocket.sendJson(actualResponseBody, sessionId);
			}else{
				webSocket.sendVar("```", sessionId);
				webSocket.sendVar(actualResponseBody, sessionId);
				webSocket.sendVar("```", sessionId);
			}
		}
		
		resultDetail.setResult(result);
		
		//处理断言
		if(null != assertions){
			logger.info("开始处理断言");
			List<Map<String, String>> handledAssertions = new ArrayList<>();
			result  = apiAssertionsHandler.handleAssertions(map, handledAssertions, assertions, sessionId, env);
			resultDetail.setResult(result);
			resultDetail.setAssertions(JSONObject.toJSONString(handledAssertions));
		}
		
		return result;
	}
	
	@Async
	public void asyncRunCase(String sessionId, String env, Integer tcId, List<TestCaseInterface> testCaseInterfaces, int totalSize){
		Map<String, Object> varMap = new HashMap<String, Object>();
		
		List<GlobalVariable> listOfGlobalVars = globalVariableService.getAll();
		listOfGlobalVars.forEach(g->{
			String name = g.getName();
			String value = g.getValue();
			varMap.put(name, value);
		});
		
		String apiIds = testCaseInterfaces.stream().map(x->x.getId()).collect(Collectors.toList()).toString();
		ResultHistory resultHistory = resultHistoryService.add(null, tcId, null, apiIds, totalSize);
		runCase(resultHistory, tcId, sessionId, env, testCaseInterfaces, varMap);
	}
	
	/**
	 * 
	 * @param resultHistory
	 * @param tcId
	 * @param sessionId
	 * @param env
	 * @param testCaseInterfaces
	 * @param map 变量上下文
	 */
	public void runCase(ResultHistory resultHistory, Integer tcId, String sessionId, String env, List<TestCaseInterface> testCaseInterfaces,
			Map<String, Object> map) {
		
		logger.info("开始执行用例，id：{}", tcId);
		
		for(TestCaseInterface tcf : testCaseInterfaces){
			TestCase testCase = tcf.getTestCase();
			
			boolean isflow = false;
			
			if(null != testCase.getCaseType()){
				 isflow = testCase.getCaseType();
			}
			
			ResultDetail resultDetail = new ResultDetail();
			resultDetail.setApiId(tcf.getInterfaceId());
			resultDetail.setTcId(tcId);
			resultDetail.setResultHistory(resultHistory);
			resultDetail.setCreatedTime(LocalDateTime.now());
			resultDetail.setTcName(testCase.getName());
			
			try{
				boolean result = runApi(resultHistory, tcId, sessionId, env, map, tcf, resultDetail);
				resultDetailService.add(resultDetail);
				
				if(isflow == true && result == false){
					webSocket.sendH5("用例执行结束...", sessionId);
					break;
				}else{
					continue;
				}
			}catch(Exception e){
				e.printStackTrace();
				webSocket.sendVar("接口测试发生异常：" + e.getMessage(), sessionId);
				resultDetail.setException(e.getMessage());
				resultDetail.setResult(false);
				resultDetailService.add(resultDetail);
					
				if(isflow == true){
					webSocket.sendH5("用例执行结束...", sessionId);
					throw new TestCaseException(ResultCode.EXCEPTION_ERROR);
				}else{
					continue;
				}
			}
		}
		
//		webSocket.send6("用例执行完成", "123");
		logger.info("用例执行完成");
	}
	
}
