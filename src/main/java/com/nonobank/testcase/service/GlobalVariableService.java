package com.nonobank.testcase.service;

import java.util.List;
import java.util.Map;

import com.nonobank.testcase.entity.GlobalVariable;

public interface GlobalVariableService {
	
	public Map<String, Object> getPage(int pageIndex, int pageSize);
	
	public List<GlobalVariable> getAll();
	
	public Map<String, Object> getAllVars();
	
	public GlobalVariable addOrUpdate(GlobalVariable globalVariable);
	
	public void delete (GlobalVariable globalVariable);
	
	public GlobalVariable findByName(String name);

}
