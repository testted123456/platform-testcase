package com.nonobank.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.SystemEnv;

public interface SystemEnvRepository extends JpaRepository<SystemEnv, Integer> {
	
	SystemEnv findById(Integer id);
	
//	List<SystemEnv> findBySystemName(String systemName);
	
//	List<SystemEnv> findByEnvName(String envName);
	
//	SystemEnv findBySystemNameAndEnvName(String systemName, String envName);
	
//	List<SystemEnv> findBySystemNameOrEnvName(String systemName, String envName);
	
	SystemEnv findBySystemCfgIdAndEnvId(Integer systemCfgId, Integer envId);
}
