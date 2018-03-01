package com.nonobank.testcase.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nonobank.testcase.component.executor.TestCaseExecutor;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.TestCaseInterfaceService;
import com.nonobank.testcase.service.TestCaseService;

@Controller
@RequestMapping(value="testCase")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestCaseController {

	public static Logger logger = LoggerFactory.getLogger(TestCaseController.class);
	
	@Autowired
	TestCaseService testCaseService;
	
	@Autowired
	TestCaseInterfaceService testCaseInterfaceService;
	
	@Autowired
	TestCaseExecutor testCaseExecutor;
	
	@PostMapping(value="addCase")
	@ResponseBody
	public Result addCase(@RequestBody TestCase testCase){
		logger.info("开始新增用例");
		
		testCaseService.add(testCase, true);
		return ResultUtil.success(testCase);
	}
	
	@PostMapping(value="addCaseDir")
	@ResponseBody
	public Result addCaseDir(@RequestBody TestCase testCase){
		logger.info("开始新增用例目录");
		
		testCaseService.add(testCase, false);
		return ResultUtil.success(testCase);
	}
	
	@GetMapping(value="getCaseTreeByPId")
	@ResponseBody
	public Result getCaseTreeByPId(@RequestParam Integer pId){
		logger.info("开始查找用例树");
		
		List<TestCase> cases = testCaseService.findByPId(pId);
		logger.info("查找用例树成功");
		return ResultUtil.success(cases);
		
	}
	
	@GetMapping(value="getCase")
	@ResponseBody
	public Result getCase(@RequestParam Integer id){
		logger.info("开始查找用例");
		
		TestCase testCase = testCaseService.findById(id);
		logger.info("查找用例成功");
		return ResultUtil.success(testCase);
	}
	
	@PostMapping(value="updateCase")
	@ResponseBody
	public Result updateCase(@RequestBody TestCase testCase){
		logger.info("开始更新用例");
		testCaseService.update(testCase);
		return ResultUtil.success(testCase);
	}
	
	@PostMapping(value="deleteCase")
	@ResponseBody
	public Result deleteCase(@RequestBody TestCase testCase){
		logger.info("开始删除用例");
		testCaseService.delete(testCase);
		return ResultUtil.success(testCase);
	}
	
	@GetMapping(value="execute")
	@ResponseBody
	public Result executeCase(@CookieValue(value="JSESSIONID", required=false) String sessionId,  @RequestParam Integer id){
		logger.info("开始执行用例,id:{}", id);
		
		TestCase testCase = testCaseService.findById(id);
		String env = testCase.getEnv();
		List<TestCaseInterface> testCaseInterfaces = testCaseInterfaceService.findByTestCaseId(id);
	    testCaseExecutor.runCase(sessionId, env, testCaseInterfaces);
	    
	    return ResultUtil.success(testCase);
	}
}
