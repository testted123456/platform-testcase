package com.nonobank.testcase.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import com.alibaba.fastjson.JSONArray;

public class TestCaseInterfaceFront {

	Integer id;

	TestCase testCase;

	@NotNull(message = "接口id不能为空")
	Integer interfaceId;

	@NotNull(message = "orderNo不能为空")
	Integer orderNo;

	@NotEmpty(message = "step不能为空")
	String step;

	String name;

	Character apiType;

	Character postWay;

	String branch;

	String system;

	@NotEmpty(message = "urlAddress不能为空")
	String urlAddress;

	JSONArray variables;

	JSONArray requestHead;

	String requestBody;

	JSONArray responseHead;

	String responseBody;

	JSONArray assertions;

	String createdBy;

	String createdTime;

	String updatedBy;

	String updatedTime;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Short getOptstatus() {
		return optstatus;
	}

	public void setOptstatus(Short optstatus) {
		this.optstatus = optstatus;
	}

	public TestCaseInterface convert() {
		TestCaseInterface tci = new TestCaseInterface();

		tci.setId(this.id);
		tci.setTestCase(this.testCase);
		tci.setInterfaceId(this.interfaceId);
		tci.setInterfaceName(this.name);
		tci.setApiType(this.apiType);
		tci.setPostWay(this.postWay);

//		switch (this.apiType) {
//		case "Http":
//			tci.setApiType('0');
//			break;
//		case "Https":
//			tci.setApiType('1');
//			break;
//		case "MQ":
//			tci.setApiType('2');
//			break;
//
//		default:
//			break;
//		}
//
//		switch (this.postWay) {
//		case "get":
//			tci.setPostWay('0');
//			break;
//		case "post":
//			tci.setPostWay('1');
//			break;
//		default:
//			break;
//		}

		tci.setBranch(this.branch);
		tci.setSystem(this.system);
		tci.setOrderNo(this.orderNo);
		tci.setStep(this.step);
		tci.setUrlAddress(this.urlAddress);

		if (this.variables != null) {
			tci.setVariables(this.variables.toJSONString());
		}

		if (this.requestHead != null) {
			tci.setRequestHead(this.requestHead.toJSONString());
		}

		if (this.responseHead != null) {
			tci.setResponseHead(this.responseHead.toJSONString());
		}

		if (this.assertions != null) {
			tci.setAssertions(this.assertions.toJSONString());
		}

		tci.setRequestBody(this.requestBody);
		tci.setResponseBody(this.responseBody);
		tci.setOptstatus(this.optstatus);

		return tci;
	}
}
