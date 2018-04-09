package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.DBCfg;

public interface DBCfgService {

	DBCfg findByDbGroupIdAndName(Integer dbGroupId, String name);
	
	List<DBCfg> findAllDBCfg();
	
	DBCfg add(DBCfg dbCfg);
	
	void delete(DBCfg dbCfg);
}
