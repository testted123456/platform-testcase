package com.nonobank.testcase.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.nonobank.testcase.component.result.Result;
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
	
	public SystemBranch updateBySystemAndBranch( SystemBranch systemBranch);
	
	void update(String system, List<String> branches);
	
	/**
	 * 获取未删除的系统分支
	 * @param system
	 * @return
	 */
	List<SystemBranch> findBySystem(String system); 
	
	List<SystemBranch> findByBranch(String branch);
	
	/**
	 * 根据system、branch查询
	 * @param system
	 * @param branch
	 * @return
	 */
	SystemBranch findBySystemAndBranch(String system, String branch);
	
	SystemBranch findById(Integer id);
	
	List<SystemBranch> findBySystemAndLast(String system, boolean last);

	List<SystemBranch> findall();
}
