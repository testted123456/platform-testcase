package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.SystemBranch;

public interface SystemBranchService {
	
	SystemBranch add(SystemBranch systemBranch);
	
	List<SystemBranch> add(List<SystemBranch> systemBranches);
	
	SystemBranch delete(String system, String branch);
	
	SystemBranch delete(SystemBranch systemBranch);
	
	SystemBranch findBySystemAndBranch(String system, String branch);
	
	List<SystemBranch> findBySystem(String system); 
}
