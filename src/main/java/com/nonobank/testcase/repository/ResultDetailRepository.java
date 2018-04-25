package com.nonobank.testcase.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.nonobank.testcase.entity.ResultDetail;
import com.nonobank.testcase.entity.ResultHistory;

public interface ResultDetailRepository extends JpaRepository<ResultDetail, Integer> {

	List<ResultDetail> findByResultHistory(ResultHistory resultHistory);
}
