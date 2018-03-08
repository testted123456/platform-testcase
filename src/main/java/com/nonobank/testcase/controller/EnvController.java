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
import org.springframework.web.bind.annotation.ResponseBody;

import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.Env;
import com.nonobank.testcase.service.EnvService;

@Controller
@RequestMapping(value="env")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EnvController {

	public static Logger logger = LoggerFactory.getLogger(EnvController.class);
	
	@Autowired
	EnvService envService;
	
	@PostMapping(value="add")
	@ResponseBody
	public Result add(@RequestBody Env env){
		logger.info("开始添加环境{}", env.getName());
		envService.add(env);
		logger.info("添加环境{}成功", env.getName());
		return ResultUtil.success(env);
	}
	
	@PostMapping(value="update")
	@ResponseBody
	public Result update(@RequestBody Env env){
		logger.info("开始修改环境{}", env.getName());
		envService.update(env);
		logger.info("修改环境{}成功", env.getName());
		return ResultUtil.success(env);
	}
	
	@PostMapping(value="delete")
	@ResponseBody
	public Result delete(@RequestBody Env env){
		logger.info("开始删除环境{}", env.getName());
		envService.delete(env);
		logger.info("删除环境{}成功", env.getName());
		return ResultUtil.success(env);
	}
	
	@GetMapping(value="getAllEnvs")
	@ResponseBody
	public Result getAllEnvs(){
		logger.info("开始查询所有环境");
		List<Env> envs = envService.findAll();
		return ResultUtil.success(envs);
	}

}
