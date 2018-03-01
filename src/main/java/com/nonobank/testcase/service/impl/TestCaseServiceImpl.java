package com.nonobank.testcase.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.repository.TestCaseRepository;
import com.nonobank.testcase.service.TestCaseService;

@Service
public class TestCaseServiceImpl implements TestCaseService {
	
	public static Logger logger = LoggerFactory.getLogger(TestCaseServiceImpl.class);
	
	@Autowired
	TestCaseRepository testCaseRepository;

	@Override
	public TestCase findById(Integer id) {
		return testCaseRepository.findByIdAndOptstatusEquals(id, (short)0);
	}

	@Override
	public List<TestCase> findByPId(Integer pId) {
		return testCaseRepository.findByPIdAndOptstatusEquals(pId, (short)0);
	}

	@Override
	public TestCase add(TestCase testCase, boolean type) {
		testCase.setType(type);
		testCase.setOptstatus((short)0);
		testCase = testCaseRepository.save(testCase);
		logger.info("新增用例{}成功", testCase.getId());
		return testCase;
	}

	@Override
	public TestCase update(TestCase testCase) {
		Integer id = testCase.getId();
		
		if(null != findById(id)){
			testCaseRepository.save(testCase);
			logger.info("更新用例{}成功", id);
		}else{
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "case不在数据库中");
		}
		
		return testCase;
	}

	@Override
	public TestCase delete(TestCase testCase) {
		testCase.setOptstatus((short)2);
		testCaseRepository.save(testCase);
		logger.info("删除用例{}成功", testCase.getId());
		return testCase;
	}
}
