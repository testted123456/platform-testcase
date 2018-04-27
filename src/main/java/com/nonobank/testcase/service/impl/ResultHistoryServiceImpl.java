package com.nonobank.testcase.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.repository.ResultHistoryRepository;
import com.nonobank.testcase.service.ResultHistoryService;

@Service
public class ResultHistoryServiceImpl implements ResultHistoryService {
	
	@Autowired
	ResultHistoryRepository resultHistoryRepository;

	@Override
	public ResultHistory add(ResultHistory resultHistory) {
		return resultHistoryRepository.save(resultHistory);
	}

	@Override
	public ResultHistory add(Integer groupId, Integer tcId, String tcIds, String apiIds, Integer totalSize) {
		ResultHistory resultHistory = new ResultHistory();
		resultHistory.setGroupId(groupId);
		resultHistory.setTcId(tcId);
		resultHistory.setTcIds(tcIds);
		resultHistory.setApiIds(apiIds);
		resultHistory.setTotalSize(totalSize);
		resultHistory.setCreatedTime(LocalDateTime.now());
		return add(resultHistory);
	}

	@Override
	public ResultHistory findLastByTcIdAndTcType(Integer id, Character tcType) {
		return resultHistoryRepository.findFirst1ByTcIdAndTcTypeOrderByIdDesc(id, tcType);
	}
	
	public List<ResultHistory> findLast10ByGroupId(Integer id){
		return resultHistoryRepository.findFirst10ByGroupIdOrderByIdDesc(id);
	}

}
