package com.nonobank.testcase.service;

import com.nonobank.testcase.entity.DBCfg;

public interface DBCfgService {

	DBCfg findByDbGroupIdAndName(Integer dbGroupId, String name);
}
