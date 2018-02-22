package com.nonobank.testcase.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.component.MessageEntity;
import com.nonobank.testcase.component.validation.ErrorCode;
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
		// TODO Auto-generated method stub
		return testCaseRepository.findByIdAndOptstatusEquals(id, (short)0);
	}

	@Override
	public List<TestCase> findByPId(Integer pId) {
		// TODO Auto-generated method stub
		return testCaseRepository.findByPIdAndOptstatusEquals(pId, (short)0);
	}

	@Override
	public MessageEntity add(Optional<TestCase> optTestCase, boolean type) {
		// TODO Auto-generated method stub
		MessageEntity msg = new MessageEntity();
		
		msg.setSucceed(false);
		msg.setErrorCode(ErrorCode.entity_empty_error);
		msg.setErrorMessage("新增用例为空");
		
		optTestCase.map(x->{
			x.setType(type);
			x.setOptstatus((short)0);
			try{
				TestCase testCase = testCaseRepository.save(x);
				logger.info("新增用例成功");
				msg.setSucceed(true);
				msg.setErrorCode(ErrorCode.correct);
				msg.setErrorMessage("新增用例成功");
				msg.setData(testCase);
			}catch(Exception e){
				logger.error("新增用例失败");
				msg.setSucceed(false);
				msg.setErrorCode(ErrorCode.exception_occurred_error);
				msg.setErrorMessage("新增用例失败");
			}
			return msg;
			});
		
		return msg;
	}

	@Override
	public MessageEntity update(Optional<TestCase> optTestCase) {
		// TODO Auto-generated method stub
		MessageEntity msg = new MessageEntity();
		msg.setSucceed(false);
		msg.setErrorCode(ErrorCode.entity_empty_error);
		msg.setErrorMessage("更新用例为空");
		
		optTestCase.map(x->{
			Integer id = x.getId();
			if(Optional.ofNullable(findById(id)).isPresent()){
				try{
					TestCase testCase = testCaseRepository.save(x);
					logger.info("更新用例成功");
					msg.setSucceed(true);
					msg.setErrorMessage("更新用例成功");
					msg.setData(testCase);
				}catch(Exception e){
					logger.error("更新用例失败");
					e.printStackTrace();
					msg.setSucceed(false);
					msg.setErrorMessage("更新用例失败");
				}
			}
			return msg;
		});
		
		return msg;
	}

	@Override
	public MessageEntity delete(Optional<TestCase> optTestCase) {
		// TODO Auto-generated method stub
		MessageEntity msg = new MessageEntity();
		msg.setSucceed(false);
		msg.setErrorCode(ErrorCode.entity_empty_error);
		msg.setErrorMessage("删除用例为空");
		
		optTestCase.map(x->{
			Integer id = x.getId();
			if(Optional.ofNullable(findById(id)).isPresent()){
				x.setOptstatus((short)2);
				try{
					TestCase testCase = testCaseRepository.save(x);
					logger.info("删除用例成功");
					msg.setSucceed(true);
					msg.setErrorMessage("删除用例成功");
					msg.setData(testCase);
				}catch(Exception e){
					logger.error("更新用例失败");
					e.printStackTrace();
					msg.setSucceed(false);
					msg.setErrorMessage("更新用例失败");
				}
			}
			return msg;
		});
		
		return msg;
	}

}
