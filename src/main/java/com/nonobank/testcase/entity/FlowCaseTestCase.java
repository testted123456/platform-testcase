package com.nonobank.testcase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Where;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class FlowCaseTestCase {
	
	@Id
	@GeneratedValue
	Integer id;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "flowCaseId", nullable = false)
	@Where(clause="optstatus != 2")
	FlowCase flowCase;
	
	Integer testCaseId;
	
	Integer orderNo;
	
	@Column(nullable=false, columnDefinition="smallint(1) COMMENT '0:正常，1:已更新，2:已删除'")
	Short optstatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FlowCase getFlowCase() {
		return flowCase;
	}

	public void setFlowCase(FlowCase flowCase) {
		this.flowCase = flowCase;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Short getOptstatus() {
		return optstatus;
	}

	public void setOptstatus(Short optstatus) {
		this.optstatus = optstatus;
	}

}
