package com.nonobank.testcase.controller;

import java.util.List;
import java.util.Optional;
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
import com.nonobank.testcase.component.MessageEntity;
import com.nonobank.testcase.component.executor.TestCaseExecutor;
import com.nonobank.testcase.component.validation.ErrorCode;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.exception.EntityException;
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
	public MessageEntity addCase(@RequestBody TestCase testCase){
		logger.info("开始新增用例");
		
		Optional<TestCase> optTestCase = Optional.ofNullable(testCase);
		MessageEntity msg = testCaseService.add(optTestCase, true);
		return msg;
	}
	
	@PostMapping(value="addCaseDir")
	@ResponseBody
	public MessageEntity addCaseDir(@RequestBody TestCase testCase){
		logger.info("开始新增用例目录");
		
		Optional<TestCase> optTestCase = Optional.ofNullable(testCase);
		MessageEntity msg = testCaseService.add(optTestCase, false);
		return msg;
	}
	
	@GetMapping(value="getCaseTreeByPId")
	@ResponseBody
	public MessageEntity getCaseTreeByPId(@RequestParam Integer pId){
		logger.info("开始查找用例树");
		MessageEntity msg = new MessageEntity();
		
		try{
			List<TestCase> cases = testCaseService.findByPId(pId);
			logger.info("查找用例树成功");
			msg.setSucceed(true);
			msg.setErrorCode(ErrorCode.correct);
			msg.setErrorMessage("查找用例树成功");
			msg.setData(cases);
		}catch(Exception e){
			logger.info("查找用例树失败");
			e.printStackTrace();
			msg.setSucceed(false);
			msg.setErrorCode(ErrorCode.exception_occurred_error);
			msg.setErrorMessage("查找用例树失败");
		}
		
		return msg;
	}
	
	@GetMapping(value="getCase")
	@ResponseBody
	public MessageEntity getCase(@RequestParam Integer id){
		logger.info("开始查找用例");
		MessageEntity msg = new MessageEntity();
		
		try{
			TestCase testCase = testCaseService.findById(id);
			logger.info("查找用例成功");
			msg.setSucceed(true);
			msg.setErrorMessage("查找用例成功");
			msg.setData(testCase);
		}catch(Exception e){
			logger.info("查找用例失败");
			e.printStackTrace();
			msg.setSucceed(true);
			msg.setErrorCode(ErrorCode.exception_occurred_error);
			msg.setErrorMessage("查找用例失败");
		}
		
		return msg;
	}
	
	@PostMapping(value="updateCase")
	@ResponseBody
	public MessageEntity updateCase(@RequestBody TestCase testCase){
		logger.info("开始更新用例");
		MessageEntity msg = testCaseService.update(Optional.ofNullable(testCase));
		return msg;
	}
	
	@PostMapping(value="deleteCase")
	@ResponseBody
	public MessageEntity deleteCase(@RequestBody TestCase testCase){
		logger.info("开始删除用例");
		MessageEntity msg = testCaseService.delete(Optional.ofNullable(testCase));
		return msg;
	}
	
	@GetMapping(value="execute")
	@ResponseBody
	public MessageEntity executeCase(@CookieValue(value="JSESSIONID", required=false) String sessionId,  @RequestParam Integer id){
		MessageEntity msg = new MessageEntity();
		logger.info("开始执行用例,id:" + id);
		
		TestCase testCase = testCaseService.findById(id);
		String env = testCase.getEnv();
		List<TestCaseInterface> testCaseInterfaces = testCaseInterfaceService.findByTestCaseId(id);
		
		boolean isStartSucceed = false;
	    testCaseExecutor.runCase(sessionId, env, testCaseInterfaces);
		
		if(isStartSucceed == true){
			msg.setSucceed(true);
			msg.setErrorCode(ErrorCode.correct);
			msg.setErrorMessage("开始执行用例成功，id:" + id);
		}else{
			msg.setSucceed(false);
			msg.setErrorCode(ErrorCode.common_error);
			msg.setErrorMessage("开始执行用例失败，id:" + id); 
		}
		
		return msg;
	}
}
