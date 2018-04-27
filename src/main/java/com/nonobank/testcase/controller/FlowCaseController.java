package com.nonobank.testcase.controller;

import java.time.LocalDateTime;
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
import com.nonobank.testcase.component.executor.FlowCaseExecutor;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.FlowCase;
import com.nonobank.testcase.service.FlowCaseService;
import com.nonobank.testcase.utils.UserUtil;

@Controller
@RequestMapping(value="flowCase")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FlowCaseController {

	public static Logger logger = LoggerFactory.getLogger(FlowCaseController.class);
	
	@Autowired
	FlowCaseService flowCaseService;
	
	@Autowired
	FlowCaseExecutor flowCaseExecutor;
	
	@PostMapping(value="addOrUpdate")
	@ResponseBody
	public Result addOrUpdate(@RequestBody FlowCase flowCase){
		logger.info("开始新增流用例：{}", flowCase.getName());
		
		String userName = UserUtil.getUser();
		
		if(flowCase.getId() != null){
			flowCase.setUpdatedBy(userName);
			flowCase.setUpdatedTime(LocalDateTime.now());
			flowCase.setOptstatus((short)0);
		}else{
			flowCase.setCreatedBy(userName);
			flowCase.setCreatedTime(LocalDateTime.now());
			flowCase.setOptstatus((short)0);
		}
		
		flowCase = flowCaseService.add(flowCase);
		logger.info("新增流用例成功，{}", flowCase.getName());
		return ResultUtil.success(flowCase);
	}
	
	
	@PostMapping(value="update")
	@ResponseBody
	public Result update(@RequestBody FlowCase flowCase){
		logger.info("开始更新流用例：{}", flowCase.getName());
		
		String userName = UserUtil.getUser();
		
		if(flowCase.getId() == null){
			return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "流用例id不存在！");
		}
		
		flowCase.setUpdatedBy(userName);
		flowCase.setUpdatedTime(LocalDateTime.now());
		flowCase.setOptstatus((short)0);
		
		flowCase = flowCaseService.add(flowCase);
		logger.info("更新流用例成功，{}", flowCase.getName());
		return ResultUtil.success(flowCase);
	}
	
	@GetMapping(value="delete")
	@ResponseBody
	public Result delte(@RequestParam Integer id){
		logger.info("开始删除流用例,id:：{}", id);
		
		String userName = UserUtil.getUser();
		
		FlowCase flowCase = flowCaseService.getById(id);
		
		if(flowCase == null){
			return ResultUtil.success();
		}
		
		flowCase.setUpdatedBy(userName);
		flowCase.setUpdatedTime(LocalDateTime.now());
		flowCase = flowCaseService.delete(flowCase);
		return ResultUtil.success();
	}
	
	@GetMapping(value="getById")
	@ResponseBody
	public Result getById(Integer id){
		logger.info("开始查找流用例，ID：{}", id);
		
		FlowCase flowCase = flowCaseService.getById(id);
		return ResultUtil.success(flowCase);
	}
	
	@GetMapping(value="getTreeByPId")
	@ResponseBody
	public Result getTreeByPId(Integer pId) {
		logger.info("开始查找流用例树，PID：{}", pId);
		try{
			List<FlowCase> flowCases = flowCaseService.getTreeByPId(pId);
			return ResultUtil.success(flowCases);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ResultUtil.success();
	}
	
	@PostMapping(value="execute")
	@ResponseBody
	public Result execute(@RequestBody FlowCase flowCase){
		logger.info("开始执行流用例，id：{}", flowCase.getId());
		String user = UserUtil.getUser();
		flowCaseExecutor.runFlowCase(user, flowCase);
//		flowCaseExecutor.runFlowCase(user, flowCase.getId(), flowCase.getEnv(), flowCase.getTestCases().size(), flowCase.getTestCases());
		return ResultUtil.success();
	}
}
