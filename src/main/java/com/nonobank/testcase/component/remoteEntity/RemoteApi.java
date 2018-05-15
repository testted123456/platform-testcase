package com.nonobank.testcase.component.remoteEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.nonobank.testcase.component.config.HttpServerProperties;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;

@Component
public class RemoteApi {
	
	public static Logger logger = LoggerFactory.getLogger(RemoteApi.class);

	@Autowired
	HttpServerProperties httpServerProperties;
	
	/**
	 * 根据api id查询
	 * @param id
	 * @return
	 */
	public JSONObject getApi(Integer id){
		logger.info("开始查询API信息， id：" + id);
		
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client = httpClient.getHttpClient();
		CloseableHttpResponse response = null;
		
		try {
			String httpServer = httpServerProperties.getInterfaceServer();
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", id.toString());
			response = httpClient.doGetSend(client, null, httpServer + "api/getApi", map);
			
			String resOfString = httpClient.getResBody(response);
			JSONObject resOfJson = JSON.parseObject(resOfString);
			
			if(resOfJson.getInteger("code").equals(10000)){
				logger.info("查询api成功");
				JSONObject apiOfJson = resOfJson.getJSONObject("data");
				return apiOfJson;
			}else{
				logger.error("查询api失败，" + resOfJson.getString("errorMessage"));
				throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "获取api信息失败");
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getLocalizedMessage());
		} 
	}
	
	/**
	 * 查询最新分支的api
	 * @param system
	 * @param module
	 * @param branch
	 * @param urlAddress
	 * @return
	 */
	public JSONObject getLastApi(String system, String module, String branch, String urlAddress){
		logger.info("开始查询API信息， system：{}, module:{}, urlAddress:{}", system, module, urlAddress);
		
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client = httpClient.getHttpClient();
		CloseableHttpResponse response = null;
		
		try {
			String httpServer = httpServerProperties.getInterfaceServer();
			Map<String, String> map = new HashMap<String, String>();
			map.put("system", system);
			map.put("module", module);
			map.put("branch", branch);
			map.put("urlAddress", urlAddress);
			response = httpClient.doGetSend(client, null, httpServer + "api/getLastApis", map);
			
			String resOfString = httpClient.getResBody(response);
			JSONObject resOfJson = JSON.parseObject(resOfString);
			
			if(resOfJson.getInteger("code").equals(10000)){
				logger.info("查询api成功");
				JSONObject apiOfJson = resOfJson.getJSONObject("data");
				return apiOfJson;
			}else{
				logger.error("查询api失败，" + resOfJson.getString("errorMessage"));
				throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "获取api信息失败");
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getLocalizedMessage());
		} 
	}
	
	public JSONArray getApisById(List<Integer> ids){
		logger.info("开始查询API信，");
		
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client = httpClient.getHttpClient();
		CloseableHttpResponse response = null;
		
		try {
			String httpServer = httpServerProperties.getInterfaceServer();
			response = httpClient.doPostSendJson(client, null, httpServer + "api/findByIds", ids.toString());
			
			String resOfString = httpClient.getResBody(response);
			JSONObject resOfJson = JSON.parseObject(resOfString);
			
			if(resOfJson.getInteger("code").equals(10000)){
				logger.info("查询api成功");
				JSONArray apisOfJson = resOfJson.getJSONArray("data");
				return apisOfJson;
			}else{
				logger.error("查询api失败，" + resOfJson.getString("errorMessage"));
				throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "获取api信息失败");
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getLocalizedMessage());
		} 
	}
	
	/**
	 * 查询系统git分支
	 * @param system
	 * @param gitAddress
	 * @return
	 */
	public JSONArray getSystemBranches(String system, String gitAddress){
		logger.info("开始查询系统git分支信息");
		
		HttpClient httpClient = new HttpClient();
		CloseableHttpClient client = httpClient.getHttpClient();
		CloseableHttpResponse response = null;
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("system", system);
		jsonObj.put("gitAddress", gitAddress);
		
		try {
			String httpServer = httpServerProperties.getInterfaceServer();
			response = httpClient.doPostSendJson(client, null, httpServer + "api/getBranchs", jsonObj.toJSONString());
			
			String resOfString = httpClient.getResBody(response);
			JSONObject resOfJson = JSON.parseObject(resOfString);
			
			if(resOfJson.getInteger("code").equals(10000)){
				logger.info("查询系统git分支成功");
				JSONArray apisOfJson = resOfJson.getJSONArray("data");
				return apisOfJson;
			}else{
				logger.error("查询系统git分支失败，" + resOfJson.getString("msg"));
				throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "获取系统git分支信息失败");
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new TestCaseException(ResultCode.EXCEPTION_ERROR.getCode(), e.getLocalizedMessage());
		} 
	}
	
}
