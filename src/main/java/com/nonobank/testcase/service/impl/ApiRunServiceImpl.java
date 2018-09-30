package com.nonobank.testcase.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.nonobank.apps.NonoDnsResolver;
import com.nonobank.testcase.component.dataProvider.common.SignatureUtils;
import com.nonobank.testcase.component.exception.CaseExecutionException;
import com.nonobank.testcase.component.executor.ApiAssertionsHandler;
import com.nonobank.testcase.component.executor.ApiHandlerUtils;
import com.nonobank.testcase.component.executor.ApiRequestHandler;
import com.nonobank.testcase.component.executor.ApiResponseHandler;
import com.nonobank.testcase.component.executor.ApiVariablesHandler;
import com.nonobank.testcase.component.executor.HttpExecutor;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.entity.DBCfg;
import com.nonobank.testcase.entity.Env;
import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.SystemEnv;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.ApiRunService;
import com.nonobank.testcase.service.DBCfgService;
import com.nonobank.testcase.service.SystemEnvService;
import com.nonobank.testcase.utils.JSONUtils;
import com.nonobank.testcase.utils.dll.DBUtils;

@Service
public class ApiRunServiceImpl implements ApiRunService {
	
	public static Logger logger = LoggerFactory.getLogger(ApiRunServiceImpl.class);
	
	@Autowired
	WebSocket webSocket;
	
	@Autowired
	SystemEnvService systemEnvService;
	
	@Autowired
	ApiHandlerUtils apiHandlerUtils;
	
	@Autowired
	ApiVariablesHandler apiVariablesHandler;
	
	@Autowired
	ApiRequestHandler apiRequestHandler;
	
	@Autowired
	DBCfgService dbCfgService;
	
	@Autowired
	HttpExecutor httpExecutor;
	
	@Autowired
	ApiResponseHandler apiResponseHandler;
	
	@Autowired
	ApiAssertionsHandler apiAssertionsHandler;

	@Override
	public boolean runApi(ResultHistory resultHistory, ResultDetail resultDetail, TestCase testCase,
			TestCaseInterface testCaseInterface, Env env, Map<String, Object> map, String sessionId) {
		
		boolean result = true;
		
		Integer tciId = testCaseInterface.getId();
		
		logger.info("开始执行api，id：{}", tciId);
		
		String apiName = testCaseInterface.getInterfaceName();
		
		webSocket.sendH6("执行接口：" + apiName, sessionId);
		
		//0:Http;1:Https;2:MQ
		Character apiType = testCaseInterface.getApiType();
		
		//0:get，1:post
		Character postWay = testCaseInterface.getPostWay();
		
		String requestHeaders = testCaseInterface.getRequestHead();
		
		String system = testCaseInterface.getSystem();
		
		String url = testCaseInterface.getUrlAddress();
		
		//记录日志
		resultDetail.setHeaders(requestHeaders);
		resultDetail.setApiName(apiName);
		resultDetail.setApiStepName(testCaseInterface.getStep());
		resultDetail.setUrl(url);
		
		//提取消息头信息
		String requestHeaderContentType = null;
		
		if(null != requestHeaders){
			List<Map> list = JSONObject.parseArray(requestHeaders, Map.class);
			
			for(Map m : list){
				if("Content-Type".equals(String.valueOf(m.get("Key")))){
					requestHeaderContentType = String.valueOf(m.get("Value"));
				}
			}
		}
		
		if(null == requestHeaders || null == requestHeaderContentType){
			logger.error("接口未配置请求消息头，接口id：{}", tciId);
			webSocket.sendItem("【提示】接口未配置请求消息头", sessionId);
			result = false;
			resultDetail.setResult(result);
			return false;
		}
		
		//提取响应消息头信息
		String responseHeaders = testCaseInterface.getResponseHead();
		String responseHeaderContentType = null;
		String responseBodyType = null;
		
		if(null != responseHeaders){
			List<Map> list = JSONObject.parseArray(responseHeaders, Map.class);
			
			for(Map m : list){
				if("Content-Type".equals(String.valueOf(m.get("Key")))){
					responseHeaderContentType = String.valueOf(m.get("Value"));
				}
			}
		}
		
		//如果响应消息头没有设置content-type，则认为响应content-type和请求相同
		if(null == responseHeaderContentType){
			responseHeaderContentType = requestHeaderContentType;
		}
		
		switch (responseHeaderContentType) {
			case "application/x-www-form-urlencoded":
				responseBodyType = "1";
				break;
			case "application/json":
				responseBodyType = "0";
				break;
			case "application/xml":
				responseBodyType = "3";
				break;
			default:
				break;
		}
		
		//根据系统、环境的对应关系查找服务器的地址
		SystemEnv systemEnv = systemEnvService.findBySystemAndEnv(system, env.getName());
		
		if(null == systemEnv){
			logger.error("系统环境对应关系未配置，系统：{}，环境：{}", system, env);
			webSocket.sendItem("【提示】配置中没有查到系统<" + system + ">对应环境<" + env +">", sessionId);
			result = false;
			resultDetail.setResult(result);
			return false;
		}
		
		String domain = systemEnv.getDomain();
		
		String dns = systemEnv.getDns();
		
		//运行环境dns
		NonoDnsResolver dnsResolver = new NonoDnsResolver();
		
		if(null != dns){
			dnsResolver.addResolve(domain, dns);
		}
		
		url = apiHandlerUtils.handleUrl(apiType, domain, url);
		
		if(apiHandlerUtils.variableMatched(url) == true){//url中含有变量
			Map<Boolean, String> urlMap = apiHandlerUtils.handleVariable(map, url);
			if(urlMap.containsKey(true)){
				url = urlMap.get(true);
			}
		}
		
		//记录处理后的url
		resultDetail.setUrl(url);
		
		//处理自定义变量
		String variables = testCaseInterface.getVariables();
		
		if(null != variables){
			Map<String, String> HandledVariables = apiVariablesHandler.handleAllVariables(map, variables, sessionId, env.getName());
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
				webSocket.sendH6("请求消息头：", sessionId);
			}else if(resultOfHeaders.containsKey(false)){
				webSocket.sendH6("请求消息头处理失败：" + resultOfHeaders.get(false), sessionId);
				result = false;
				resultDetail.setResult(result);
				return result;
			}
			
			webSocket.sendJson(requestHeaders, sessionId);
		}
		
		webSocket.sendH6("接口地址", sessionId);
		webSocket.sendItem(url, sessionId);
		
		//处理请求消息体
		String requestBody = testCaseInterface.getRequestBody();
		
		if(null != requestBody){
			requestBody = apiRequestHandler.replaceVariables(map, requestBody, sessionId);
		}
		
		//前置系统加签
		if(null != requestBody){
			JSONObject jsonObj = JSONObject.parseObject(requestBody);
			JSONObject reqXML = jsonObj.getJSONObject("requestXml");
			String sign = reqXML.getString("sign");
			String appUser = reqXML.getString("appUser");
			String version = reqXML.getString("version");
			String clientType = reqXML.getString("clientType");
			
			//查询密钥
			Integer dbGroupId = env.getDbGroup().getId();
			String dbGroupName = "qtpay";
			DBCfg dbCfg = dbCfgService.findByDbGroupIdAndName(dbGroupId, dbGroupName);
			
			if(null == dbCfg){
				dbGroupName = "default";
				dbCfg = dbCfgService.findByDbGroupIdAndName(dbGroupId, dbGroupName);
			}
			
			if("true".equals(sign) && null != dbCfg){
				try {
				    Connection con = DBUtils.getConnection("oracle.jdbc.driver.OracleDriver", 
							"jdbc:oracle:thin:@" + dbCfg.getIp() + ":" + dbCfg.getPort() + ":" + dbCfg.getDbName(), 
							dbCfg.getUserName(), 
							dbCfg.getPassword());
				    String sql = "select key"
				    		+ " from prep_client_version where client_type='" + clientType + "'"
				    		+ " and client_version='" + version + "'"
				    		+ " and appuser='" + appUser + "'";
				    Object key = DBUtils.getOneObject(con, sql);
				    logger.info("key is {}", String.valueOf(key));
				    reqXML.put("sign", String.valueOf(key));
				    String reqXMLOfStr = reqXML.toJSONString();
				    String newSign =  SignatureUtils.md5(URLEncoder.encode(reqXMLOfStr, "UTF-8")).toUpperCase();
				    logger.info("new sign is: {}",newSign);
				    reqXMLOfStr = reqXMLOfStr.replace(String.valueOf(key), newSign);
				    logger.info("requestXml:{}", reqXMLOfStr);
				    jsonObj.put("requestXml", reqXMLOfStr);
				    requestBody = jsonObj.toJSONString();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	
			logger.info("请求消息加签后内容：{}", requestBody);
		}
		
		resultDetail.setRequestBody(requestBody);
		
		//预计结果
		String expectedResponseBody = null;
		
		//实际响应消息
		String actualResponseBody = null;
		
		CloseableHttpResponse resp = null;
		
		try {
			resp = httpExecutor.sendHttpRequest(apiType, postWay, requestHeaderContentType, url, requestHeaders, requestBody, dnsResolver);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		if(!"2".equals(apiType)){
			expectedResponseBody = testCaseInterface.getResponseBody();
		}
		
		if(resp != null){
			try {
				actualResponseBody = HttpClient.getResBody(resp);
			} catch (Exception exp) {
				logger.error("解析响应消息失败，" + exp.getLocalizedMessage());
				throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), exp.getLocalizedMessage());
			} 
		}
		
		logger.info("响应消息为：" + actualResponseBody);
		resultDetail.setActualResponseBody(actualResponseBody);

		if(null != expectedResponseBody){
			 Map<String, String> handledResponse = new HashMap<String, String>();
			 result = apiResponseHandler.handleResponseBody(map, expectedResponseBody, actualResponseBody, responseBodyType, 
					 handledResponse, 
					 sessionId);
			 JSONObject responseJson = new JSONObject();
			 responseJson.put("staus", result);
			 responseJson.put("result", handledResponse);
			 resultDetail.setResponseBody(JSONObject.toJSONString(responseJson));
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
		
		String assertions = testCaseInterface.getAssertions();
		
		//处理断言
		if(null != assertions){
			logger.info("开始处理断言");
			List<Map<String, String>> handledAssertions = new ArrayList<>();
			result  = apiAssertionsHandler.handleAssertions(map, handledAssertions, assertions, sessionId, env.getName());
			resultDetail.setResult(result);
			resultDetail.setAssertions(JSONObject.toJSONString(handledAssertions));
		}
		
		return result;
	}

}
