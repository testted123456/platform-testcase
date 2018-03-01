package com.nonobank.testcase.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.SystemCfg;
import com.nonobank.testcase.service.SystemCfgService;

@Controller
@RequestMapping(value="sysCfg")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SystemCfgController {

	public static Logger logger = LoggerFactory.getLogger(SystemCfgController.class);
	
	SystemCfgService SystemCfgService;
	
	@PostMapping(value="add")
	public Result add(SystemCfg systemCfg){
		logger.info("开始添加系统{}", systemCfg.getSystem());
		SystemCfgService.add(systemCfg);
		logger.info("添加系统{}成功", systemCfg.getSystem());
		return ResultUtil.success(systemCfg);
	}
	
	@PostMapping(value="update")
	public Result update(SystemCfg systemCfg){
		logger.info("开始更新系统{}", systemCfg.getSystem());
		SystemCfgService.update(systemCfg);
		logger.info("更新系统{}成功", systemCfg.getSystem());
		return ResultUtil.success(systemCfg);
	}
	
	@PostMapping(value="delete")
	public Result delete(SystemCfg systemCfg){
		logger.info("开始删除系统{}", systemCfg.getSystem());
		SystemCfgService.update(systemCfg);
		logger.info("更新删除{}成功", systemCfg.getSystem());
		return ResultUtil.success();
	}
}
