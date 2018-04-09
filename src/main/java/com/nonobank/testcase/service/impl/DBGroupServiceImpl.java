package com.nonobank.testcase.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.entity.DBGroup;
import com.nonobank.testcase.repository.DBGroupRepository;
import com.nonobank.testcase.service.DBGroupService;

@Service
public class DBGroupServiceImpl implements DBGroupService {
	
	@Autowired
	DBGroupRepository dbGroupRepository;

	@Override
	public DBGroup findById(Integer id) {
		return dbGroupRepository.findById(id);
	}

	@Override
	public List<DBGroup> findAll() {
		return dbGroupRepository.findAll();
	}

	@Override
	public DBGroup add(DBGroup dbGroup) {
		return dbGroupRepository.save(dbGroup);
	}

	@Override
	public DBGroup findByGroupName(String groupName) {
		return dbGroupRepository.findByGroupName(groupName);
	}

	@Override
	public void delete(DBGroup dbGroup) {
		dbGroupRepository.delete(dbGroup);
	}

}
