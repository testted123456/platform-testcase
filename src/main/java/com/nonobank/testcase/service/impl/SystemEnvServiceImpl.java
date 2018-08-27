package com.nonobank.testcase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.Env;
import com.nonobank.testcase.entity.SystemCfg;
import com.nonobank.testcase.entity.SystemEnv;
import com.nonobank.testcase.repository.SystemEnvRepository;
import com.nonobank.testcase.service.EnvService;
import com.nonobank.testcase.service.SystemCfgService;
import com.nonobank.testcase.service.SystemEnvService;

@Service
public class SystemEnvServiceImpl implements SystemEnvService {
	
	public static Logger logger = LoggerFactory.getLogger(SystemEnvService.class);
	
	@Autowired
	SystemEnvRepository systemEnvRepository;
	
	@Autowired
	SystemCfgService systemCfgService;
	
	@Autowired
	EnvService envService;
	
	@Override
	public SystemEnv findById(Integer id){
		return systemEnvRepository.findById(id);
	}
	
	@Override
	public SystemEnv findBySystemCfgIdAndEnvId(Integer systemCfgId, Integer envId){
		return systemEnvRepository.findBySystemCfgIdAndEnvId(systemCfgId, envId);
	}

	@Override
	public SystemEnv add(SystemEnv systemEnv) {
		SystemEnv existSystemEnv = findBySystemCfgIdAndEnvId(systemEnv.getSystemCfg().getId(), systemEnv.getEnv().getId());
		
		if(null == existSystemEnv || existSystemEnv.getId().equals(systemEnv.getId())){
			return systemEnvRepository.save(systemEnv);
		}else{
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "系统名称或环境名称有重复");
		}
	}

	@Override
	public SystemEnv update(SystemEnv systemEnv) {
		SystemEnv systemEnvDB = findById(systemEnv.getId());
		
		if(null != systemEnvDB){
			return systemEnvRepository.save(systemEnv);
		}else{
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "要更新的记录不存在");
		}
	}

	@Override
	public void delete(Integer id) {
		systemEnvRepository.delete(id);
	}

	@Override
	public List<SystemEnv> findAll() {
		return systemEnvRepository.findAll();
	}
	
	@Override
	public List<SystemEnv> findBySystemCfgId(Integer systemCfgId){
		return systemEnvRepository.findBySystemCfgId(systemCfgId);
	}

	@Override
	public String getApiRequest(String request) {
		// TODO Auto-generated method stub
		return request;
	}

	@Override
	public String getApiResponse(String response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemEnv findBySystemAndEnv(String system, String env) {
		// TODO Auto-generated method stub
		Env e = envService.findByName(env);
		SystemCfg scfg = 
//				systemCfgService.findBySystem(system);
		systemCfgService.findByAlias(system);
		
		if(null != e && null != scfg){
			SystemEnv systemEnv = systemEnvRepository.findBySystemCfgIdAndEnvId(scfg.getId(), e.getId());
			return systemEnv;
		}
		
		return null;
	}

}
