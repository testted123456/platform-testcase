package com.nonobank.testcase.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.executor.GroupExecutor;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.result.ResultUtil;

@Controller
@RequestMapping(value="group")
@CrossOrigin(origins = "*", maxAge = 3600)
@EnableAsync
public class GroupController {
	
	public static Logger logger = LoggerFactory.getLogger(GroupController.class);
	
	@Autowired
	GroupExecutor groupExecutor;
	
	/**
	 * 获取group执行进度
	 * @param groupId
	 * @return
	 */
	@GetMapping(value="getProgress")
	@ResponseBody
	public Result getProgress(@RequestParam Integer groupId){
		float progress = groupExecutor.getGroupStatus(groupId);
		DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		String p = "0.00";
		
		if(progress != 0){
			p = decimalFormat.format(progress);
		}
		
		if(progress != -1){
			return ResultUtil.success(p);
		}else{
			return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "group已执行结束！");
		}
		
	}
	
	/**
	 * 执行group
	 * @param map
	 * @return
	 */
	@PostMapping(value="execute")
	@ResponseBody
	public Result executeGroup(@RequestBody JSONObject groupOfJson){
		int groupId = groupOfJson.getIntValue("groupId");
		
		if(groupExecutor.checkGroup(groupId) == true){
			 return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "测试集正在执行中...");
		}
		
		String env = groupOfJson.getString("env");
		int totalSize = groupOfJson.getIntValue("totalSize");
		JSONArray testCases = groupOfJson.getJSONArray("testCases");
		
		logger.info("env: {}", env);
		logger.info("totalSize:{}", totalSize);
		logger.info("testCases:{}", testCases.toJSONString());
		
		List<Object> checkedTestCases = 
		testCases.stream().filter(x->{
			if(x instanceof JSONObject){
				JSONObject jsonObj = (JSONObject)x;
				return true == jsonObj.getBooleanValue("checked");
			}else{
				return false;
			}
		}).collect(Collectors.toList());
		
		Map<String, Integer> groupMap = new HashMap<String, Integer>();
		groupMap.put("totalCount", checkedTestCases.size());
		groupMap.put("executedCount", 0);
		groupExecutor.setMap(groupId, groupMap);
		groupExecutor.runGroup(groupId, env, totalSize, checkedTestCases);
//		groupExecutor.runGroup(groupId, env, checkedTestCases.size(), checkedTestCases);
		
		return ResultUtil.success();
	}

}
