package com.nonobank.testcase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.SystemBranch;

public interface SystemBranchRepository extends JpaRepository<SystemBranch, Integer> {
	SystemBranch findById(Integer id);
	
	List<SystemBranch> findBySystemAndOptstatusEquals(String system, short optstatus); 
	
	SystemBranch findBySystemAndBranchAndOptstatusEquals(String system, String branch, short optstatus);
}
