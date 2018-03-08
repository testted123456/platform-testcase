package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.alibaba.fastjson.JSONArray;

public class TestCaseInterfaceFront {
	
	Integer id;

	TestCase testCase;

	@NotNull(message="接口id不能为空")
	Integer interfaceid;

	@NotNull(message="orderNo不能为空")
	Integer orderNo;

	@NotEmpty(message="step不能为空")
	String step;

	@NotEmpty(message="urlAddress不能为空")
	String urlAddress;

	JSONArray variables;

	JSONArray requestHead;

	String requestBody;

	JSONArray responseHead;

	String responseBody;

	JSONArray assertions;

	String createdBy;

	LocalDateTime createdTime;

	String updatedBy;

	LocalDateTime updatedTime;

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

	public JSONArray getVariables() {
		return variables;
	}

	public void setVariables(JSONArray variables) {
		this.variables = variables;
	}

	public JSONArray getRequestHead() {
		return requestHead;
	}

	public void setRequestHead(JSONArray requestHead) {
		this.requestHead = requestHead;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public JSONArray getResponseHead() {
		return responseHead;
	}

	public void setResponseHead(JSONArray responseHead) {
		this.responseHead = responseHead;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public JSONArray getAssertions() {
		return assertions;
	}

	public void setAssertions(JSONArray assertions) {
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
	
	public TestCaseInterface convert(){
		TestCaseInterface tci = new TestCaseInterface();
		
		tci.setId(this.id);
		tci.setTestCase(this.testCase);
		tci.setInterfaceid(this.interfaceid);
		tci.setOrderNo(this.orderNo);
		tci.setStep(this.step);
		tci.setUrlAddress(this.urlAddress);
		
		if(this.variables != null){
			tci.setVariables(this.variables.toJSONString());
		}
		
		if(this.requestHead != null){
			tci.setRequestHead(this.requestHead.toJSONString());
		}
		
		if(this.responseHead != null){
			tci.setResponseHead(this.responseHead.toJSONString());
		}
		
		if(this.assertions != null){
			tci.setAssertions(this.assertions.toJSONString());
		}
		
		tci.setCreatedBy(this.createdBy);
		
		tci.setCreatedTime(this.createdTime);
		
		tci.setUpdatedBy(this.updatedBy);
		
		tci.setUpdatedTime(this.updatedTime);
		
		tci.setOptstatus(this.optstatus);
		
		return tci;
		
	}
}
