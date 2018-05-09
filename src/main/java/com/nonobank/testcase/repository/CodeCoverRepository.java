package com.nonobank.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.CodeCover;

public interface CodeCoverRepository extends JpaRepository<CodeCover, Integer> {
	
	CodeCover findBySystemAndBranch(String system, String branch);

}
