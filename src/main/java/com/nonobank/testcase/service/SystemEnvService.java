package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.SystemEnv;

public interface SystemEnvService {
	
	SystemEnv add(SystemEnv systemEnv);
	
	SystemEnv update(SystemEnv systemEnv);
	
	void delete(Integer id);
	
	SystemEnv findById(Integer id);
	
	SystemEnv findBySystemCfgIdAndEnvId(Integer systemCfgId, Integer envId);
	
	SystemEnv findBySystemAndEnv(String system, String env);
	
	List<SystemEnv> findAll();
	
	List<SystemEnv> findBySystemCfgId(Integer systemCfgId);
	
	String getApiRequest(String request);
	
	String getApiResponse(String response);
	
}
