package com.nonobank.testcase.service.impl;

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

}
