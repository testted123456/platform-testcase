package com.nonobank.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.FlowCaseTestCase;

public interface FlowCaseTestCaseRepository extends JpaRepository<FlowCaseTestCase, Integer> {

}
