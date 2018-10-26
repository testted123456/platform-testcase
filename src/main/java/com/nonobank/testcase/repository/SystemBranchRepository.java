package com.nonobank.testcase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.SystemBranch;

public interface SystemBranchRepository extends JpaRepository<SystemBranch, Integer> {
	SystemBranch findById(Integer id);
	
	SystemBranch findBySystemAndBranch(String system, String branch);
	
	List<SystemBranch> findByBranch(String branch);
	
	List<SystemBranch> findBySystemAndOptstatusNot(String system, short optstatus); 
	
	List<SystemBranch> findBySystemAndBranchAndOptstatusNot(String system, String branch, short optstatus);
	
	SystemBranch findBySystemAndBranchAndOptstatusEquals(String system, String branch, short optstatus);
	
	List<SystemBranch> findBySystemAndLastAndOptstatusEquals(String system, boolean last, short optstatus);
}
