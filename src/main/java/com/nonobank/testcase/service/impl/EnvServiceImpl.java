package com.nonobank.testcase.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.Env;
import com.nonobank.testcase.repository.EnvRepository;
import com.nonobank.testcase.service.EnvService;

@Service
public class EnvServiceImpl implements EnvService {
	
	public static Logger logger = LoggerFactory.getLogger(EnvServiceImpl.class);
	
	@Autowired
	EnvRepository envRepository;
	
	public Env findById(Integer id){
		return envRepository.findById(id);
	}

	@Override
	public Env add(Env env) {
		if(envRepository.findByName(env.getName()) == null){
			return envRepository.save(env);
		}else{
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "要添加的记录已存在");
		}
	}

	@Override
	public Env update(Env env) {
		
		if(envRepository.findByName(env.getName()) != null){
			return envRepository.save(env);
		}else{
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "要更新的记录不存在");
		}
	}

	@Override
	public void delete(Env env) {
		envRepository.delete(env);
	}

	@Override
	public Env findByName(String name) {
		return envRepository.findByName(name);
	}
}
