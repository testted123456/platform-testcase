package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Where;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TestCaseInterface implements Cloneable {
	@Id
	@GeneratedValue
	Integer id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "testCaseId", nullable = false)
	@Where(clause="optstatus != 2")
	TestCase testCase;

	Integer interfaceId;

	@Column(nullable = false, columnDefinition = "varchar(300) COMMENT '接口名称'")
	String interfaceName;

	@Column(columnDefinition = "char(1) COMMENT '0:Http;1:Https;2:MQ'")
	Character apiType;

	@Column(columnDefinition = "char(1) COMMENT '0:get，1:post'")
	Character postWay;
	
	String branch;

	String system;

	@Column(nullable = true)
	Integer orderNo;

	@Column(nullable = false, columnDefinition = "varchar(500) COMMENT '测试步骤描述'")
	String step;

	@Column(nullable = false, columnDefinition = "varchar(500) COMMENT '接口URL地址'")
	String urlAddress;

	@Column(columnDefinition = " text")
	String variables;

	@Column(columnDefinition = " text")
	String requestHead;

	@Column(columnDefinition = " text")
	String requestBody;

	@Column(columnDefinition = " text")
	String responseHead;

	@Column(columnDefinition = " text")
	String responseBody;
	
	@Column(columnDefinition = " text")
	String assertions;

	String createdBy;

	@Column(nullable = true, columnDefinition = "datetime")
	LocalDateTime createdTime;

	String updatedBy;

	@Column(nullable = true, columnDefinition = " datetime")
	LocalDateTime updatedTime;

	@Column(nullable = false, columnDefinition = "smallint(1) COMMENT '0:正常，1:已更新，2:已删除'")
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

	public Integer getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Character getApiType() {
		return apiType;
	}

	public void setApiType(Character apiType) {
		this.apiType = apiType;
	}

	public Character getPostWay() {
		return postWay;
	}

	public void setPostWay(Character postWay) {
		this.postWay = postWay;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
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

	public String getCreatedTime() {
			
			if(null != this.createdTime){
				return this.createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			}else{
				return null;
			}
		}
	
	public void setCreatedTime(String createdTime){
		if(null != createdTime){
			this.createdTime = LocalDateTime.parse(createdTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
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

	public String getUpdatedTime() {
		if(null != this.updatedTime){
			return this.updatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}else{
			return null;
		}
	}

	public void setUpdatedTime(String updatedTime) {
		if(null != this.updatedTime){
			LocalDateTime t = LocalDateTime.parse(updatedTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			this.updatedTime = t;
		}
	}
	
	public void setUpdatedTime(LocalDateTime updatedTime){
		this.updatedTime = updatedTime;
	}

	public Short getOptstatus() {
		return optstatus;
	}

	public void setOptstatus(Short optstatus) {
		this.optstatus = optstatus;
	}

	public TestCaseInterfaceFront convert() {
		TestCaseInterfaceFront tcif = new TestCaseInterfaceFront();

		tcif.setId(this.id);
		tcif.setTestCase(this.testCase);
		tcif.setInterfaceId(this.interfaceId);
		tcif.setName(this.interfaceName);
		tcif.setApiType(this.apiType);
		tcif.setPostWay(this.postWay);

//		switch (this.apiType) {
//		case '0':
//			tcif.setApiType("Http");
//			break;
//		case '1':
//			tcif.setApiType("Https");
//			break;
//		case '2':
//			tcif.setApiType("MQ");
//			break;
//		default:
//			break;
//		}
//
//		switch (this.postWay) {
//		case '0':
//			tcif.setPostWay("get");
//			break;
//		case '1':
//			tcif.setPostWay("post");
//			break;
//		default:
//			break;
//		}
 
		tcif.setBranch(this.branch);
		tcif.setSystem(this.system);
		tcif.setOrderNo(this.orderNo);
		tcif.setStep(this.step);
		tcif.setUrlAddress(this.urlAddress);
		tcif.setRequestBody(this.getRequestBody());
		tcif.setResponseBody(this.responseBody);

		if (this.variables != null) {
			tcif.setVariables(JSONArray.parseArray(this.variables));
		}

		if (this.requestHead != null) {
			tcif.setRequestHead(JSONArray.parseArray(this.requestHead));
		}

		if (this.responseHead != null) {
			tcif.setResponseHead(JSONArray.parseArray(this.responseHead));
		}

		if (this.assertions != null) {
			tcif.setAssertions(JSONArray.parseArray(this.assertions));
		}

		tcif.setCreatedBy(this.createdBy);

		if (null != this.createdTime) {
			tcif.setCreatedTime(this.createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
		}

		tcif.setUpdatedBy(this.updatedBy);

		if (null != this.updatedTime) {
			tcif.setUpdatedTime(this.updatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
		}

		tcif.setOptstatus(this.optstatus);

		return tcif;
	}

	public TestCaseInterface clone() throws CloneNotSupportedException {
		TestCaseInterface tci = (TestCaseInterface) super.clone();
		TestCase tc = this.testCase.clone();
		tci.setTestCase(tc);
		return tci;
	}

}
