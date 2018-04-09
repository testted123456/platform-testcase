package com.nonobank.testcase.controller;

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

import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.SystemBranch;
import com.nonobank.testcase.service.SystemBranchService;

@Controller
@RequestMapping(value="sysBranch")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SystemBranchController {
	
	public static Logger logger = LoggerFactory.getLogger(SystemBranchController.class);
	
	@Autowired
	SystemBranchService systemBranchService;
	
	@GetMapping(value="syncBranches")
	@ResponseBody
	public Result syncBranches(@RequestParam String system){
		logger.info("开始同步系统 {} 分支", system);
		
		List<SystemBranch> systemBranches = null;
		systemBranchService.add(system, systemBranches);
		return ResultUtil.success();
	}
	
	@PostMapping(value="updateBranch")
	@ResponseBody
	public Result updateBranch(@RequestBody SystemBranch systemBranch){
		logger.info("开始更新系统 {} ,分支 {}", systemBranch.getSystem(), systemBranch.getBranch());
		systemBranchService.update(systemBranch);
		return ResultUtil.success();
	}
	
	@GetMapping(value="getAll")
	@ResponseBody
	public Result getAll(){
		logger.info("开始获取系统分支配置");
		return ResultUtil.success(systemBranchService.findall());
	}

}
