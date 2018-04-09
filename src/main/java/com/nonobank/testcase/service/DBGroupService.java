package com.nonobank.testcase.service;

import java.util.List;
import com.nonobank.testcase.entity.DBGroup;

public interface DBGroupService {

	DBGroup findById(Integer id);
	
	DBGroup findByGroupName(String groupName);
	
	List<DBGroup> findAll();
	
	DBGroup add(DBGroup dbGroup);
	
	void delete(DBGroup dbGroup);
}
