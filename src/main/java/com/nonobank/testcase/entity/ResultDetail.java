package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ResultDetail {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="historyId", nullable=false)
	private ResultHistory resultHistory;
	
	@Column(nullable=false, columnDefinition="int(11) COMMENT 'case id'")
	private Integer tcId;
	
	@Column(nullable=true, columnDefinition="varchar(500) COMMENT '用例名称'")
	private String tcName;
	
	@Column(nullable=false, columnDefinition="int(11) COMMENT 'api id'")
	private Integer apiId;
	
	@Column(nullable=true, columnDefinition="varchar(500) COMMENT '接口名称'")
	private String apiName;
	
	@Column(nullable=true, columnDefinition="varchar(500) COMMENT '接口步骤名称'")
	private String apiStepName;
	
	@Column(nullable=true, columnDefinition="varchar(500) COMMENT '接口地址'")
	private String url;
	
	@Column(nullable=true, columnDefinition="text COMMENT '接口地址'")
	private String variables;
	
	@Column(nullable=true, columnDefinition="text COMMENT '消息头'")
	private String headers;
	
	@Column(nullable=true, columnDefinition="text COMMENT '消息体'")
	private String requestBody;
	
	@Column(nullable=true, columnDefinition="text COMMENT '响应消息'")
	private String responseBody;
	
	@Column(nullable=true, columnDefinition="text COMMENT '实际响应消息'")
	private String actualResponseBody;
	
	@Column(nullable=true, columnDefinition="text COMMENT '接口断言'")
	private String assertions;
	
	@Column(nullable=true, columnDefinition="text COMMENT '接口执行抛出的异常'")
	private String exception;
	
	@Column(nullable=true, columnDefinition="bit(1) COMMENT '0:失败，1:成功'")
	private Boolean result;
	
	private LocalDateTime createdTime;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ResultHistory getResultHistory() {
		return resultHistory;
	}

	public void setResultHistory(ResultHistory resultHistory) {
		this.resultHistory = resultHistory;
	}

	public Integer getTcId() {
		return tcId;
	}

	public void setTcId(Integer tcId) {
		this.tcId = tcId;
	}
	
	public String getTcName() {
		return tcName;
	}

	public void setTcName(String tcName) {
		this.tcName = tcName;
	}

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}
	
	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiStepName() {
		return apiStepName;
	}

	public void setApiStepName(String apiStepName) {
		this.apiStepName = apiStepName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVariables() {
		return variables;
	}

	public void setVariables(String variables) {
		this.variables = variables;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getActualResponseBody() {
		return actualResponseBody;
	}

	public void setActualResponseBody(String actualResponseBody) {
		this.actualResponseBody = actualResponseBody;
	}

	public String getAssertions() {
		return assertions;
	}

	public void setAssertions(String assertions) {
		this.assertions = assertions;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}
	
	public String getCreatedTime() {

		if (null != this.createdTime) {
			return this.createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} else {
			return null;
		}
	}

	public void setCreatedTime(String createdTime) {
		if (null != createdTime) {
			this.createdTime = LocalDateTime.parse(createdTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

}
