package com.nonobank.testcase.service;

import java.util.List;

import com.nonobank.testcase.entity.FlowCase;
import com.nonobank.testcase.entity.FlowCaseFront;

public interface FlowCaseService {
	
	public FlowCase add(FlowCase flowCase);
	
	public FlowCase update(FlowCase flowCase);
	
	public FlowCase delete(FlowCase flowCase);
	
	public FlowCase getById(Integer id);
	
	public List<FlowCase> getTreeByPId(Integer pId);
	
	public FlowCase convert2Entity(FlowCaseFront flowCaseFront);
	
	public FlowCaseFront convert2Front(FlowCase flowCase);

}
