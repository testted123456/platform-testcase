package com.nonobank.testcase.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.executor.TestCaseExecutor;
import com.nonobank.testcase.component.remoteEntity.RemoteApi;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.entity.SystemBranch;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseFront;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.SystemBranchService;
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
	
	@Autowired
	WebSocket webSocket;
	
	@Autowired
	RemoteApi remoteApi;
	
	@Autowired
	SystemBranchService systemBranchService;
	
	@PostMapping(value="addCase")
	@ResponseBody
	public Result addCase(@CookieValue(value="nonousername",required=false) String userName, @RequestBody TestCaseFront testCaseFront){
		logger.info("开始新增用例");
		
		TestCase testCase = testCaseService.add(userName, testCaseFront, true);
		
		if(null != testCase){
			testCaseFront = testCase.convert();
		}
		return ResultUtil.success(testCaseFront);
	}
	
	@PostMapping(value="addCaseDir")
	@ResponseBody
	public Result addCaseDir(@CookieValue(value="nonousername",required=false) String userName, @RequestBody TestCaseFront testCaseFront){
		logger.info("开始新增用例目录");
		
		TestCase testCase = testCaseService.add(userName, testCaseFront, false);
		
		if(null != testCase){
			testCaseFront = testCase.convert();
		}
		
		return ResultUtil.success(testCaseFront);
	}
	
	@GetMapping(value="getCaseTreeByPId")
	@ResponseBody
	public Result getCaseTreeByPId(@RequestParam Integer pId){
		logger.info("开始查找用例树");
		
		List<TestCase> cases = testCaseService.findByPId(pId);
		logger.info("查找用例树成功");
		return ResultUtil.success(cases);
		
	}
	
	@GetMapping(value="getCaseById")
	@ResponseBody
	public Result getCaseById(@RequestParam Integer id){
		logger.info("开始查找用例");
		
		TestCase testCase = testCaseService.findById(id);
		TestCaseFront tcf = testCase.convert();
		logger.info("查找用例成功");
		return ResultUtil.success(tcf);
	}
	
	@PostMapping(value="updateCase")
	@ResponseBody
	public Result updateCase(@CookieValue(value="nonousername",required=false) String userName, @RequestBody TestCaseFront testCaseFront){
		logger.info("开始更新用例");
		
		TestCase testCase = testCaseService.update(userName, testCaseFront);
		testCaseFront = testCase.convert();
		return ResultUtil.success(testCaseFront);
	}
	
	@GetMapping(value="deleteCase")
	@ResponseBody
	public Result deleteCase(@CookieValue(value="nonousername",required=false) String userName, Integer id){
		logger.info("开始删除用例");
		testCaseService.deleteTestCase(userName, id);
		return ResultUtil.success();
	}
	
	@GetMapping(value="deleteTestCaseDir")
	@ResponseBody
	public Result deleteTestCaseDir(@CookieValue(value="nonousername",required=false) String userName, Integer id){
		logger.info("开始删除用例");
		testCaseService.deleteTestCaseDir(userName, id);
		return ResultUtil.success();
	}
	
	@GetMapping(value="checkCase")
	@ResponseBody
	public Result checkCase(@RequestParam Integer testCaseId){
		logger.info("开始检查用例");
		TestCase tc = testCaseService.findById(testCaseId);
		List<TestCaseInterface> tcis = testCaseInterfaceService.findByTestCaseId(testCaseId);
		JSONArray jsonArr = new JSONArray();
		
		tcis.forEach(x->{
			JSONObject jsonObj = new JSONObject();
			Integer apiId = x.getInterfaceId();
			Integer id = x.getId();
			
			//当前接口
			JSONObject jsonApi = remoteApi.getApi(apiId);
			String system = jsonApi.getString("system");
			String branch = jsonApi.getString("branch");
			String name = jsonApi.getString("name");
			List<SystemBranch> systemBranches = systemBranchService.findBySystemAndLast(system, true);
			long count = systemBranches.stream().filter(y->{return y.getSystem().equals(system) && y.getBranch().equals(branch); }).count();
		    
			jsonObj.put("name", name);
			jsonObj.put("currentBranch", branch);
			jsonObj.put("id", id);
			jsonObj.put("apiId", apiId);
			
			List<String> lastBranches = new ArrayList<String>();
			
			if(count == 0){
				systemBranches.forEach(s->lastBranches.add(s.getBranch()));
			}else{
				lastBranches.add("已是最新分支");
			}
			jsonObj.put("lastBranch", systemBranches);
			
			jsonArr.add(jsonObj);
		});
		
		return ResultUtil.success(jsonArr);
	}
	
	@GetMapping(value="execute")
	@ResponseBody
	public Result executeCase(@CookieValue(value="JSESSIONID", required=false) String sessionId,  @RequestParam Integer id){
		logger.info("开始执行用例,id:{}", id);
		
		TestCase testCase = testCaseService.findById(id);
		
		webSocket.sendMsgTo("### " + "开始执行用例：" + testCase.getName(), "123");
		
		String env = testCase.getEnv();
		List<TestCaseInterface> testCaseInterfaces = testCaseInterfaceService.findByTestCaseId(id);
	    testCaseExecutor.asyncRunCase(sessionId, env, testCaseInterfaces);
	    return ResultUtil.success(testCase);
	}
	
	@PostMapping(value="executeApis")
	@ResponseBody
	public Result executeApis(@CookieValue(value="JSESSIONID", required=false) String sessionId, @RequestBody JSONObject jsonObj){
		Integer tcId = jsonObj.getInteger("tcId");
		JSONArray jsonArrOfApiIds = jsonObj.getJSONArray("apiIds");
		
		TestCase testCase = testCaseService.findById(tcId);
		webSocket.sendMsgTo("### " + "开始执行用例：" + testCase.getName(), "123");
		String env = testCase.getEnv();
		
		List<Integer> listOfApiIds = jsonArrOfApiIds.stream().map(x->{
			return Integer.valueOf(x.toString());
		}).collect(Collectors.toList());
		
		List<TestCaseInterface> tcis = new ArrayList<TestCaseInterface>();
		
		for(Integer apiId : listOfApiIds){
			TestCaseInterface tci = testCaseInterfaceService.findById(apiId);
			tcis.add(tci);
		}
		
		
		testCaseExecutor.asyncRunCase(sessionId, env, tcis);
		return ResultUtil.success(testCase);
	}
}
