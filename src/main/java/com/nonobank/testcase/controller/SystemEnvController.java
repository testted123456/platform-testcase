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
import com.nonobank.testcase.entity.SystemEnv;
import com.nonobank.testcase.service.SystemEnvService;

@Controller
@RequestMapping(value="sysEnv")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SystemEnvController {
	
	public static Logger logger = LoggerFactory.getLogger(SystemEnvController.class);
	
	@Autowired
	SystemEnvService systemEnvService;
	
	@PostMapping(value="add")
	@ResponseBody
	public Result add(@RequestBody SystemEnv systemEnv){
		logger.info("开始配置系统{}，环境{}", systemEnv.getSystemCfg().getAlias(), systemEnv.getEnv().getName());
		systemEnvService.add(systemEnv);
		logger.info("开始配置系统{}，环境{}", systemEnv.getSystemCfg().getAlias(), systemEnv.getEnv().getName());
		return ResultUtil.success(systemEnv);
	}
	
	@PostMapping(value="update")
	@ResponseBody
	public Result update(@RequestBody SystemEnv systemEnv){
		logger.info("开始更新配置系统{}，环境{}", systemEnv.getSystemCfg().getAlias(), systemEnv.getEnv().getName());
		systemEnvService.update(systemEnv);
		logger.info("开始更新配置系统{}，环境{}", systemEnv.getSystemCfg().getAlias(), systemEnv.getEnv().getName());
		return ResultUtil.success(systemEnv);
	}
	
	@PostMapping(value="del")
	@ResponseBody
	public Result del(@RequestParam Integer id){
		
		logger.info("开始配置系统环境配置，ID：{}", id);
		systemEnvService.delete(id);
		logger.info("开始配置系统环境配置成功，ID：{}", id);
		return ResultUtil.success();
	}
	
	@GetMapping(value="getAll")
	@ResponseBody
	public Result getAll(){
		logger.info("开始查找所有系统配置");
		List<SystemEnv> systemCfgs = systemEnvService.findAll();
		logger.info("查找所有系统配置");
		return ResultUtil.success(systemCfgs);
	}

}
