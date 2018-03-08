package com.nonobank.testcase.service.impl;

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
	public void add(List<TestCaseInterfaceFront> tcifs) {
		
		int size = tcifs.size();
		
		for(int i=0;i<size;i++){
			TestCaseInterfaceFront tcif = tcifs.get(i);
			TestCaseInterface tci = tcif.convert();
			tcif.setOrderNo(i);
			testCaseInterfaceRepository.save(tci);
		}
	}

	@Override
	@Transactional
	public void update(List<TestCaseInterfaceFront> tcifs) {
		
		int size = tcifs.size();
		
		Integer tcId = null;
		
		for(int i=0;i<size;i++){
			TestCaseInterface tci = tcifs.get(i).convert();
			tcId = tci.getTestCase().getId();
			tci.setOrderNo(i);
			testCaseInterfaceRepository.save(tci);
		}
		
		List<TestCaseInterface> tcis = testCaseInterfaceRepository.findByTestCaseIdAndOptstatusEquals(tcId, (short)0);
		
		
		tcis.forEach(t->{
			long count = tcifs.stream().filter(s->{
				return s.getId().equals(t.getId());
			}).count();
			
			if(count == 0){
				t.setOptstatus((short)2);
				testCaseInterfaceRepository.save(t);
			}
		});
	}

}
