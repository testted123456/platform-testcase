package com.nonobank.testcase.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.SystemCfg;
import com.nonobank.testcase.repository.SystemCfgRepository;
import com.nonobank.testcase.service.SystemCfgService;

@Service
public class SystemCfgServiceImpl implements SystemCfgService{
	
	@Autowired
	SystemCfgRepository systemCfgRepository;

	@Override
	public SystemCfg add(SystemCfg systemCfg) {
		
		if(systemCfg.getId() != null){
			return update(systemCfg);
		}
		
		SystemCfg scfg = systemCfgRepository.findBySystemOrAlias(systemCfg.getSystem(), systemCfg.getAlias());
		
		if(scfg != null){
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "名称或别名不能重复");
		}else{
			return systemCfgRepository.save(systemCfg);
		}
	}

	@Override
	public SystemCfg update(SystemCfg systemCfg) {
		
		SystemCfg scfg = systemCfgRepository.findById(systemCfg.getId());
		
		if(!scfg.getId().equals(systemCfg.getId())){
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "名称或别名不能重复");
		}else{
			return systemCfgRepository.save(systemCfg);
		}
	}

	@Override
	public void delete(SystemCfg systemCfg) {
		systemCfgRepository.delete(systemCfg);
	}

	@Override
	public SystemCfg findBySystem(String system) {
		return systemCfgRepository.findBySystem(system);
	}

	@Override
	public SystemCfg findByAlias(String alias) {
		return systemCfgRepository.findByAlias(alias);
	}

	@Override
	public SystemCfg findBySystemOrAlias(String system, String alias) {
		return systemCfgRepository.findBySystemOrAlias(system, alias);
	}
	
	@Override
	public SystemCfg findById(Integer id){
		return systemCfgRepository.findById(id);
	}

	@Override
	public List<SystemCfg> findAll() {
		// TODO Auto-generated method stub
		return systemCfgRepository.findAll();
	}

	@Override
	public List<String> findAllAlias() {
		// TODO Auto-generated method stub
		List<SystemCfg> sysCfgs = findAll();
		List<String> aliases = new ArrayList<String>();
		
		sysCfgs.forEach(x->{
			aliases.add(x.getAlias());
		});
		
		return aliases;
	}

	@Override
	public SystemCfg findBySystemAndBranch(String system, String branch) {
		return null;
	}

	@Override
	public List<SystemCfg> findPage(int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<SystemCfg> page = systemCfgRepository.findAll(pageable);
		List<SystemCfg> list = null;
		
		if(page.hasContent()){
			list = page.getContent();
		}
		
		return list;
	}

}
