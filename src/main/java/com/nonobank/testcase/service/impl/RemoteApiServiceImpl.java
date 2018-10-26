package com.nonobank.testcase.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.remotecontroller.RemoteApi;
import com.nonobank.testcase.service.RemoteApiService;

@Service
public class RemoteApiServiceImpl implements RemoteApiService {
	
	public static Logger logger = LoggerFactory.getLogger(RemoteApiServiceImpl.class);

	@Autowired
	RemoteApi remoteApi;

	@Override
	public JSONObject getApi(Integer id) {
		Result result = remoteApi.getApi(id);
		
		if(ResultCode.SUCCESS.getCode().equals(result.getCode())){
			logger.error("查询api成功，id:{}" + id);
			Object data = result.getData();
			String strData = JSONObject.toJSONString(data);
			JSONObject jsonData = JSONObject.parseObject(strData);
			return jsonData;
		}else{
			logger.error("查询api失败，" + result.getMsg());
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "获取api信息失败");
		}
	}

	@Override
	public JSONObject getLastApi(String system, String module, String branch, String urlAddress) {
		
		logger.info("开始查询API信息， system：{}, module:{}, urlAddress:{}", system, module, urlAddress);
		
		Result result = remoteApi.getLastApi(system, module, urlAddress, branch);
		
		if(ResultCode.SUCCESS.getCode().equals(result.getCode())){
			Object data = result.getData();
			String strData = JSONObject.toJSONString(data);
			JSONObject jsonData = JSONObject.parseObject(strData);
			return jsonData;
		}else{
			logger.error("查询api失败，" + result.getMsg());
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "获取api信息失败");
		}
	}

	@Override
	public JSONArray getApisById(List<Integer> ids) {
		logger.info("开始查询API信");
		
		Result result = remoteApi.findByIds(ids);
		
		if(ResultCode.SUCCESS.getCode().equals(result.getCode())){
			Object data = result.getData();
			String strData = JSONObject.toJSONString(data);
			JSONArray jsonArrData = JSONObject.parseArray(strData);
			return jsonArrData;
		}else{
			logger.error("查询api失败，" + result.getMsg());
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "获取api信息失败");
		}
	}

	@Override
	public JSONArray getBranchs(String system, String gitAddress) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("system", system);
		jsonObj.put("gitAddress", gitAddress);
		
		Result result = remoteApi.getBranchs(jsonObj);
		
		if(ResultCode.SUCCESS.getCode().equals(result.getCode())){
			Object data = result.getData();
			String strData = JSONObject.toJSONString(data);
			JSONArray jsonArrData = JSONObject.parseArray(strData);
			return jsonArrData;
		}else{
			logger.error("查询系统git分支失败，" + result.getMsg());
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "查询系统git分支失败");
		}
	}

}
