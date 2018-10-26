package com.nonobank.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.SystemCfg;

public interface SystemCfgRepository extends JpaRepository<SystemCfg, Integer> {
	
	SystemCfg findById(Integer id);

	SystemCfg findBySystem(String system);
	
	SystemCfg findByAlias(String alias);
	
	SystemCfg findBySystemOrAlias(String system, String alias);
	
}
