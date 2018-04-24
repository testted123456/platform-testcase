package com.nonobank.testcase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nonobank.testcase.entity.ResultHistory;

public interface ResultHistoryRepository extends JpaRepository<ResultHistory, Integer> {
	
	ResultHistory findFirst1ByTcIdOrderByIdDesc(Integer id);
	
	List<ResultHistory> findFirst10ByGroupIdOrderByIdDesc(Integer id);

}
