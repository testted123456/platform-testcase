package com.nonobank.testcase.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.component.exception.TestCaseException;
import com.nonobank.testcase.component.result.Result;
import com.nonobank.testcase.component.result.ResultCode;
import com.nonobank.testcase.entity.SystemBranch;
import com.nonobank.testcase.entity.SystemCfg;
import com.nonobank.testcase.repository.SystemBranchRepository;
import com.nonobank.testcase.service.SystemBranchService;

@Service
public class SystemBranchServiceImpl implements SystemBranchService {
	
	@Autowired
	SystemBranchRepository systemBranchRepository;

	@Override
	public List<SystemBranch> findBySystem(String system) {
		return systemBranchRepository.findBySystemAndOptstatusNot(system,  (short)2);
	}

	@Override
	public void add(String system, List<SystemBranch> systemBranches) {
		systemBranches.forEach(s->{
			String branch = s.getBranch();
			SystemBranch systemBranch = systemBranchRepository.findBySystemAndBranch(system, branch);
			
			if(null == systemBranch){
				systemBranch = new SystemBranch();
				systemBranch.setVersion(null);
				systemBranch.setId(null);
				systemBranch.setLast(null);
				systemBranch.setOptstatus((short)0);
				systemBranchRepository.save(systemBranch);
			}else if(systemBranch.getOptstatus().equals((short)2)){
				systemBranch.setOptstatus((short)2);
				systemBranch.setLast(null);
				systemBranchRepository.save(systemBranch);
			}
		});
		
		List<SystemBranch> systemBranchesInDB = systemBranchRepository.findBySystemAndOptstatusNot(system, (short)2);
		
		systemBranchesInDB.forEach(s->{
			long count = systemBranches.stream().filter(x->{
				return x.getSystem().equals(s.getSystem()) && x.getBranch().equals(s.getBranch());
			}).count();
			
			if(count == 0){
				s.setOptstatus((short)2);
				systemBranchRepository.save(s);
			}
		});
	}

	@Override
	public SystemBranch update(SystemBranch systemBranch) {
		SystemBranch systemBranchInDB = systemBranchRepository.findBySystemAndBranch(systemBranch.getSystem(), systemBranch.getBranch());
		
		if(null != systemBranchInDB && systemBranchInDB.getOptstatus() != (short)2){
			systemBranchInDB.setOptstatus(systemBranch.getOptstatus());
			systemBranchInDB.setVersion(systemBranch.getVersion());
			systemBranchRepository.save(systemBranchInDB);
			return systemBranchInDB;
		}else{
			throw new TestCaseException(ResultCode.VALIDATION_ERROR.getCode(), "分支不存在或已被删除");
		}
	}

	@Override
	public SystemBranch findBySystemAndBranch(String system, String branch) {
		return systemBranchRepository.findBySystemAndBranch(system, branch);
	}

	@Override
	public List<SystemBranch> findBySystemAndLast(String system, boolean last) {
		return systemBranchRepository.findBySystemAndLastAndOptstatusEquals(system, last, (short)0);
	}

	@Override
	public List<SystemBranch> findall() {
		return systemBranchRepository.findAll();
	}

	@Override
	public SystemBranch findById(Integer id) {
		return systemBranchRepository.findById(id);
	}

	@Override
	public void update(String system, List<String> branches) {
		branches.forEach(x->{
			SystemBranch systemBranch = findBySystemAndBranch(system, x);
			if(null == systemBranch){
				systemBranch = new SystemBranch();
				systemBranch.setSystem(system);
				systemBranch.setBranch(x);
				systemBranch.setOptstatus((short)0);
				systemBranch.setVersion("");
				systemBranchRepository.save(systemBranch);
			}
		});
	}

	@Override
	public SystemBranch updateBySystemAndBranch(SystemBranch systemBranch) {
		// TODO Auto-generated method stub
		Boolean codeChecked = systemBranch.getCodeChecked();
		Boolean last = systemBranch.getLast();
		Short optStatus = systemBranch.getOptstatus();
		String version = systemBranch.getVersion();
		
		SystemBranch sysBranch = findBySystemAndBranch(systemBranch.getSystem(), systemBranch.getBranch());
		
		if(null != codeChecked){
			sysBranch.setCodeChecked(codeChecked);
		}
		
		if(null != last){
			sysBranch.setLast(last);
		}
		
		if(null != optStatus){
			sysBranch.setOptstatus(optStatus);
		}
		
		if(null != version){
			sysBranch.setVersion(version);
		}
		
		sysBranch = update(sysBranch);
		
		return sysBranch;
	}

	@Override
	public List<SystemBranch> findByBranch(String branch) {
		// TODO Auto-generated method stub
		return systemBranchRepository.findByBranch(branch);
	}

	@Override
	public Map<String, Object> findPageByBranch(String branch, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<SystemBranch> page = systemBranchRepository.findByBranch(branch, pageable);
		List<SystemBranch> list = null;
		
		if(page.hasContent()){
			list = page.getContent();
		}
		
		long count = page.getTotalElements();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", Long.valueOf(count));
		map.put("list", list);
		
		return map;
	}
}
