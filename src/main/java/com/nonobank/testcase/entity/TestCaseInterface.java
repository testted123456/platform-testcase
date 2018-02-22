package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class TestCaseInterface {
	@Id
	@GeneratedValue
	Integer id;
	
	@ManyToOne
	@JoinColumn(name="testCaseId", nullable=false)
	TestCase testCase;
	
	Integer interfaceid;
	
	@Column(nullable=true)
	Integer orderNo;
	
	@Column(nullable=false, columnDefinition="varchar(500) COMMENT '测试步骤描述'")
	String step;
	
	String urlAddress;
	
	@Column(columnDefinition=" text")
	String variables;
	
	@Column(columnDefinition=" text")
	String requestHead;
	
	@Column(columnDefinition=" text")
	String requestBody;
	
	@Column(columnDefinition=" text")
	String responseHead;
	
	@Column(columnDefinition=" text")
	String responseBody;
	
	@Column(columnDefinition=" text")
	String assertions;
	
	String createdBy;
	
	@Column(columnDefinition="datetime")
	LocalDateTime createdTime;
	
	String updatedBy;
	
	@Column(columnDefinition=" datetime")
	LocalDateTime updatedTime;
	
	@Column(nullable=false, columnDefinition="smallint(1) COMMENT '0:正常，1:已更新，2:已删除'")
	Short optstatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

	public Integer getInterfaceid() {
		return interfaceid;
	}

	public void setInterfaceid(Integer interfaceid) {
		this.interfaceid = interfaceid;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public String getVariables() {
		return variables;
	}

	public void setVariables(String variables) {
		this.variables = variables;
	}

	public String getRequestHead() {
		return requestHead;
	}

	public void setRequestHead(String requestHead) {
		this.requestHead = requestHead;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponseHead() {
		return responseHead;
	}

	public void setResponseHead(String responseHead) {
		this.responseHead = responseHead;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getAssertions() {
		return assertions;
	}

	public void setAssertions(String assertions) {
		this.assertions = assertions;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Short getOptstatus() {
		return optstatus;
	}

	public void setOptstatus(Short optstatus) {
		this.optstatus = optstatus;
	}

}
