package com.nonobank.testcase.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.GlobalVariable;
import com.nonobank.testcase.repository.GlobalVariableRepository;
import com.nonobank.testcase.service.GlobalVariableService;

@Service
public class GlobalVariableServiceImpl implements GlobalVariableService {
	
	@Autowired
	GlobalVariableRepository globalVariableRepository;

	@Override
	public List<GlobalVariable> getAll() {
		return globalVariableRepository.findAll();
	}

	@Override
	public GlobalVariable addOrUpdate(GlobalVariable globalVariable) {
		Integer id = globalVariable.getId();
		String name = globalVariable.getName();
		GlobalVariable gv = findByName(name);
		
		if(gv == null || gv.getId().equals(id)){
			globalVariable = globalVariableRepository.save(globalVariable);
		}else{
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "变量名已存在！　");
		}
		
		return globalVariable;
	}

	@Override
	public void delete(GlobalVariable globalVariable) {
		 globalVariableRepository.delete(globalVariable);
	}

	@Override
	public GlobalVariable findByName(String name) {
		return globalVariableRepository.findByName(name);
	}

}
