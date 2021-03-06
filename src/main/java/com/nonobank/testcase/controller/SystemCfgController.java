package com.nonobank.testcase.controller;

import java.util.List;
import java.util.Map;

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
import com.nonobank.testcase.entity.SystemCfg;
import com.nonobank.testcase.service.SystemCfgService;

@Controller
@RequestMapping(value="sysCfg")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SystemCfgController {

	public static Logger logger = LoggerFactory.getLogger(SystemCfgController.class);
	
	@Autowired
	SystemCfgService systemCfgService;
	
	@PostMapping(value="add")
	@ResponseBody
	public Result add(@RequestBody SystemCfg systemCfg){
		logger.info("开始添加系统{}", systemCfg.getSystem());
		systemCfgService.add(systemCfg);
		logger.info("添加系统{}成功", systemCfg.getSystem());
		return ResultUtil.success(systemCfg);
	}
	
	@PostMapping(value="update")
	@ResponseBody
	public Result update(@RequestBody SystemCfg systemCfg){
		logger.info("开始更新系统{}", systemCfg.getSystem());
		systemCfgService.update(systemCfg);
		logger.info("更新系统{}成功", systemCfg.getSystem());
		return ResultUtil.success(systemCfg);
	}
	
	@PostMapping(value="delete")
	@ResponseBody
	public Result delete(@RequestBody SystemCfg systemCfg){
		logger.info("开始删除系统{}", systemCfg.getSystem());
		systemCfgService.delete(systemCfg);
		logger.info("更新删除{}成功", systemCfg.getSystem());
		return ResultUtil.success();
	}
	
	@GetMapping(value="getAllAlias")
	@ResponseBody
	public Result getAllAlias(){
		logger.info("开始查找所有系统别名");
		List<String> aliases = systemCfgService.findAllAlias();
		logger.info("查找所有系统别名成功");
		return ResultUtil.success(aliases);
	}
	
	@GetMapping(value="getAll")
	@ResponseBody
	public Result getAll(){
		logger.info("开始查找所有系统配置");
		List<SystemCfg> systemCfgs = systemCfgService.findAll();
		logger.info("查找所有系统配置");
		return ResultUtil.success(systemCfgs);
	}
	
	@PostMapping(value="getPage")
	@ResponseBody
	public Result getPage(@RequestParam int pageIndex, @RequestParam int pageSize){
		logger.info("开始分页查询...");
		Map<String, Object> map = systemCfgService.findPage(pageIndex, pageSize);
		return ResultUtil.success(map);
	}
	
	@GetMapping(value="getBySystem")
	@ResponseBody
	public Result getBySystem(@RequestParam String system){
		logger.info("开始查找系统配置");
		SystemCfg systemCfgs = systemCfgService.findBySystem(system);
		return ResultUtil.success(systemCfgs);
	}
	
}
