package com.nonobank.testcase.controller;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.nonobank.testcase.utils.JSONUtils;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.executor.TestCaseExecutor;
import com.nonobank.testcase.component.remoteEntity.RemoteApi;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.component.security.manager.MyAccessDecisionManager;
import com.nonobank.testcase.component.ws.WebSocket;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.entity.SystemBranch;
import com.nonobank.testcase.entity.SystemCfg;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseFront;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.service.GlobalVariableService;
import com.nonobank.testcase.service.SystemBranchService;
import com.nonobank.testcase.service.SystemCfgService;
import com.nonobank.testcase.service.TestCaseInterfaceService;
import com.nonobank.testcase.service.TestCaseRunService;
import com.nonobank.testcase.service.TestCaseService;
import com.nonobank.testcase.utils.UserUtil;

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
	SystemCfgService systemCfgService;
	
	@Autowired
	WebSocket webSocket;
	
	@Autowired
	RemoteApi remoteApi;
	
	@Autowired
	SystemBranchService systemBranchService;
	
	@Autowired
	MyAccessDecisionManager myAccessDecisionManager;
	
	@Autowired
	TestCaseRunService testCaseRunService;
	
	@Autowired
	GlobalVariableService globalVariableService;
	
	/**
	 * reload url权限
	 * @return
	 */
	@GetMapping(value="initUrlRole")
	@ResponseBody
	public Result initUrlRole(){
		myAccessDecisionManager.initUrlMap();
		return ResultUtil.success();
	}
	
	@PostMapping(value="addCase")
	@ResponseBody
	public Result addCase(@RequestBody TestCaseFront testCaseFront){
		logger.info("开始新增用例");
		
		String userName = UserUtil.getUser();
		
		TestCase testCase = testCaseService.add(userName, testCaseFront, true);
		
		if(null != testCase){
			testCaseFront = testCase.convert();
		}
		return ResultUtil.success(testCaseFront);
	}
	
	@PostMapping(value="addCaseDir")
	@ResponseBody
	public Result addCaseDir(@RequestBody TestCaseFront testCaseFront){
		logger.info("开始新增用例目录");
		
		String userName = UserUtil.getUser();
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
	public Result updateCase(@RequestBody TestCaseFront testCaseFront){
		logger.info("开始更新用例");
		
		String userName = UserUtil.getUser();
		
		String createdBy = testCaseFront.getCreatedBy();
		
		if(null != createdBy && !createdBy.equals(userName)){
			return ResultUtil.error(ResultCode.VALIDATION_ERROR.getCode(), "用例只能由创建人修改，创建人：{}" + createdBy);
		}
		TestCase testCase = testCaseService.update(userName, testCaseFront);
		testCaseFront = testCase.convert();
		return ResultUtil.success(testCaseFront);
	}
	
	@GetMapping(value="deleteCase")
	@ResponseBody
	public Result deleteCase(Integer id){
		logger.info("开始删除用例");
		String userName = UserUtil.getUser();
		testCaseService.deleteTestCase(userName, id);
		return ResultUtil.success();
	}
	
	@GetMapping(value="deleteTestCaseDir")
	@ResponseBody
	public Result deleteTestCaseDir(Integer id){
		logger.info("开始删除用例");
		String userName = UserUtil.getUser();
		testCaseService.deleteTestCaseDir(userName, id);
		return ResultUtil.success();
	}
	
	@GetMapping(value="checkCase")
	@ResponseBody
	public Result checkCase(@RequestParam Integer testCaseId){
		logger.info("开始检查用例");
		TestCase tc = testCaseService.findById(testCaseId);
		List<TestCaseInterface> tcis = tc.getTestCaseInterfaces();
//				testCaseInterfaceService.findByTestCaseId(testCaseId);
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
			SystemCfg systemCfg = systemCfgService.findBySystemOrAlias(system, system);
			List<SystemBranch> systemBranches = systemBranchService.findBySystemAndLast(systemCfg.getSystem(), true);
			long count = systemBranches.stream().filter(y->{return y.getSystem().equals(systemCfg.getSystem()) && y.getBranch().equals(branch); }).count();
		    
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
	public Result executeCase(@RequestParam Integer id){
		logger.info("开始执行用例,id:{}", id);
		
		String user = UserUtil.getUser();
		
		String sessionId = id + user + "1";
		
		TestCase testCase = testCaseService.findById(id);
		
		webSocket.sendMsgTo("### " + "开始执行用例：" + testCase.getName(), "123");
		
	    int totalSize = testCase.getTestCaseInterfaces().size();
		
		String env = testCase.getEnv();
		List<TestCaseInterface> testCaseInterfaces = testCaseInterfaceService.findByTestCaseId(id);
	    testCaseExecutor.asyncRunCase(sessionId, env, id, testCaseInterfaces, totalSize);
	    return ResultUtil.success(testCase);
	}
	
	@PostMapping(value="executeApis")
	@ResponseBody
	public Result executeApis(@RequestBody JSONObject jsonObj){
		Integer tcId = jsonObj.getInteger("tcId");
		JSONArray jsonArrOfApiIds = jsonObj.getJSONArray("apiIds");
		List<Integer> tciIds = jsonArrOfApiIds.stream().map(x->{return (Integer)x;}).collect(Collectors.toList());
		String user = UserUtil.getUser();
		
		//sessionId用于和websocket通信识别
		String sessionId = tcId + user + "1";
		
		//初始化上下文
		Map<String, Object> map = globalVariableService.getAllVars();
		
		ResultHistory resultHistory = new ResultHistory();
		resultHistory.setTcType('0');
		resultHistory.setTcId(tcId);
		resultHistory.setApiIds(tciIds.toString());
		resultHistory.setCreatedTime(LocalDateTime.now());
		//异步执行case
		testCaseRunService.asyncRunCase(resultHistory, tcId, tciIds, map, sessionId);
		return ResultUtil.success();
	}
	
	/*@PostMapping(value="executeApis")
	@ResponseBody
	public Result executeApis( @RequestBody JSONObject jsonObj){
		Integer tcId = jsonObj.getInteger("tcId");
		JSONArray jsonArrOfApiIds = jsonObj.getJSONArray("apiIds");
		
		String user = UserUtil.getUser();
		
		String sessionId = tcId + user + "1";
		
		TestCase testCase = testCaseService.findById(tcId);
		webSocket.sendMsgTo("### " + "开始执行用例：" + testCase.getName(), sessionId);
		String env = testCase.getEnv();
		
		List<Integer> listOfApiIds = jsonArrOfApiIds.stream().map(x->{
			return Integer.valueOf(x.toString());
		}).collect(Collectors.toList());
		
		List<TestCaseInterface> tcis = new ArrayList<TestCaseInterface>();
		
		for(Integer apiId : listOfApiIds){
			TestCaseInterface tci = testCaseInterfaceService.findById(apiId);
			tcis.add(tci);
		}
		
		int totalsize = testCase.getTestCaseInterfaces().size();
		
		testCaseExecutor.asyncRunCase(sessionId, env, tcId, tcis, totalsize);
		return ResultUtil.success(testCase);
	}*/
	
	@GetMapping(value="searchCase")
	@ResponseBody
	public Result searchCase(@RequestParam String name, @RequestParam String createdBy,  @RequestParam String apiName,  @RequestParam String urlAddress){
		logger.info("开始搜索用例,名称：{},创建人：{}",name, createdBy);
		List<JSONObject> testCases = testCaseService.searchCases(name, createdBy, apiName, urlAddress);
		return ResultUtil.success(testCases);
	}


	@PostMapping(value="clearJsonValue")
	@ResponseBody
	public Result clearJsonValue( @RequestBody JSONObject jsonObj){
		jsonObj = JSON.parseObject(jsonObj.toJSONString());
		JSONUtils.clearJsonValue(jsonObj);
		return ResultUtil.success(jsonObj);

	}

	@PostMapping(value="setJsonValue")
	@ResponseBody
	public Result setJsonValue( @RequestBody JSONObject jsonObj){
		Set<String> keys = JSONUtils.setJsonValue(jsonObj);
		Map<String,Object> remap =new HashMap<>();
		remap.put("json",jsonObj);
		remap.put("variables",keys==null?new HashSet<>():keys);
		return ResultUtil.success(remap);

	}


	
}
