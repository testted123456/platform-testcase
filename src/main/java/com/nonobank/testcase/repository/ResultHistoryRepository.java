package com.nonobank.testcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.ResultHistory;

public interface ResultHistoryRepository extends JpaRepository<ResultHistory, Integer> {

}
