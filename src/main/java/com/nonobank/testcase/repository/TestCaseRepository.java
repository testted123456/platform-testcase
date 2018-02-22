package com.nonobank.testcase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.TestCase;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

	TestCase findByIdAndOptstatusEquals(Integer id, short optstatus);
	
	List<TestCase> findByPIdAndOptstatusEquals(Integer pId, short optstatus);
}
