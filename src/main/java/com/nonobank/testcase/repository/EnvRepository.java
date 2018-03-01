package com.nonobank.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonobank.testcase.entity.Env;

public interface EnvRepository extends JpaRepository<Env, Integer> {
	
	Env findById(Integer id);
	
	Env findByName(String name);
}
