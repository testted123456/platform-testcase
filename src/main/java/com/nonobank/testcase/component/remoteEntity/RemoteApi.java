package com.nonobank.testcase.component.remoteEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

@Component
public class RemoteApi {
	
	public static Logger logger = LoggerFactory.getLogger(RemoteApi.class);

	@Autowired
	HttpServerProperties httpServerProperties;
	
	public Optional<JSONObject> getApi(Integer id){
		logger.info("开始查询API信息， id：" + id);
		
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client = httpClient.getHttpClient();
		CloseableHttpResponse response = null;
		
		try {
			String httpServer = httpServerProperties.getInterfaceServer();
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id.toString());
			response = httpClient.doGetSend(client, null, httpServer + "/api/getApi", map);
			
			try {
				String resOfString = httpClient.getResBody(response);
				JSONObject resOfJson = JSON.parseObject(resOfString);
				
				if(resOfJson.getBoolean("succeed")){
					logger.info("查询api成功");
					JSONObject apiOfJson = resOfJson.getJSONObject("data");
					return Optional.ofNullable(apiOfJson);
				}else{
					logger.error("查询api失败，" + resOfJson.getString("errorMessage"));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.getMessage();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Optional.empty();
	}
}
