package com.nonobank.testcase.controller;

import java.util.List;
import java.util.Optional;
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
import com.nonobank.testcase.component.MessageEntity;
import com.nonobank.testcase.component.validation.ErrorCode;
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
	public MessageEntity getById(@RequestParam Integer id){
		logger.info("开始查询用例接口");
		
		MessageEntity msg = new  MessageEntity();
		
		try{
			TestCaseInterface testCaseInterface = testCaseInterfaceService.findById(id);
			logger.info("查询用例接口成功");
			msg.setSucceed(true);
			msg.setErrorCode(ErrorCode.correct);
			msg.setErrorMessage("查询用例接口成功");
			msg.setData(testCaseInterface);
		}catch(Exception e){
			logger.error("查询用例接口失败");
			e.printStackTrace();
			msg.setSucceed(true);
			msg.setErrorCode(ErrorCode.exception_occurred_error);
			msg.setErrorMessage("查询用例接口失败");
		}
		
		return msg;
	}
	
	@GetMapping(value="getByTestCaseId")
	@ResponseBody
	public MessageEntity getByTestCaseId(@RequestParam Integer testCaseId){
		logger.info("开始查询用例接口");
		
		MessageEntity msg = new  MessageEntity();
		
		try{
			List<TestCaseInterface> testCaseInterfaces = testCaseInterfaceService.findByTestCaseId(testCaseId);
			logger.info("查询用例接口成功");
			msg.setSucceed(true);
			msg.setErrorCode(ErrorCode.correct);
			msg.setErrorMessage("查询用例接口成功");
			msg.setData(testCaseInterfaces);
		}catch(Exception e){
			logger.error("查询用例接口失败");
			e.printStackTrace();
			msg.setSucceed(true);
			msg.setErrorCode(ErrorCode.exception_occurred_error);
			msg.setErrorMessage("查询用例接口失败");
		}
		
		return msg;
	}
	
	@PostMapping(value="addCaseInterface")
	@ResponseBody
	public MessageEntity addCaseInterface(@RequestBody TestCaseInterface testCaseInterface){
		logger.info("开始新增用例接口");
		
		MessageEntity msg = testCaseInterfaceService.add(Optional.ofNullable(testCaseInterface));	
		return msg;
	}
	
	@PostMapping(value="addCaseInterfaces")
	@ResponseBody
	public MessageEntity add(@RequestBody List<TestCaseInterface> testCaseInterfaces){
		logger.info("开始新增用例接口");
		MessageEntity msg = testCaseInterfaceService.add(testCaseInterfaces);	
		return msg;
	}
	
}
