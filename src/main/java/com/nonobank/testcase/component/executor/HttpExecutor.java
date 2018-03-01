package com.nonobank.testcase.component.executor;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.nonobank.apps.HttpClient;
import com.nonobank.testcase.component.config.HttpServerProperties;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.SystemEnv;
import com.nonobank.testcase.service.EnvService;

@Component
public class HttpExecutor {
	
	public static Logger logger = LoggerFactory.getLogger(HttpExecutor.class);
	
	@Autowired
	HttpServerProperties httpServerProperties;
	
	@SuppressWarnings("unchecked")
	public Optional<CloseableHttpResponse> exec(String apiType, String postWay, String url, Map<String, Object> mapOfVariables, String requestHeaders, String requestBody)  {
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client =  null;
		CloseableHttpResponse response = null;
		Map mapOfReq = JSON.parseObject(requestBody, Map.class);
		Map<String, String> mapOfHeaders = JSON.parseObject(requestHeaders, Map.class);
		
		if("0".equals(apiType)){
			client = httpClient.getHttpClient();
		}else if("1".equals(apiType)){
			try {
				client = httpClient.getHttpsClient();
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if("0".equals(postWay)){//get
			try {
				response = httpClient.doGetSend(client, null, url, mapOfReq);
			} catch (IOException e) {
				throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getMessage());
			}
			return Optional.ofNullable(response);
		}else{//post
			String contentType = mapOfHeaders.get("Content-Type");
			
			if("application/json".equals(contentType)){
				try {
					response = httpClient.doPostSendJson(client, mapOfHeaders, url, requestBody);
				} catch (IOException e) {
					throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getMessage());
				}
				return Optional.ofNullable(response);
			}else if("application/x-www-form-urlencoded".equals(contentType)){
				try {
					response = httpClient.doPostSendForm(client, mapOfHeaders, url, mapOfReq);
				} catch (IOException e) {
					throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getMessage());
				}
				return Optional.ofNullable(response);
			}
		}
		
		return Optional.empty();
	}
}
