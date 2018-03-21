package com.nonobank.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.DBGroup;

public interface DBGroupRepository extends JpaRepository<DBGroup, Integer> {

	DBGroup findById(Integer id);
}
