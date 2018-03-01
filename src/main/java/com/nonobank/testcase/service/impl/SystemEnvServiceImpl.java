package com.nonobank.testcase.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.SystemEnv;
import com.nonobank.testcase.repository.SystemEnvRepository;
import com.nonobank.testcase.service.SystemEnvService;

@Service
public class SystemEnvServiceImpl implements SystemEnvService {
	
	public static Logger logger = LoggerFactory.getLogger(SystemEnvService.class);
	
	@Autowired
	SystemEnvRepository systemEnvRepository;
	
	@Override
	public SystemEnv findById(Integer id){
		return systemEnvRepository.findById(id);
	}

	@Override
	public List<SystemEnv> findBySystemName(String systemName) {
		return systemEnvRepository.findBySystemName(systemName);
	}

	@Override
	public List<SystemEnv> findByEnvName(String envName) {
		return systemEnvRepository.findByEnvName(envName);
	}

	@Override
	public SystemEnv findBySystemNameAndEnvName(String systemName, String envName) {
		return systemEnvRepository.findBySystemNameAndEnvName(systemName, envName);
	}

	@Override
	public SystemEnv add(SystemEnv systemEnv) {
		List<SystemEnv> optList = findBySystemNameOrEnvName(systemEnv.getSystemName(), systemEnv.getEnvName());
		
		if(null == optList || optList.size() == 0){
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
	public void delete(SystemEnv systemEnv) {
		systemEnvRepository.delete(systemEnv.getId());
	}

	@Override
	public List<SystemEnv> findBySystemNameOrEnvName(String systemName, String envName) {
		return systemEnvRepository.findBySystemNameOrEnvName(systemName, envName);
	}

}
