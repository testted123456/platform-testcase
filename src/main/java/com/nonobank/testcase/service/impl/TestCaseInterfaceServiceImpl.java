package com.nonobank.testcase.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.repository.TestCaseInterfaceRepository;
import com.nonobank.testcase.service.TestCaseInterfaceService;
import com.nonobank.testcase.service.TestCaseService;

@Service
public class TestCaseInterfaceServiceImpl implements TestCaseInterfaceService {
	
	public static Logger logger = LoggerFactory.getLogger(TestCaseInterfaceServiceImpl.class);
	
	@Autowired
	TestCaseInterfaceRepository testCaseInterfaceRepository;
	
	@Autowired
	TestCaseService testCaseService;

	@Override
	public TestCaseInterface findById(Integer id) {
		return testCaseInterfaceRepository.findByIdAndOptstatusEquals(id, (short)0);
	}

	@Override
	public List<TestCaseInterface> findByTestCaseId(Integer testCaseId) {
		return testCaseInterfaceRepository.findByTestCaseIdAndOptstatusEquals(testCaseId, (short)0);
	}

	@Override
	public TestCaseInterface add(TestCaseInterface testCaseInterface) {
		testCaseInterfaceRepository.save(testCaseInterface);
		logger.info("新增用例接口成功");
		return testCaseInterface;
	}

	@Override
	@Transactional
	public List<TestCaseInterface> add(List<TestCaseInterface> testCaseInterfaces) {
		
		testCaseInterfaces.forEach(x->{
			testCaseInterfaceRepository.save(x);
			logger.info("新增用例接口成功");
		});
		
		return testCaseInterfaces;
	}

	@Override
	public TestCaseInterface update(TestCaseInterface optTestCaseInterface) {
		return null;
	}

	@Override
	public TestCaseInterface update(List<TestCaseInterface> optTestCaseInterfaces) {
		// TODO Auto-generated method stub
		return null;
	}

}
