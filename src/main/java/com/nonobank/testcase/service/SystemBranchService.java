package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.SystemBranch;

public interface SystemBranchService {
	
	/**
	 * 同步系统分支
	 * @param systemBranches
	 * @return
	 */
	void add(String system, List<SystemBranch> systemBranches);
	
	/**
	 * 更新系统分支
	 * @param systemBranch
	 * @return
	 */
	SystemBranch update(SystemBranch systemBranch);
	
	/**
	 * 获取未删除的系统分支
	 * @param system
	 * @return
	 */
	List<SystemBranch> findBySystem(String system); 
	
	/**
	 * 根据system、branch查询
	 * @param system
	 * @param branch
	 * @return
	 */
	SystemBranch findBySystemAndBranch(String system, String branch);
	
	
	List<SystemBranch> findBySystemAndLast(String system, boolean last);
	
	List<SystemBranch> findall();
	
}
