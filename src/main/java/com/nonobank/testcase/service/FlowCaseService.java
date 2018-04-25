package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.FlowCase;

public interface FlowCaseService {
	
	public FlowCase add(FlowCase flowCase);
	
	public FlowCase update(FlowCase flowCase);
	
	public FlowCase delete(FlowCase flowCase);
	
	public FlowCase getById(Integer id);
	
	public List<FlowCase> getTreeByPId(Integer pId);

}
