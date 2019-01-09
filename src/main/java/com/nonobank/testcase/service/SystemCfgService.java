package com.nonobank.testcase.service;

import java.util.List;
import java.util.Map;

import com.nonobank.testcase.entity.SystemCfg;

public interface SystemCfgService {
	
	public SystemCfg add(SystemCfg systemCfg);
	
	public SystemCfg update(SystemCfg systemCfg);
	
	public void delete(SystemCfg systemCfg);
	
	public SystemCfg findBySystem(String system);
	
	public SystemCfg findByAlias(String alias);
	
	SystemCfg findById(Integer id);
	
	SystemCfg findBySystemOrAlias(String system, String alias);
	
	SystemCfg findBySystemAndBranch(String system, String branch);
	
	List<SystemCfg> findAll();
	
	List<String> findAllAlias();
	
	Map<String, Object> findPage(int pageIndex, int pageSize);
	
}
