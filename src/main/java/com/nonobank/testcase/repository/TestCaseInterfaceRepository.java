package com.nonobank.testcase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.TestCaseInterface;


public interface TestCaseInterfaceRepository extends JpaRepository<TestCaseInterface, Integer> {
	
	TestCaseInterface findById(Integer id);
	
	List<TestCaseInterface> findByInterfaceIdAndOptstatusEquals(Integer interfaceId, short optstatus);

	TestCaseInterface findByIdAndOptstatusEquals(Integer id, short optstatus);
	
	List<TestCaseInterface> findByTestCaseIdAndOptstatusEquals(Integer testCaseId, short optstatus);
}
