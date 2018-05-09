package com.nonobank.testcase.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.nonobank.testcase.component.remoteEntity.RemoteApi;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultUtil;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.entity.TestCaseInterfaceFront;
import com.nonobank.testcase.service.SystemBranchService;
import com.nonobank.testcase.service.TestCaseInterfaceService;
import com.nonobank.testcase.service.TestCaseService;
import com.nonobank.testcase.utils.JSONUtils;
import com.nonobank.testcase.utils.UserUtil;

@Controller
@RequestMapping(value="testCaseInterface")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestCaseInterfaceController {
	
	public static Logger logger = LoggerFactory.getLogger(TestCaseInterfaceController.class);
	
	@Autowired
	TestCaseInterfaceService testCaseInterfaceService;
	
	@Autowired
	TestCaseService testCaseService;
	
	@Autowired
	RemoteApi remoteApi;
	
	@Autowired
	SystemBranchService systemBranchService;
	
	@GetMapping(value="getByTestCaseId")
	@ResponseBody
	public Result getByTestCaseId(@RequestParam Integer testCaseId){
		logger.info("开始查询用例接口");
		
		List<TestCaseInterface> tcis = testCaseInterfaceService.findByTestCaseId(testCaseId);
		
		List<TestCaseInterfaceFront> tcifs = new ArrayList<>();
		
		tcis.stream().forEach(x->{
			tcifs.add(x.convert());
		});
		/**
		List<Integer> ids = tcis.stream().map(x->x.getInterfaceId()).collect(Collectors.toList());
		JSONArray jsonApis = remoteApi.getApisById(ids);
		
		tcis.forEach(x->{
			TestCaseInterfaceFront tcif = x.convert();
			
			Optional<Object> obj = jsonApis.stream().filter(y->{
				JSONObject z = (JSONObject)y;
				Integer id = z.getInteger("id");
				return id.equals(x.getInterfaceId());
			}).findFirst();
			
			if(obj.get() instanceof JSONObject){
				JSONObject jsonObj = (JSONObject)obj.get();
				tcif.setName(jsonObj.getString("name"));
				tcif.setSystem(jsonObj.getString("system"));
				tcif.setBranch(jsonObj.getString("branch"));
			}
			
			tcifs.add(tcif);
		});**/
		
		return ResultUtil.success(tcifs);
	}
	
	
	@PostMapping(value="addCaseInterfaces")
	@ResponseBody
	public Result add(@RequestBody List<TestCaseInterfaceFront> tcifs){
		logger.info("开始新增用例接口");
		String userName = UserUtil.getUser();
		testCaseInterfaceService.add(userName, tcifs);
		return ResultUtil.success(tcifs);
	}
	
	@PostMapping(value="updateCaseInterfaces")
	@ResponseBody
	public Result update( @RequestBody List<TestCaseInterfaceFront> tcifs){
		logger.info("开始新增用例接口");
		String userName = UserUtil.getUser();
		List<TestCaseInterfaceFront> result = testCaseInterfaceService.update(userName, tcifs);
		return ResultUtil.success(result);
	}
	
	@GetMapping(value="checkApi")
	@ResponseBody
	public Result checkApi(Integer id, Integer apiId, String lastBranch){
		logger.info("开始检查用例接口");
		
		//当前接口
		JSONObject currentJsonApi = remoteApi.getApi(apiId);
		String system = currentJsonApi.getString("system");
		String module = currentJsonApi.getString("module");
		String urlAddress = currentJsonApi.getString("urlAddress");
		String currentRequestBody = currentJsonApi.getString("requestBody");
		String currentResponseBody = currentJsonApi.getString("responseBody");
		
		//最新分支接口
		JSONObject lastJsonApi = remoteApi.getLastApi(system, module, lastBranch, urlAddress);
		String lastRequestBody = lastJsonApi.getString("requestBody");
		String lastResponseBody = lastJsonApi.getString("responseBody");
		
		TestCaseInterface tci = testCaseInterfaceService.findById(id);
		String requestBody = tci.getRequestBody();
		String responseBody = tci.getResponseBody();
		
		Object compareRequest = null;
		Object compareResponse = null;
		Object comprareApiRequest = null;
		Object compareApiResponse = null;
		
		if(JSONUtils.isJsonArray(currentRequestBody) && JSONUtils.isJsonArray(lastRequestBody)){
			comprareApiRequest = JSONUtils.compareJsonArray(JSONArray.parseArray(currentRequestBody), JSONArray.parseArray(lastRequestBody));
			compareRequest = JSONUtils.compareJsonArray(JSONArray.parseArray(requestBody), JSONArray.parseArray(lastRequestBody));
		}else if(JSONUtils.isJsonObject(currentRequestBody) && JSONUtils.isJsonObject(currentRequestBody)){
			comprareApiRequest = JSONUtils.compareJsonObj(JSONObject.parseObject(currentRequestBody), JSONObject.parseObject(lastRequestBody));
			compareRequest = JSONUtils.compareJsonObj(JSONObject.parseObject(responseBody), JSONObject.parseObject(lastRequestBody));
		}
		
		if(JSONUtils.isJsonArray(currentResponseBody) && JSONUtils.isJsonArray(lastResponseBody)){
			compareApiResponse = JSONUtils.compareJsonArray(JSONArray.parseArray(currentResponseBody), JSONArray.parseArray(lastResponseBody));
			compareResponse = JSONUtils.compareJsonArray(JSONArray.parseArray(responseBody), JSONArray.parseArray(lastResponseBody));
		}else if(JSONUtils.isJsonObject(currentResponseBody) && JSONUtils.isJsonObject(lastResponseBody)){
			compareApiResponse = JSONUtils.compareJsonObj(JSONObject.parseObject(currentResponseBody), JSONObject.parseObject(lastResponseBody));
			compareResponse = JSONUtils.compareJsonObj(JSONObject.parseObject(responseBody), JSONObject.parseObject(lastResponseBody));
		}
		
		JSONObject resultJson = new JSONObject();
		resultJson.put("currentRequestBody", currentRequestBody);
		resultJson.put("currentResponseBody", currentResponseBody);
		resultJson.put("lastRequestBody", lastRequestBody);
		resultJson.put("lastResponseBody", lastResponseBody);
		resultJson.put("compareRequest", compareRequest);
		resultJson.put("compareResponse", compareResponse);
		resultJson.put("comprareApiRequest", comprareApiRequest);
		resultJson.put("compareApiResponse", compareApiResponse);
		resultJson.put("requestBody", requestBody);
		resultJson.put("responseBody", responseBody);
		
		return ResultUtil.success(resultJson);
	}
	
	/**
	 * 查找具有相同接口的用例，并以树展示
	 * @param apiId
	 * @return
	 */
	@GetMapping(value="getSameCasesByApiId")
	@ResponseBody
	public Result getSameCasesByApiId(@RequestParam Integer apiId){
		
		List<JSONObject> treeNodes = new ArrayList<JSONObject>();
		
		Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
		
		JSONObject rootNode = new JSONObject();
		
		List<TestCaseInterface> tcis = testCaseInterfaceService.findByInterfaceIdAndOptstatusEquals(apiId);
		
		tcis.forEach(x->{
			String name = x.getStep();
			Integer id = x.getId();
			TestCase tc = x.getTestCase();
			
			
			JSONObject leaf = new JSONObject();
			leaf.put("pId", tc.getId());
			leaf.put("id", id);
			leaf.put("name", name);
			leaf.put("type", 0);
			x.setId(null);
			leaf.put("api", x);
			
			name = tc.getName();
			id = tc.getId();
			Integer pId = tc.getpId();
			
			JSONObject node = null;
			
			if(map.containsKey(id)){
				node = map.get(id);
				node.getJSONArray("children").add(leaf);
			}else{
				node = new JSONObject();
				node.put("pId", pId);
				node.put("id", id);
				node.put("name", name);
				node.put("type", 1);
				JSONArray children = new JSONArray();
				children.add(leaf);
				node.put("children", children);
				map.put(id, node);
			}
			
			while(!pId.equals(0)){
				tc = testCaseService.findById(pId);
				
				name = tc.getName();
				id = tc.getId();
				pId = tc.getpId();
				
				JSONObject parnetNode = null;
				
				if(map.containsKey(id)){
					parnetNode = map.get(id);
					parnetNode.getJSONArray("children").add(node);
				}else{
					parnetNode = new JSONObject();
					parnetNode.put("id", id);
					parnetNode.put("name", name);
					parnetNode.put("type", 1);
					parnetNode.put("pId", pId);
					JSONArray parentChildren = new JSONArray();
					parentChildren.add(node);
					parnetNode.put("children", parentChildren);
					map.put(id, parnetNode);
				}
						
//				if(parnetNode.containsKey("children")){
//					parnetNode.getJSONArray("children").add(node);
//				}else{
//					JSONArray parentChildren = new JSONArray();
//					parentChildren.add(node);
//					parnetNode.put("children", parentChildren);
//				}
				node = parnetNode;
			}
			
		});
		
		rootNode.put("children", treeNodes);
		rootNode.put("id", 0);
		rootNode.put("type", 1);
		rootNode.put("name", "测试用例");
		
		map.forEach((k,v)->{
			if(v.getInteger("pId").equals(0)){
				treeNodes.add(v);
			}
		});
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		list.add(rootNode);
//		
//		List<JSONObject> tempList = treeNodes.stream().filter(x->{
//			return !x.getInteger("pId").equals(0);
//		}).collect(Collectors.toList());
//		
//		while(tempList.size()>0){
//			tempList.forEach(x->{
//				
//			});
//		}
//	
//		
//		tcis.stream().forEach(x->{
//			JSONObject leaf = new JSONObject();
//			leaf.put("id", x.getId());
//			leaf.put("name", x.getStep());
//			leaf.put("type", 0);
//			leaf.put("api", x);
//
//			TestCase tc = x.getTestCase();
//			String name = tc.getName();
//			Integer id = tc.getId();
//			Integer pId = tc.getpId();
//			
//			leaf.put("pId", pId);
//			
//			JSONObject treeNode = treeNodes.stream().filter(y->{
//				return y.getInteger("id").equals(tc.getId());
//				}).map(z->{
//					z.getJSONArray("children").add(leaf);
//					return z;
//				}).findFirst().orElseGet(()->{
//					JSONObject jsonTreeNode =  new JSONObject();
//					jsonTreeNode.put("name", name);
//					jsonTreeNode.put("id", tc.getId());
//					jsonTreeNode.put("type", 1);
//					jsonTreeNode.put("pId", tc.getpId());
//					JSONArray leafs = new JSONArray();
//					leafs.add(leaf);
//					jsonTreeNode.put("children", leafs);
//					treeNodes.add(jsonTreeNode);
//					return jsonTreeNode;
//					});
//			
//			while(true){
//				treeNodes.stream().forEach(y->{
//					if(y.equals(pId)){
//						y.getJSONArray("children").stream().filter(z->{
//							JSONObject jsonObj = (JSONObject)z;
//							return jsonObj.getInteger("id").equals(id);
//						}).findFirst().orElseGet(()->{
//							y.getJSONArray("children").add(treeNode);
//							return y;
//						});
//					}
//				});
//				
//				if(pId.equals(0)){
//					break;
//				}else{
//					TestCase parentCase = testCaseService.findById(pId);
//					JSONObject jsonParentTNode = new JSONObject();
//					jsonParentTNode.put("name", parentCase.getName());
//					jsonParentTNode.put("id", parentCase.getId());
//					jsonParentTNode.put("type", 1);
//					jsonParentTNode.put("pId", parentCase.getpId());
//					jsonParentTNode.put("children", treeNode);
//					treeNodes.add(jsonParentTNode);
//				}
//			}
//		});
//		
//		List<JSONObject> list = treeNodes.stream().filter(x->{
//			return x.getInteger("pId").equals(0);
//		}).collect(Collectors.toList());
		
		return ResultUtil.success(list);
	}
	
}
