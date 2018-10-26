package com.nonobank.testcase.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.entity.TestCaseFront;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.repository.TestCaseRepository;
import com.nonobank.testcase.service.TestCaseInterfaceService;
import com.nonobank.testcase.service.TestCaseService;

@Service
public class TestCaseServiceImpl implements TestCaseService {
	
	public static Logger logger = LoggerFactory.getLogger(TestCaseServiceImpl.class);
	
	@Autowired
	TestCaseRepository testCaseRepository;
	
	@Autowired
	TestCaseInterfaceService testCaseInterfaceService;

	@Override
	public TestCase findById(Integer id) {
		return testCaseRepository.findByIdAndOptstatusEquals(id, (short)0);
	}

	@Override
	public List<TestCase> findByPId(Integer pId) {
		return testCaseRepository.findByPIdAndOptstatusEquals(pId, (short)0);
	}

	@Override
	public TestCase add(String userName, TestCaseFront testCaseFront, boolean type) {
		TestCase testCase = testCaseFront.convert();
		testCase.setType(type);
		testCase.setOptstatus((short)0);
		testCase.setCreatedBy(userName);
		testCase.setCreatedTime(LocalDateTime.now());
		
		List<TestCaseInterface> tcis = testCase.getTestCaseInterfaces();
		
		if(null == tcis){
			testCaseRepository.save(testCase);
			logger.info("新增用例{}成功", testCase.getId());
			return testCase;
		}
		
		int size = tcis.size();
		
		for(int i=0;i<size;i++){
			TestCaseInterface tci = tcis.get(i);
			tci.setTestCase(testCase);
			tci.setOrderNo(i);
			tci.setCreatedBy(userName);
			tci.setCreatedTime(LocalDateTime.now());
			tci.setId(null);
			tci.setOptstatus((short)0);
		}
		
		testCaseRepository.save(testCase);
		logger.info("新增用例{}成功", testCase.getId());
		return testCase;
	}

	@Override
	public TestCase update(String userName, TestCaseFront testCaseFront) {
		TestCase testCase = testCaseFront.convert();
		
		Integer id = testCase.getId();
		
		if(null != findById(id)){
			testCase.setUpdatedBy(userName);
			testCase.setUpdatedTime(LocalDateTime.now());
			
			List<TestCaseInterface> tcis = testCase.getTestCaseInterfaces();
			int size = tcis.size();
			
			for(int i=0;i<size;i++){
				TestCaseInterface tci = tcis.get(i);
				tci.setOrderNo(i);
				tci.setOptstatus((short)0);
//				System.out.println(tci.getApiType());
				
				if(null == tci.getId() ){//新增接口
					tci.setTestCase(testCase);
					tci.setCreatedBy(userName);
					tci.setCreatedTime(LocalDateTime.now());
				}else{//更新接口
					tci.setUpdatedBy(userName);
					tci.setUpdatedTime(LocalDateTime.now());
				}
			}
			
			List<TestCaseInterface> dbTcis = testCaseInterfaceService.findByTestCaseId(id);
//					testCaseInterfaceRepository.findByTestCaseIdAndOptstatusEquals(tcId, (short)0);
			dbTcis.forEach(x->{
				boolean res = tcis.stream().filter(y->{return y.getId() != null;}).noneMatch(y->{
					return y.getId().equals(x.getId());
				});
				
				if(res == true){
					x.setOptstatus((short)2);
					tcis.add(x);
				}
			});
			
			testCase = testCaseRepository.save(testCase);
			
			List<TestCaseInterface> updatedTcis =
			testCase.getTestCaseInterfaces().stream().filter(x->{return x.getOptstatus() != 2;}).collect(Collectors.toList());
			
			testCase.setTestCaseInterfaces(updatedTcis);
			logger.info("更新用例{}成功", id);
		}else{
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "case不在数据库中");
		}
		
		return testCase;
	}

	@Override
	public TestCase deleteTestCase(String userName, Integer id) {
		TestCase testCase = testCaseRepository.findByIdAndOptstatusEquals(id, (short)0);
		testCase.setOptstatus((short)2);
		testCase.setUpdatedBy(userName);
		testCase.setUpdatedTime(LocalDateTime.now());
		testCaseRepository.save(testCase);
		logger.info("删除用例{}成功", testCase.getId());
		return testCase;
	}

	@Override
	@Transactional
	public void deleteTestCaseDir(String userName, Integer id) {
		TestCase tc = testCaseRepository.findByIdAndOptstatusEquals(id, (short)0);
		List<TestCase> tcs = testCaseRepository.findByPIdAndOptstatusEquals(id, (short)0);
		
		tc.setUpdatedBy(userName);
		tc.setUpdatedTime(LocalDateTime.now());
		tc.setOptstatus((short)2);
		testCaseRepository.save(tc);
		
		for(TestCase tCase : tcs){
			if(tCase.getType() == true){
				tCase.setOptstatus((short)2);
				tCase.setUpdatedTime(LocalDateTime.now());
				tCase.setUpdatedBy(userName);
				testCaseRepository.save(tCase);
			}else{
				deleteTestCaseDir(userName, tCase.getId());
			}
		}
	}

	/**
	 * 根据名称、创建人模糊查询
	 */
	@Override
	public List<JSONObject> searchCases(String name, String createdBy, String apiName, String urlAddress) {
		//存放用例文件夹
		Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
		List<Object []> objs = testCaseRepository.searchCases(name, createdBy, apiName, urlAddress);
		
		objs.stream().forEach(x->{
			Object id = x[0];
			Object pId = x[1];
			Object caseName = x[2];
			Object type = x[3];
			JSONObject tc = new JSONObject();
			tc.put("id", id);
			tc.put("pId", pId);
			tc.put("name", caseName);
			tc.put("type", type);
			
			if("0".equals(String.valueOf(pId))){
				map.put(Integer.valueOf(String.valueOf(id)), tc);
			}
			
			while(!"0".equals(String.valueOf(pId))){
				JSONObject p_tc = null;
				
				if(map.containsKey(pId)){
					p_tc = map.get(pId);
					JSONArray children = p_tc.getJSONArray("children");
					children.add(tc);
					break;
				}else{
					p_tc = new JSONObject();
					List<Object []> objs2 = testCaseRepository.findById(Integer.parseInt(String.valueOf(pId)));
					
					JSONArray children = new JSONArray();
					children.add(tc);
					
					id = objs2.get(0)[0];
					pId = objs2.get(0)[1];
					caseName = objs2.get(0)[2];
					type = objs2.get(0)[3];
					
					p_tc.put("id", id);
					p_tc.put("pId", pId);
					p_tc.put("name", caseName);
					p_tc.put("type", type);
					p_tc.put("children", children);
					
					map.put(Integer.valueOf(String.valueOf(id)), p_tc);
					tc = p_tc;
					
					if("0".equals(String.valueOf(pId))){
						break;
					}
				}
			}
		});
		
		List<JSONObject> list = new ArrayList<>();
		
		map.forEach((k,v)->{
			if(v.getString("pId").equals("0")){
				list.add(v);
			}
		});
		
		return list;
	}
}
