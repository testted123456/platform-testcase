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
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.TestCaseInterfaceService;
import com.nonobank.testcase.service.TestCaseService;

@Controller
@RequestMapping(value="testCaseInterface")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestCaseInterfaceController {
	
	public static Logger logger = LoggerFactory.getLogger(TestCaseInterfaceController.class);
	
	@Autowired
	TestCaseInterfaceService testCaseInterfaceService;
	
	@Autowired
	TestCaseService testCaseService;
	
	@GetMapping(value="getById")
	@ResponseBody
	public Result getById(@RequestParam Integer id){
		logger.info("开始查询用例接口{}", id);
		
		TestCaseInterface testCaseInterface = testCaseInterfaceService.findById(id);
		return ResultUtil.success(testCaseInterface);
	}
	
	@GetMapping(value="getByTestCaseId")
	@ResponseBody
	public Result getByTestCaseId(@RequestParam Integer testCaseId){
		logger.info("开始查询用例接口");
		
		List<TestCaseInterface> testCaseInterfaces = testCaseInterfaceService.findByTestCaseId(testCaseId);
		return ResultUtil.success(testCaseInterfaces);
	}
	
	@PostMapping(value="addCaseInterface")
	@ResponseBody
	public Result addCaseInterface(@RequestBody TestCaseInterface testCaseInterface){
		logger.info("开始新增用例接口");
		
		testCaseInterfaceService.add(testCaseInterface);	
		return ResultUtil.success(testCaseInterface);
	}
	
	@PostMapping(value="addCaseInterfaces")
	@ResponseBody
	public Result add(@RequestBody List<TestCaseInterface> testCaseInterfaces){
		logger.info("开始新增用例接口");
		
		testCaseInterfaceService.add(testCaseInterfaces);	
		return ResultUtil.success(testCaseInterfaces);
	}
	
}
