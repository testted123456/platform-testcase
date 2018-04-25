package com.nonobank.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.GlobalVariable;

public interface GlobalVariableRepository extends JpaRepository<GlobalVariable, Integer> {

	GlobalVariable findByName(String name);
}
