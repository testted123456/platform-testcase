package com.nonobank.testcase.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.service.ResultDetailService;
import com.nonobank.testcase.service.ResultHistoryService;
import com.nonobank.testcase.utils.JSONUtils;

@Controller
@RequestMapping(value="report")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResultReportController {
	
	public static Logger logger = LoggerFactory.getLogger(ResultReportController.class);
	
	@Autowired
	ResultHistoryService resultHistoryService;
	
	@Autowired
	ResultDetailService resultDetailService;
	
	public List<ResultDetail> getReport(ResultHistory resultHistory){
		List<ResultDetail> resultDetails = resultDetailService.findByResultHistory(resultHistory);
		
		resultDetails.forEach(x->{
			String headers = x.getHeaders();
			
			if(null != headers){
				try {
					headers = JSONUtils.format(headers);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			String requestBody = x.getRequestBody();
			
			if(null != requestBody){
				try {
					requestBody = JSONUtils.format(requestBody);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			String responseBody = x.getResponseBody();
			
			if(null != responseBody){
				try {
					responseBody = JSONUtils.format(responseBody);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			String variables = x.getVariables();
			
			if(null != variables){
				try {
					variables = JSONUtils.format(variables);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		return resultDetails;
	}
	
	@GetMapping(value="getCaseReport")
	@ResponseBody
	public Result getCaseReport(@RequestParam Integer id, @RequestParam Character type){
		logger.info("查找用例最新执行记录，id：{}", id);
		ResultHistory resultHistory = resultHistoryService.findLastByTcIdAndTcType(id, type);
		List<ResultDetail> resultDetails = getReport(resultHistory);
		
		JSONArray jsonArr = new JSONArray();
		
		resultDetails.forEach(x->{
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("url", x.getUrl());
			jsonObj.put("exception", x.getException());
			jsonObj.put("tcName", x.getTcName());
			String headers = x.getHeaders();
			
			if(null != headers && !headers.equals("null")){
				jsonObj.put("headers", JSONArray.parseArray(headers));
			}else{
				jsonObj.put("headers", new JSONArray());
			}
			
			String variables = x.getVariables();
			
			if(null != variables){
				jsonObj.put("variables", JSONArray.parseArray(variables));
			}else{
				jsonObj.put("variables", new JSONArray());
			}
			
			String requestBody = x.getRequestBody();
			
			if(requestBody != null ){
				if(JSONUtils.isJsonObject(requestBody)){
					jsonObj.put("requestBody", JSONObject.parseObject(requestBody));
				}else{
					jsonObj.put("requestBody", requestBody);
				}
			}else{
				jsonObj.put("requestBody", null);
			}
			
			String responseBody = x.getResponseBody();
			
			if(responseBody != null ){
				if(JSONUtils.isJsonObject(responseBody)){
					jsonObj.put("responseBody", JSONObject.parseObject(responseBody));
				}else{
					jsonObj.put("responseBody", responseBody);
				}
			}else{
				jsonObj.put("responseBody", null);
			}
			
			String assertions = x.getAssertions();
			
			if(assertions != null ){
				if(JSONUtils.isJsonArray(assertions)){
					jsonObj.put("assertions", JSONArray.parseArray(assertions));
				}else{
					jsonObj.put("assertions", assertions);
				}
			}else{
				jsonObj.put("assertions", new JSONArray());
			}
			
			String actualResponseBody = x.getActualResponseBody();
			
			if(actualResponseBody != null ){
				if(JSONUtils.isJsonObject(actualResponseBody)){
					jsonObj.put("actualResponseBody", JSONObject.parseObject(actualResponseBody));
				}else{
					jsonObj.put("actualResponseBody", actualResponseBody);
				}
			}else{
				jsonObj.put("actualResponseBody", null);
			}
			
			jsonObj.put("apiName", x.getApiName());
			jsonObj.put("apiStepName", x.getApiStepName());
			jsonObj.put("createdTime", x.getCreatedTime());
			jsonObj.put("result", x.getResult());
			
			jsonArr.add(jsonObj);
		});
//		List<ResultDetail> resultDetails = resultDetailService.findByResultHistory(resultHistory);
//		
//		resultDetails.forEach(x->{
//			String headers = x.getHeaders();
//			
//			if(null != headers){
//				try {
//					headers = JSONUtils.format(headers);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			
//			String requestBody = x.getRequestBody();
//			
//			if(null != requestBody){
//				try {
//					requestBody = JSONUtils.format(requestBody);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			
//			String responseBody = x.getResponseBody();
//			
//			if(null != responseBody){
//				try {
//					responseBody = JSONUtils.format(responseBody);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			
//			String variables = x.getVariables();
//			
//			if(null != variables){
//				try {
//					variables = JSONUtils.format(variables);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		return ResultUtil.success(jsonArr);
	}
	
	@GetMapping(value="getGroupRunHistory")
	@ResponseBody
	public Result getGroupRunHistory(@RequestParam Integer id){
		logger.info("查找group最新执行记录，id：{}", id);
		List<ResultHistory> resultHistorys = resultHistoryService.findLast10ByGroupId(id);
		return ResultUtil.success(resultHistorys);
	}
	
	@GetMapping(value="getGroupReport")
	@ResponseBody
	public Result getGroupReport(@RequestParam Integer historyId){
		logger.info("查找historyId最新执行记录，historyId：{}", historyId);
		
		ResultHistory resultHistory = new ResultHistory();
		resultHistory.setId(historyId);
		List<ResultDetail> resultDetails = getReport(resultHistory);
		
		Map<Integer, List<ResultDetail>> map = resultDetails.stream().collect(Collectors.groupingBy(ResultDetail::getTcId));
		List<Map<String, Object>> list = new ArrayList<>();
		
		map.forEach((k,v)->{
			boolean res = true;
			Optional<ResultDetail> optRD = v.stream().filter(x->{
				if(null == x.getResult() || x.getResult() == false){
					return true;
				}else{
					return false;
				}
//				return x.getResult() == false;
				}).findAny();
			
			if(optRD.isPresent() == true){
				res = false;
			}
			
			String tcName = v.stream().findFirst().get().getTcName();
			
			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("id", k);
			resMap.put("name", tcName);
			resMap.put("data", v);
			resMap.put("result", res);
			list.add(resMap);
			
		});
		
		
		
		
//		resultDetails.stream().map(x->{
//			Integer id = x.getTcId();
//			String name = x.getTcName();
//			Object data = x;
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("id", id);
//			map.put(", value)
//			
//		});
		
		return ResultUtil.success(list);
	}

}
