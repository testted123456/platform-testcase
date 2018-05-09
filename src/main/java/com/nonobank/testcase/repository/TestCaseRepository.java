package com.nonobank.testcase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nonobank.testcase.entity.TestCase;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

	TestCase findByIdAndOptstatusEquals(Integer id, short optstatus);
	
	List<TestCase> findByPIdAndOptstatusEquals(Integer pId, short optstatus);

	@Query("select tc.id as id, tc.pId as pId, tc.name as name, tc.type as type from TestCase tc where tc.name like %?1% and tc.createdBy like %?2% and tc.type=1 and tc.optstatus!=2")
	List<Object []> findByNameAndCreatedBy(String name, String createdBy);
	
	@Query("select tc.id as id, tc.pId as pId, tc.name as name, tc.type as type from TestCase tc where tc.id=?1")
	List<Object []> findById(Integer id);
}
