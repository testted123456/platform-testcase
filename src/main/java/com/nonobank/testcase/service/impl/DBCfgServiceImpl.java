package com.nonobank.testcase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.entity.DBCfg;
import com.nonobank.testcase.repository.DBCfgRepository;
import com.nonobank.testcase.service.DBCfgService;

@Service
public class DBCfgServiceImpl implements DBCfgService {
	
	@Autowired
	DBCfgRepository dbCfgRepository;

	@Override
	public DBCfg findByDbGroupIdAndName(Integer dbGroupId, String name) {
		return dbCfgRepository.findByDbGroupIdAndName(dbGroupId, name);
	}

	@Override
	public List<DBCfg> findAllDBCfg() {
		return dbCfgRepository.findAll();
	}

	@Override
	public DBCfg add(DBCfg dbCfg) {
		return dbCfgRepository.save(dbCfg);
	}

	@Override
	public void delete(DBCfg dbCfg) {
		dbCfgRepository.delete(dbCfg);
	}

}
