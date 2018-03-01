package com.nonobank.testcase.component.remoteEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;
import com.nonobank.testcase.component.config.HttpServerProperties;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;

@Component
public class RemoteApi {
	
	public static Logger logger = LoggerFactory.getLogger(RemoteApi.class);

	@Autowired
	HttpServerProperties httpServerProperties;
	
	public JSONObject getApi(Integer id){
		logger.info("开始查询API信息， id：" + id);
		
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client = httpClient.getHttpClient();
		CloseableHttpResponse response = null;
		
		try {
			String httpServer = httpServerProperties.getInterfaceServer();
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id.toString());
			response = httpClient.doGetSend(client, null, httpServer + "/api/getApi", map);
			
			String resOfString = httpClient.getResBody(response);
			JSONObject resOfJson = JSON.parseObject(resOfString);
			
			if(resOfJson.getBoolean("succeed")){
				logger.info("查询api成功");
				JSONObject apiOfJson = resOfJson.getJSONObject("data");
				return apiOfJson;
			}else{
				logger.error("查询api失败，" + resOfJson.getString("errorMessage"));
				throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "获取api信息失败");
			}
		} catch (HttpException e) {
			e.printStackTrace();
			throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getClass().getName());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getClass().getName());
		} catch (IOException e) {
			e.printStackTrace();
			throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getClass().getName());
		}
		
	}
}
