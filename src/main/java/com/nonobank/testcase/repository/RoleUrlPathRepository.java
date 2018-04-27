package com.nonobank.testcase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.nonobank.testcase.entity.TestCase;

public interface RoleUrlPathRepository extends JpaRepository<TestCase, Integer> {
	
    @Query(value="select url_path, role_name from role_url_path where system='testCase' and optstatus!=2 ", nativeQuery = true)
    List<Object[]> findUrlAndRole();
}
