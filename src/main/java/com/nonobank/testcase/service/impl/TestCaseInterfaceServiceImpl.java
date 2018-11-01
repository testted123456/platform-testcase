package com.nonobank.testcase.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nonobank.testcase.entity.TestCaseInterface;
import com.nonobank.testcase.entity.TestCaseInterfaceFront;
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
	@Transactional
	public void add(String userName, List<TestCaseInterfaceFront> tcifs) {
		
		int size = tcifs.size();
		
		for(int i=0;i<size;i++){
			TestCaseInterfaceFront tcif = tcifs.get(i);
			
			logger.info("新增用例接口，用例{},接口{}",tcif.getTestCase().getName(), tcif.getName());
			
			TestCaseInterface tci = tcif.convert();
			tci.setOrderNo(i);
			tci.setCreatedBy(userName);
			tci.setCreatedTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
//			tci.setUpdatedTime(null);
			tci.setOptstatus((short)0);
			testCaseInterfaceRepository.save(tci);
			tcif.setId(tci.getId());
		}
	}

	@Override
	@Transactional
	public List<TestCaseInterfaceFront> update(String userName, List<TestCaseInterfaceFront> tcifs) {
		
		int size = tcifs.size();
		Integer tcId = null;
		
		for(int i=0;i<size;i++){//保存用例接口
			TestCaseInterfaceFront tcif = tcifs.get(i);
			TestCaseInterface tci = tcif.convert();
			tcId = tci.getTestCase().getId();
			tci.setOrderNo(i);
			
			if(tci.getId() == null){
				logger.info("新增用例接口，用例{},接口{}",tcif.getTestCase().getName(), tcif.getName());
				
				tci.setCreatedBy(userName);
				tci.setCreatedTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
				tci.setOptstatus((short)0);
			}else{
				logger.info("更新用例接口，用例{},接口{}",tcif.getTestCase().getName(), tcif.getName());
				
				tci.setUpdatedBy(userName);
				tci.setUpdatedTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
			}
			
			testCaseInterfaceRepository.save(tci);
			tcif.setId(tci.getId());
			tcif.setOrderNo(tci.getOrderNo());
			tcif.setOptstatus(tci.getOptstatus());
		}
		
		List<TestCaseInterface> tcis = testCaseInterfaceRepository.findByTestCaseIdAndOptstatusEquals(tcId, (short)0);
		
		//删除接口　
		tcis.forEach(t->{
			long count = tcifs.stream().filter(s->{
				return s.getId().equals(t.getId());
			}).count();
			
			if(count == 0){
				t.setOptstatus((short)2);
				testCaseInterfaceRepository.save(t);
			}
		});
		
		return tcifs;
	}

	@Override
	public List<TestCaseInterface> findByInterfaceIdAndOptstatusEquals(Integer interfaceId) {
		// TODO Auto-generated method stub
		return testCaseInterfaceRepository.findByInterfaceIdAndOptstatusEquals(interfaceId, (short)0);
	}

	

}
