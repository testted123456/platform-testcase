package com.nonobank.testcase.controller;

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
		
		if(progress != -1){
			return ResultUtil.success(progress);
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
		
		
//		List<Integer> listOfTcIDs = null;
//		
//		Object objOfGroupId = map.get("groupId");
//		
//		if(objOfGroupId instanceof Integer){
//			 groupId = (Integer)objOfGroupId;
//			 
//			 if(groupExecutor.checkGroup(groupId) == true){
//				 return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "测试集正在执行中...");
//			 }
//		}
//		
//		//待执行case列表
//		Object objOfTcIDs = map.get("tcIDs");
//		Object objOfTC = map.get("testCases");
//		
//		
//		String env = map.get("env").toString();
//		
//		//group中case数量
//		Integer totalSize = Integer.parseInt(map.get("totalSize").toString());
//		
//		if(objOfTcIDs instanceof List){
//			listOfTcIDs = (List)objOfTcIDs;
//			Map<String, Integer> groupMap = new HashMap<String, Integer>();
//			groupMap.put("totalCount", listOfTcIDs.size());
//			groupMap.put("executedCount", 0);
//			groupExecutor.setMap(groupId, groupMap);
//			groupExecutor.runGroup(groupId, env, totalSize, objOfTC);
//		}
		
		return ResultUtil.success();
	}

}
