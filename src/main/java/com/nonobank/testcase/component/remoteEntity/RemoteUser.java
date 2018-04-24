package com.nonobank.testcase.component.remoteEntity;

import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.apps.HttpClient;

@Component
public class RemoteUser {

	 	@Value("${httpServer.user.baseUrl}")
	    private String userBaseUrl;

	    @Value("${httpServer.user.roleUrlpath}")
	    private String roleUrlMapPath;
	    
	    private static final String KEY_DATA = "data";
	    
	    /**
	     * 依据系统名称来获取url映射关系
	     *
	     * @return
	     */
	    public Map<String, String> getUrlMap() throws IOException, HttpException {
	    	
	    	HttpClient httpClient = new HttpClient();
			CloseableHttpClient client = httpClient.getHttpClient();
			CloseableHttpResponse response = null;
			
//	        CloseableHttpClient client = httpClient.getHttpClient();
//	        String getMappurl = userBaseUrl + roleUrlMapPath;
	        response = httpClient.doGetSend(client, null, userBaseUrl + roleUrlMapPath, null);
//	        CloseableHttpResponse closeableHttpResponse = httpClient.doGetSend(client, null, getMappurl, null);
	        String repstr = HttpClient.getResBody(response);
	        JSONObject reobj = JSON.parseObject(repstr);
	        Map<String, String> remap = (Map) JSON.parse(String.valueOf(reobj.get(KEY_DATA)));
	        return remap;
	    }
}
