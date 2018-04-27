package com.nonobank.testcase.service;


import java.util.List;

import com.nonobank.testcase.entity.ResultHistory;

public interface ResultHistoryService {
	
	public ResultHistory add(ResultHistory resultHistory);
	
	public ResultHistory add(Integer groupId, Integer tcId, String tcIds, String apiIds, Integer totalSize);
	
	public ResultHistory findLastByTcIdAndTcType(Integer id, Character tcType);
	
	public List<ResultHistory> findLast10ByGroupId(Integer id);

}
