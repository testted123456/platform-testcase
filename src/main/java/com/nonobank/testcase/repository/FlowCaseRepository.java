package com.nonobank.testcase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.FlowCase;

public interface FlowCaseRepository extends JpaRepository<FlowCase, Integer> {
	
	FlowCase findById(Integer id);
	
	List<FlowCase> findByPIdAndOptstatusEquals(Integer pId, short optStatus);
}
