package com.nonobank.testcase.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.nonobank.testcase.entity.SystemBranch;
import com.nonobank.testcase.repository.SystemBranchRepository;
import com.nonobank.testcase.service.SystemBranchService;

public class SystemBranchServiceImpl implements SystemBranchService {
	
	@Autowired
	SystemBranchRepository systemBranchRepository;

	@Override
	public List<SystemBranch> findBySystem(String system) {
		return systemBranchRepository.findBySystemAndOptstatusEquals(system,  (short)0);
	}

	@Override
	public SystemBranch delete(String system, String branch) {
		return null;
	}

	@Override
	public SystemBranch findBySystemAndBranch(String system, String branch) {
		return systemBranchRepository.findBySystemAndBranchAndOptstatusEquals(system, branch, (short)0);
	}

	@Override
	public SystemBranch add(SystemBranch systemBranch) {
		systemBranch.setOptstatus((short)0);
		return systemBranchRepository.save(systemBranch);
	}

	@Override
	public SystemBranch delete(SystemBranch systemBranch) {
		systemBranch.setOptstatus((short)2);
		return systemBranchRepository.save(systemBranch);
	}

	@Override
	public List<SystemBranch> add(List<SystemBranch> systemBranches) {
		return null;
	}

}
