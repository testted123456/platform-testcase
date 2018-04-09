package com.nonobank.testcase.service;


import com.nonobank.testcase.entity.ResultHistory;

public interface ResultHistoryService {
	
	public ResultHistory add(ResultHistory resultHistory);
	
	public ResultHistory add(Integer groupId, Integer tcId, String tcIds, Integer totalSize);

}
