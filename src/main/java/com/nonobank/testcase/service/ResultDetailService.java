package com.nonobank.testcase.service;

import java.util.List;

import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;

public interface ResultDetailService {
	
	public ResultDetail add(ResultDetail resultDetail);
	
	public List<ResultDetail> findByResultHistory(ResultHistory resultHistory);

}
