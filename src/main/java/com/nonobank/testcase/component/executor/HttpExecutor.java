package com.nonobank.testcase.component.executor;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.nonobank.apps.NonoDnsResolver;
import com.nonobank.testcase.component.config.HttpServerProperties;
import com.nonobank.testcase.component.exception.CaseExecutionException;
import com.nonobank.testcase.component.result.ResultCode;

@Component
public class HttpExecutor {
	
	public static Logger logger = LoggerFactory.getLogger(HttpExecutor.class);
	
	@Autowired
	HttpServerProperties httpServerProperties;
	
	public Map<String, String> handleHeaders(String requestHeaders){
		Map<String, String> mapOfHeaders =  new HashMap<String, String>();
		JSONArray jsonArrayOfHeaders = null == requestHeaders ? null : JSONArray.parseArray(requestHeaders);
		
		if(null != jsonArrayOfHeaders){
//			mapOfHeaders = new HashMap<String, String>();
			
			for(Object objOfHead : jsonArrayOfHeaders){
				if(objOfHead instanceof JSONObject){
					JSONObject jsonObjOfHead = (JSONObject)objOfHead;
					mapOfHeaders.put(jsonObjOfHead.getString("Key"), jsonObjOfHead.getString("Value"));
				}
			}
		}
		
		String traceId = UUID.randomUUID().toString().replaceAll("-", "");
		mapOfHeaders.put("traceId", traceId);
		
		return mapOfHeaders;
	}
	
	public CloseableHttpResponse doHttpGet(String url, Map<String, Object> mapOfVariables, String requestHeaders, String requestBody, NonoDnsResolver dnsResolver) {
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client =  httpClient.getHttpClient();
		Map<String, String> mapOfReq = null == requestBody ? null : JSON.parseObject(requestBody, Map.class);
		Map<String, String> mapOfHeaders =  handleHeaders(requestHeaders);
				
		try {
			CloseableHttpResponse response = httpClient.doGetSend(client, mapOfHeaders, url, mapOfReq);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), "http get请求失败");
		}
	}
	
	public CloseableHttpResponse doHttpsGet(String url, Map<String, Object> mapOfVariables, String requestHeaders, String requestBody, NonoDnsResolver dnsResolver) {
		HttpClient httpClient = new HttpClient();
		Map<String, String> mapOfReq = null == requestBody ? null :  JSON.parseObject(requestBody, Map.class);
		Map<String, String> mapOfHeaders =  handleHeaders(requestHeaders);
		CloseableHttpClient client = null;
		
		try {
			client = httpClient.getHttpsClient();
			CloseableHttpResponse response = httpClient.doGetSend(client, mapOfHeaders, url, mapOfReq);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), "https get请求失败");
		}
		
	}
	
	public CloseableHttpResponse doPost(HttpClient httpClient, CloseableHttpClient client, String url, Map<String, Object> mapOfVariables, String requestHeaders, String requestBody){
		Map<String, String> mapOfReq = null == requestBody ? null :  JSON.parseObject(requestBody, Map.class);
		Map<String, String> mapOfHeaders =  handleHeaders(requestHeaders);
		
		String contentType = mapOfHeaders.get("Content-Type");
		
		if("application/json".equals(contentType)){
			try {
				CloseableHttpResponse response = httpClient.doPostSendJson(client, mapOfHeaders, url, requestBody);
				return response;
			} catch (IOException e) {
				e.printStackTrace();
				throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), "http post json请求失败");
			}
		}else if("application/x-www-form-urlencoded".equals(contentType)){
			try {
				CloseableHttpResponse response = httpClient.doPostSendForm(client, mapOfHeaders, url, mapOfReq);
				return response;
			} catch (IOException e) {
				e.printStackTrace();
				throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), "http post form请求失败");
			}
		}
		
		return null;
	}
	
	public CloseableHttpResponse doHttpPost(String url, Map<String, Object> mapOfVariables, String requestHeaders, String requestBody, NonoDnsResolver dnsResolver){
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client =  httpClient.getHttpClient();
		CloseableHttpResponse response = doPost(httpClient, client, url, mapOfVariables, requestHeaders, requestBody);
		return response;
	}
	
	public CloseableHttpResponse doHttpsPost(String url, Map<String, Object> mapOfVariables, String requestHeaders, String requestBody, NonoDnsResolver dnsResolver) {
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client = null;
		
		try {
			client = httpClient.getHttpsClient();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CaseExecutionException(ResultCode.EXCEPTION_ERROR.getCode(), "获取https失败");
		}
		
		CloseableHttpResponse response = doPost(httpClient, client, url, mapOfVariables, requestHeaders, requestBody);
		return response;
	}
	
	public CloseableHttpResponse sendHttpRequest(Character apiType, Character postWay, String contentType, String url, String header, String requestBody, NonoDnsResolver dnsResolver) throws KeyManagementException, NoSuchAlgorithmException, IOException{
		
		HttpClient httpClient = new HttpClient();
		
		CloseableHttpResponse response = null;
		
		CloseableHttpClient client = null;
		
		if(apiType.equals('0')){//http
			client = httpClient.getHttpClient(dnsResolver);
		}else{//https
			client = httpClient.getHttpsClient(dnsResolver);
		}
		
		final Map<String, String> mapOfHeader = new HashMap<String, String>();
		List<JSONObject> listOfHeader = null;
		
		if(header != null){
			listOfHeader = JSONArray.parseArray(header, JSONObject.class);
			listOfHeader.forEach(x->{
				String key = x.getString("Key");
				String value = x.getString("Value");
				mapOfHeader.put(key, value);
			});
		}
		
		if(postWay.equals('0')){//get
			Map<String, String> mapOfRquest = JSONObject.parseObject(requestBody, Map.class);
			response = httpClient.doGetSend(client, mapOfHeader, url, mapOfRquest);
		}else{//post
			switch (contentType) {
			case "application/x-www-form-urlencoded":
				Map<String, String> mapOfRquest = JSONObject.parseObject(requestBody, Map.class);
				response = httpClient.doPostSendForm(client, mapOfHeader, url, mapOfRquest);
				break;
			case "application/json":
				mapOfRquest = JSONObject.parseObject(requestBody, Map.class);
				response = httpClient.doPostSendJson(client, mapOfHeader, url, requestBody);
				break;
			case "application/xml":
				mapOfRquest = JSONObject.parseObject(requestBody, Map.class);
				response = httpClient.doPostSendXML(client, mapOfHeader, url, requestBody);
				break;
			default:
				break;
			}
		}
		
		return response;
	}
	
}
