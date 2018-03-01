package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.SystemEnv;

public interface SystemEnvService {
	
	SystemEnv add(SystemEnv systemEnv);
	
	SystemEnv update(SystemEnv systemEnv);
	
	void delete(SystemEnv systemEnv);
	
	SystemEnv findById(Integer id);
	
	List<SystemEnv> findBySystemName(String systemName);
	
	List<SystemEnv> findByEnvName(String envName);
	
	SystemEnv findBySystemNameAndEnvName(String systemName, String envName);	
	
	List<SystemEnv> findBySystemNameOrEnvName(String systemName, String envName);
}
