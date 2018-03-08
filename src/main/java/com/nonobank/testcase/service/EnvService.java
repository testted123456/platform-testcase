package com.nonobank.testcase.service;

import java.util.List;

import com.nonobank.testcase.entity.Env;

public interface EnvService {

	Env add(Env env);
	
	Env update(Env env);
	
	void delete(Env env);
	
	Env findByName(String name);
	
	Env findById(Integer id);
	
	List<Env> findAll();
}
