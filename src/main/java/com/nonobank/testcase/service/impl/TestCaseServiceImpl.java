package com.nonobank.testcase.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
				System.out.println(tci.getApiType());
				
				if(null == tci.getId()){//新增接口
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
			
			testCaseRepository.save(testCase);
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
}
