package com.nonobank.testcase.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.entity.FlowCase;
import com.nonobank.testcase.repository.FlowCaseRepository;
import com.nonobank.testcase.service.FlowCaseService;

@Service
public class FlowCaseServiceImpl implements FlowCaseService {
	
	public static Logger logger = LoggerFactory.getLogger(FlowCaseServiceImpl.class);
	
	@Autowired
	FlowCaseRepository flowCaseRepository;

	@Override
	public FlowCase add(FlowCase flowCase) {
	    return flowCaseRepository.save(flowCase);
	}

	@Override
	public FlowCase update(FlowCase flowCase) {
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

}
