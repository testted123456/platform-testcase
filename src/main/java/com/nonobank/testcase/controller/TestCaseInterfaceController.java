package com.nonobank.testcase.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.remoteEntity.RemoteApi;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.entity.TestCaseInterfaceFront;
import com.nonobank.testcase.service.SystemBranchService;
import com.nonobank.testcase.service.TestCaseInterfaceService;
import com.nonobank.testcase.service.TestCaseService;
import com.nonobank.testcase.utils.JSONUtils;

@Controller
@RequestMapping(value="testCaseInterface")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestCaseInterfaceController {
	
	public static Logger logger = LoggerFactory.getLogger(TestCaseInterfaceController.class);
	
	@Autowired
	TestCaseInterfaceService testCaseInterfaceService;
	
	@Autowired
	TestCaseService testCaseService;
	
	@Autowired
	RemoteApi remoteApi;
	
	@Autowired
	SystemBranchService systemBranchService;
	
	@GetMapping(value="getByTestCaseId")
	@ResponseBody
	public Result getByTestCaseId(@RequestParam Integer testCaseId){
		logger.info("开始查询用例接口");
		
		List<TestCaseInterface> tcis = testCaseInterfaceService.findByTestCaseId(testCaseId);
		List<TestCaseInterfaceFront> tcifs = new ArrayList<>();
		
		List<Integer> ids = tcis.stream().map(x->x.getInterfaceId()).collect(Collectors.toList());
		JSONArray jsonApis = remoteApi.getApisById(ids);
		
		tcis.forEach(x->{
			TestCaseInterfaceFront tcif = x.convert();
			
			Optional<Object> obj = jsonApis.stream().filter(y->{
				JSONObject z = (JSONObject)y;
				Integer id = z.getInteger("id");
				return id.equals(x.getInterfaceId());
			}).findFirst();
			
			if(obj.get() instanceof JSONObject){
				JSONObject jsonObj = (JSONObject)obj.get();
				tcif.setName(jsonObj.getString("name"));
				tcif.setSystem(jsonObj.getString("system"));
				tcif.setBranch(jsonObj.getString("branch"));
			}
			
			tcifs.add(tcif);
		});
		
		return ResultUtil.success(tcifs);
	}
	
	
	@PostMapping(value="addCaseInterfaces")
	@ResponseBody
	public Result add(@CookieValue(value="nonousername",required=false) String userName, @RequestBody List<TestCaseInterfaceFront> tcifs){
		logger.info("开始新增用例接口");
		testCaseInterfaceService.add(userName, tcifs);
		return ResultUtil.success();
	}
	
	@PostMapping(value="updateCaseInterfaces")
	@ResponseBody
	public Result update(@CookieValue(value="nonousername",required=false) String userName, @RequestBody List<TestCaseInterfaceFront> tcifs){
		logger.info("开始新增用例接口");
		List<TestCaseInterfaceFront> result = testCaseInterfaceService.update(userName, tcifs);
		return ResultUtil.success(result);
	}
	
	@GetMapping(value="checkApi")
	@ResponseBody
	public Result checkApi(Integer id, Integer apiId, String lastBranch){
		logger.info("开始检查用例接口");
		
		//当前接口
		JSONObject currentJsonApi = remoteApi.getApi(apiId);
		String system = currentJsonApi.getString("system");
		String module = currentJsonApi.getString("module");
		String urlAddress = currentJsonApi.getString("urlAddress");
		String currentRequestBody = currentJsonApi.getString("requestBody");
		String currentResponseBody = currentJsonApi.getString("responseBody");
		
		//最新分支接口
		JSONObject lastJsonApi = remoteApi.getLastApi(system, module, lastBranch, urlAddress);
		String lastRequestBody = lastJsonApi.getString("requestBody");
		String lastResponseBody = lastJsonApi.getString("responseBody");
		
		TestCaseInterface tci = testCaseInterfaceService.findById(id);
		String requestBody = tci.getRequestBody();
		String responseBody = tci.getResponseBody();
		
		Object compareRequest = null;
		Object compareResponse = null;
		Object comprareApiRequest = null;
		Object compareApiResponse = null;
		
		if(JSONUtils.isJsonArray(currentRequestBody) && JSONUtils.isJsonArray(lastRequestBody)){
			comprareApiRequest = JSONUtils.compareJsonArray(JSONArray.parseArray(currentRequestBody), JSONArray.parseArray(lastRequestBody));
			compareRequest = JSONUtils.compareJsonArray(JSONArray.parseArray(requestBody), JSONArray.parseArray(lastRequestBody));
		}else if(JSONUtils.isJsonObject(currentRequestBody) && JSONUtils.isJsonObject(currentRequestBody)){
			comprareApiRequest = JSONUtils.compareJsonObj(JSONObject.parseObject(currentRequestBody), JSONObject.parseObject(lastRequestBody));
			compareRequest = JSONUtils.compareJsonObj(JSONObject.parseObject(responseBody), JSONObject.parseObject(lastRequestBody));
		}
		
		if(JSONUtils.isJsonArray(currentResponseBody) && JSONUtils.isJsonArray(lastResponseBody)){
			compareApiResponse = JSONUtils.compareJsonArray(JSONArray.parseArray(currentResponseBody), JSONArray.parseArray(lastResponseBody));
			compareResponse = JSONUtils.compareJsonArray(JSONArray.parseArray(responseBody), JSONArray.parseArray(lastResponseBody));
		}else if(JSONUtils.isJsonObject(currentResponseBody) && JSONUtils.isJsonObject(lastResponseBody)){
			compareApiResponse = JSONUtils.compareJsonObj(JSONObject.parseObject(currentResponseBody), JSONObject.parseObject(lastResponseBody));
			compareResponse = JSONUtils.compareJsonObj(JSONObject.parseObject(responseBody), JSONObject.parseObject(lastResponseBody));
		}
		
		JSONObject resultJson = new JSONObject();
		resultJson.put("currentRequestBody", currentRequestBody);
		resultJson.put("currentResponseBody", currentResponseBody);
		resultJson.put("lastRequestBody", lastRequestBody);
		resultJson.put("lastResponseBody", lastResponseBody);
		resultJson.put("compareRequest", compareRequest);
		resultJson.put("compareResponse", compareResponse);
		resultJson.put("comprareApiRequest", comprareApiRequest);
		resultJson.put("compareApiResponse", compareApiResponse);
		resultJson.put("requestBody", requestBody);
		resultJson.put("responseBody", responseBody);
		
		return ResultUtil.success(resultJson);
	}
}
