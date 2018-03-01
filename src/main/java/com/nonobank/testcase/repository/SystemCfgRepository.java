package com.nonobank.testcase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonobank.testcase.entity.SystemCfg;

public interface SystemCfgRepository extends JpaRepository<SystemCfg, Integer> {
	
	SystemCfg findById(Integer id);

	SystemCfg findBySystem(String system);
	
	SystemCfg findByAlias(String alias);
	
	List<SystemCfg> findBySystemOrAlias(String system, String alias);
}
