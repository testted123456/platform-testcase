package com.nonobank.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.DBCfg;

public interface DBCfgRepository extends JpaRepository<DBCfg, Integer> {

	DBCfg findByDbGroupIdAndName(Integer dbGroupId, String name);
}
