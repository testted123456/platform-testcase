package com.nonobank.testcase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;
import com.nonobank.testcase.repository.ResultDetailRepository;
import com.nonobank.testcase.service.ResultDetailService;

@Service
public class ResultDetailServiceImpl implements ResultDetailService {
	
	@Autowired
	ResultDetailRepository resultDetailRepository;

	@Override
	public ResultDetail add(ResultDetail resultDetail) {
		if(resultDetail.getResultHistory() != null && resultDetail.getApiId() != null && resultDetail.getTcId() != null){
			return resultDetailRepository.save(resultDetail);
		}
		
		return null;
	}

	@Override
	public List<ResultDetail> findByResultHistory(ResultHistory resultHistory) {
		return resultDetailRepository.findByResultHistory(resultHistory);
	}

}
