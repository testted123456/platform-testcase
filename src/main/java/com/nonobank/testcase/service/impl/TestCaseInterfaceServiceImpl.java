package com.nonobank.testcase.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nonobank.testcase.component.MessageEntity;
import com.nonobank.testcase.component.validation.ErrorCode;
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
		// TODO Auto-generated method stub
		return testCaseInterfaceRepository.findByIdAndOptstatusEquals(id, (short)0);
	}

	@Override
	public List<TestCaseInterface> findByTestCaseId(Integer testCaseId) {
		// TODO Auto-generated method stub
		return testCaseInterfaceRepository.findByTestCaseIdAndOptstatusEquals(testCaseId, (short)0);
	}

	@Override
	public MessageEntity add(Optional<TestCaseInterface> optTestCaseInterface) {
		// TODO Auto-generated method stub
		MessageEntity msg = new MessageEntity();
		msg.setSucceed(false);
		msg.setErrorCode(ErrorCode.entity_empty_error);
		msg.setErrorMessage("用例中的接口为空");
		
		optTestCaseInterface.map(x->{
			try{
				testCaseInterfaceRepository.save(x);
				logger.info("新增用例接口成功");
				msg.setSucceed(true);
				msg.setErrorCode(ErrorCode.correct);
				msg.setErrorMessage("新增用例接口成功");
				msg.setData(x);
			}catch(Exception e){
				logger.error("新增用例接口失败");
				msg.setSucceed(false);
				msg.setErrorCode(ErrorCode.exception_occurred_error);
				msg.setErrorMessage("新增用例接口失败");
			}
			return msg;
		});
		
		return msg;
	}

	/**
	@Override
	@Transactional
	public MessageEntity add(List<Optional<TestCaseInterface>> optTestCaseInterfaces) {
		// TODO Auto-generated method stub
		MessageEntity msg = new MessageEntity();
		msg.setSucceed(false);
		msg.setErrorCode(ErrorCode.entity_empty_error);
		msg.setErrorMessage("用例中的接口为空");
		 
		 Optional<TestCaseInterface> optTestCaseInterface =
		 optTestCaseInterfaces.stream().findFirst().flatMap(x->x);
		 
		 testCaseService.add(optTestCaseInterface.map(x->x.getTestCase()), true);
		
		 optTestCaseInterfaces.stream().forEach(x->{
			 add(x);
		});
		
		msg.setSucceed(true);
		msg.setErrorCode(ErrorCode.correct);
		msg.setErrorMessage("新增用例接口成功");
		msg.setData(optTestCaseInterfaces);
			
		return msg;
	}
	**/
	
	@Override
//	@Transactional
	public MessageEntity add(List<TestCaseInterface> testCaseInterfaces) {
		MessageEntity msg = new MessageEntity();
		
		testCaseInterfaces.forEach(x->{
			try{
				testCaseInterfaceRepository.save(x);
				logger.info("新增用例接口成功");
				msg.setSucceed(true);
				msg.setErrorCode(ErrorCode.correct);
			}catch(Exception e){
				logger.error("新增用例接口失败");
				msg.setSucceed(false);
				msg.setErrorCode(ErrorCode.exception_occurred_error);
			}
		});
		
		return msg;
	}

	@Override
	public MessageEntity update(Optional<TestCaseInterface> optTestCaseInterface) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageEntity update(List<Optional<TestCaseInterface>> optTestCaseInterfaces) {
		// TODO Auto-generated method stub
		return null;
	}

}
