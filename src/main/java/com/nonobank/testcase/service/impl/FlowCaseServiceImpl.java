package com.nonobank.testcase.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nonobank.testcase.entity.FlowCase;
import com.nonobank.testcase.entity.FlowCaseFront;
import com.nonobank.testcase.entity.FlowCaseTestCase;
import com.nonobank.testcase.entity.TestCase;
import com.nonobank.testcase.repository.FlowCaseRepository;
import com.nonobank.testcase.service.FlowCaseService;
import com.nonobank.testcase.service.TestCaseService;

@Service
public class FlowCaseServiceImpl implements FlowCaseService {
	
	public static Logger logger = LoggerFactory.getLogger(FlowCaseServiceImpl.class);
	
	@Autowired
	FlowCaseRepository flowCaseRepository;
	
	@Autowired
	TestCaseService testCaseService;

	@Override
	public FlowCase add(FlowCase flowCase) {
		List<FlowCaseTestCase> flowCaseTestCases =  flowCase.getFlowCaseTestCases();
		
		FlowCase flowCaseInDB = getById(flowCase.getId());
		
		if(null == flowCaseInDB){
			 return flowCaseRepository.save(flowCase);
		}
		
		List<FlowCaseTestCase> flowCaseTestCasesInDB = flowCaseInDB.getFlowCaseTestCases();
		
		if(null == flowCaseTestCasesInDB){
			 return flowCaseRepository.save(flowCase);
		}
		
		flowCaseTestCasesInDB.stream().forEach(x->x.setOptstatus((short)2));
		
		List<FlowCaseTestCase> flowCaseTestCasesToBeAdd =
		flowCaseTestCases.stream().filter(x->{
			Optional<FlowCaseTestCase> optFlowCaseTestCase = flowCaseTestCasesInDB.stream().filter(y->{
				return y.getOptstatus().equals(2) && y.getTestCaseId().equals(x.getTestCaseId());
			}).findFirst();
			
			if(optFlowCaseTestCase.isPresent()){
				optFlowCaseTestCase.get().setOptstatus((short)0);
				optFlowCaseTestCase.get().setOrderNo(x.getOrderNo());
				return false;
			}else{
				return true;
			}
		}).collect(Collectors.toList());
		
		flowCaseTestCasesInDB.addAll(flowCaseTestCasesToBeAdd);
		
		flowCaseInDB.setFlowCaseTestCases(flowCaseTestCasesInDB);
		
	    return flowCaseRepository.save(flowCaseInDB);
	}

	@Override
	public FlowCase update(FlowCase flowCase) {
		List<FlowCaseTestCase> flowCaseTestCases = flowCase.getFlowCaseTestCases();
		
		for(int i=0;i<flowCaseTestCases.size();i++){
			FlowCaseTestCase flowCaseTestCase = flowCaseTestCases.get(i);
			flowCaseTestCase.setOrderNo(i);
			flowCaseTestCase.setOptstatus((short)0);
			
		}
		
		FlowCase fc = getById(flowCase.getId());
		
		return flowCaseRepository.save(flowCase);
	}

	@Override
	public FlowCase delete(FlowCase flowCase) {
		flowCase.setOptstatus((short)2);
		return flowCaseRepository.save(flowCase);
	}

	@Override
	public FlowCase getById(Integer id) {
		return flowCaseRepository.findById(id);
	}

	@Override
	public List<FlowCase> getTreeByPId(Integer pId) {
		return flowCaseRepository.findByPIdAndOptstatusEquals(pId, (short)0);
	}

	@Override
	public FlowCase convert2Entity(FlowCaseFront flowCaseFront) {
		FlowCase flowCase = new FlowCase();
		flowCase.setId(flowCaseFront.getId());
		flowCase.setName(flowCaseFront.getName());
		flowCase.setDescription(flowCaseFront.getDescription());
		flowCase.setpId(flowCaseFront.getpId());
		flowCase.setType(flowCaseFront.getType());
		flowCase.setEnv(flowCaseFront.getEnv());
		flowCase.setCreatedBy(flowCaseFront.getCreatedBy());
		flowCase.setCreatedTime(flowCaseFront.getCreatedTime());
		flowCase.setUpdatedBy(flowCaseFront.getUpdatedBy());
		flowCase.setUpdatedTime(flowCaseFront.getUpdatedTime());
		flowCase.setOptstatus(flowCaseFront.getOptstatus());
		
		if(flowCaseFront.getType() == false){
			return flowCase;
		}
		
		flowCase.setFlowCaseTestCases(new ArrayList<FlowCaseTestCase>());
		
		int size = flowCaseFront.getTestCases().size();
		
		for(int i=0;i<size;i++){
			FlowCaseTestCase flowCaseTestCase = new FlowCaseTestCase();
			TestCase testCase = flowCaseFront.getTestCases().get(i);
			flowCaseTestCase.setFlowCase(flowCase);
			flowCaseTestCase.setOrderNo(i);
			flowCaseTestCase.setTestCaseId(testCase.getId());
			flowCaseTestCase.setOptstatus((short)0);
			flowCase.getFlowCaseTestCases().add(flowCaseTestCase);
		}
		
		return flowCase;
	}

	@Override
	public FlowCaseFront convert2Front(FlowCase flowCase) {
		FlowCaseFront flowCaseFront = new FlowCaseFront();
		flowCaseFront.setId(flowCase.getId());
		flowCaseFront.setName(flowCase.getName());
		flowCaseFront.setDescription(flowCase.getDescription());
		flowCaseFront.setEnv(flowCase.getEnv());
		flowCaseFront.setpId(flowCase.getpId());
		flowCaseFront.setType(flowCase.getType());
		flowCaseFront.setCreatedBy(flowCase.getCreatedBy());
		flowCaseFront.setCreatedTime(flowCase.getCreatedTime());
		flowCaseFront.setUpdatedBy(flowCase.getUpdatedBy());
		flowCaseFront.setUpdatedTime(flowCase.getUpdatedTime());
		flowCaseFront.setOptstatus(flowCase.getOptstatus());
		flowCaseFront.setTestCases(new ArrayList<TestCase>());
		
		int size = flowCase.getFlowCaseTestCases().size();
		
		for(int i=0;i<size;i++){
			FlowCaseTestCase flowCaseTestCase = flowCase.getFlowCaseTestCases().get(i);
			TestCase testCase = testCaseService.findById(flowCaseTestCase.getTestCaseId());
			flowCaseFront.getTestCases().add(testCase);
		}
		
		return flowCaseFront;
	}

}
