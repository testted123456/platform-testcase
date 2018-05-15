package com.nonobank.testcase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nonobank.testcase.entity.TestCase;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

	TestCase findByIdAndOptstatusEquals(Integer id, short optstatus);
	
	List<TestCase> findByPIdAndOptstatusEquals(Integer pId, short optstatus);

//	@Query("select tc.id as id, tc.pId as pId, tc.name as name, tc.type as type from TestCase tc where tc.name like %?1% and tc.createdBy like %?2% and tc.type=1 and tc.optstatus!=2")
//	List<Object []> findByNameAndCreatedBy(String name, String createdBy);
	
	@Query(value = "select distinct tc.id, tc.p_id as pId, tc.name, tc.type from " +
 			"test_case tc, test_case_interface tci, interface_definition idf " + 
			"where " +
			"tc.name like %:name% and tc.created_by like %:createdBy% and tc.type=1 and tc.optstatus!=2 " +
			"and idf.name like %:apiName% and idf.url_address like %:urlAddress% " +
			"and tc.id=tci.test_case_id and tci.interface_id = idf.id", nativeQuery = true)
	List<Object []> searchCases(@Param("name") String name, @Param("createdBy") String createdBy, @Param("apiName") String apiName, @Param("urlAddress") String urlAddress);
	
	@Query("select tc.id as id, tc.pId as pId, tc.name as name, tc.type as type from TestCase tc where tc.id=?1")
	List<Object []> findById(Integer id);
}
