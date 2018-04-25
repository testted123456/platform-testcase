package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.GlobalVariable;

public interface GlobalVariableService {
	
	public List<GlobalVariable> getAll();
	
	public GlobalVariable addOrUpdate(GlobalVariable globalVariable);
	
	public void delete (GlobalVariable globalVariable);
	
	public GlobalVariable findByName(String name);

}
