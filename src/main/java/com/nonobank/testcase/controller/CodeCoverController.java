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
import org.springframework.web.bind.annotation.ResponseBody;

import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.CodeCover;
import com.nonobank.testcase.service.CodeCoverService;

@RequestMapping(value="codeCover")
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class CodeCoverController {

	public static Logger logger = LoggerFactory.getLogger(CodeCoverController.class);
	
	@Autowired
	CodeCoverService codeCoverService;

	@PostMapping(value="add")
	@ResponseBody
	public Result add(@RequestBody CodeCover codeCover){
		logger.info("开始新曾codeCover");
		codeCover = codeCoverService.add(codeCover);
		return ResultUtil.success(codeCover);
	}
	
	@PostMapping(value="delete")
	@ResponseBody
	public Result delete(@RequestBody CodeCover codeCover){
		logger.info("开始删除codeCover");
		codeCoverService.delete(codeCover);
		return ResultUtil.success();
	}
	
	@GetMapping(value="getAll")
	@ResponseBody
	public Result getAll(){
		logger.info("开始获取所有codeCover");
		return ResultUtil.success(codeCoverService.getAll());
	}
}
