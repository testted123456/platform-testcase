package com.nonobank.testcase.controller;

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
import com.nonobank.testcase.entity.GlobalVariable;
import com.nonobank.testcase.service.GlobalVariableService;

@Controller
@RequestMapping(value="gVar")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GlobalVariableController {
	
	public static Logger logger = LoggerFactory.getLogger(GlobalVariableController.class);
	
	@Autowired
	GlobalVariableService globalVariableService;
	
	@PostMapping(value="addOrUpdate")
	@ResponseBody
	public Result addOrUpdate(@RequestBody GlobalVariable globalVariable){
		logger.info("开始新增或更新变量，{}", globalVariable.getName());
		globalVariable = globalVariableService.addOrUpdate(globalVariable);
		return ResultUtil.success(globalVariable);
	}
	
	@GetMapping(value="getAll")
	@ResponseBody
	public Result getAll(){
		logger.info("开始查找所有变量");
		return ResultUtil.success(globalVariableService.getAll());
	}
	
	@GetMapping(value="getPage")
	@ResponseBody
	public Result getPage(@RequestParam int pageIndex, @RequestParam int pageSize){
		logger.info("开始分页查找全局变量...");
		return ResultUtil.success(globalVariableService.getPage(pageIndex, pageSize));
	}
	
	@PostMapping(value="delete")
	@ResponseBody
	public Result delete(@RequestBody GlobalVariable globalVariable){
		logger.info("开始删除变量", globalVariable.getName());
		globalVariableService.delete(globalVariable);
		return ResultUtil.success();
	}

}
