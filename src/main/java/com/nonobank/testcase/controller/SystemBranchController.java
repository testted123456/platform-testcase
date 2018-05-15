package com.nonobank.testcase.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.nonobank.testcase.component.remoteEntity.RemoteApi;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.SystemBranch;
import com.nonobank.testcase.entity.SystemCfg;
import com.nonobank.testcase.service.SystemBranchService;
import com.nonobank.testcase.service.SystemCfgService;

@Controller
@RequestMapping(value="sysBranch")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SystemBranchController {
	
	public static Logger logger = LoggerFactory.getLogger(SystemBranchController.class);
	
	@Autowired
	SystemBranchService systemBranchService;
	
	@Autowired
	SystemCfgService systemCfgService;
	
	@Autowired
	RemoteApi remoteApi;
	
	/**
	 * 同步系统分支
	 * @param system
	 * @return
	 */
	@GetMapping(value="syncBranches")
	@ResponseBody
	public Result syncBranches(@RequestParam String system){
		logger.info("开始同步系统 {} 分支", system);
		
		SystemCfg systemCfg = systemCfgService.findBySystem(system);
		String gitAddress = systemCfg.getGitAddress();
		JSONArray branches = remoteApi.getSystemBranches(system, gitAddress);
		List<String> list = new ArrayList<>();
		
		branches.forEach(x->{
			list.add(x.toString());
		});
		
		systemBranchService.update(system, list);
		return ResultUtil.success(systemBranchService.findall());
	}
	
	@PostMapping(value="updateBranch")
	@ResponseBody
	public Result updateBranch(@RequestBody SystemBranch systemBranch){
		logger.info("开始更新系统 {} ,分支 {}", systemBranch.getSystem(), systemBranch.getBranch());
		systemBranchService.update(systemBranch);
		return ResultUtil.success();
	}
	 
	/**
	 * 更新last字段
	 * @param systemBranch
	 * @return
	 */
	@PostMapping(value="updateLast")
	@ResponseBody
	public Result updateLast(@RequestBody SystemBranch systemBranch){
		if(null == systemBranch.getId()){
			return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "系统分支还未保存！");
		}
		
		SystemBranch sb = systemBranchService.findById(systemBranch.getId());
		
		if(null == sb){
			return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "系统分支还未保存！");
		}
		
		sb.setLast(systemBranch.getLast());
		systemBranchService.update(sb);
		return ResultUtil.success();
	}
	
	/**
	 * 更新codeChecked字段
	 * @param systemBranch
	 * @return
	 */
	@GetMapping(value="updateCodeChecked")
	@ResponseBody
	public Result updateCodeChecked(@RequestParam String system, @RequestParam String branch, @RequestParam String codeChecked){
		logger.info("开始更新codeChecked");
		SystemBranch systemBranch = systemBranchService.findBySystemAndBranch(system, branch);
		
		if("true".equals(codeChecked)){
			systemBranch.setCodeChecked(true);
		}else{
			systemBranch.setCodeChecked(false);
		}
		
		systemBranchService.update(systemBranch);
		return ResultUtil.success();
	}
	
	/**
	 * 查找所有
	 * @return
	 */
	@GetMapping(value="getAll")
	@ResponseBody
	public Result getAll(){
		logger.info("开始获取系统分支配置");
		return ResultUtil.success(systemBranchService.findall());
	}
	
	/**
	 * 根据系统查找，系统为空则返回所有
	 * @param system
	 * @return
	 */
	@GetMapping(value="getBySystem")
	@ResponseBody
	public Result getBySystem(@RequestParam String system){
		logger.info("开始获取系统分支配置");
		
		if(null == system || "null".equals(system) || "".equals(system)){
			return ResultUtil.success(systemBranchService.findall());
		}
		
		return ResultUtil.success(systemBranchService.findBySystem(system));
	}
	
	@GetMapping(value="noticeSyncResult")
	@ResponseBody
	public Result noticeSyncResult(@RequestParam Integer result, @RequestParam String system, @RequestParam String branch, @RequestParam String version ){
		SystemBranch systemBranch = systemBranchService.findBySystemAndBranch(system, branch);
		systemBranch.setVersion(version);
		
		if(result.equals(1)){//同步成功
			systemBranch.setOptstatus((short)3);
		}
		if(result.equals(1)){//同步失败
			systemBranch.setOptstatus((short)4);
		}
		
		systemBranchService.update(systemBranch);
		return ResultUtil.success();
	}

}
