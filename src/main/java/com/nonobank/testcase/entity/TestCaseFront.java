package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TestCaseFront {
	
	Integer id;

	Integer pId;

	String name;

	String description;

	Boolean type;

	String env;

	String projectName;

	String createdBy;

	LocalDateTime createdTime;

	String updatedBy;

	LocalDateTime updatedTime;

	Boolean caseType;

	List<TestCaseInterfaceFront> testCaseInterfaces;

	Short optstatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getProjectName() {
		return projectName;
	}

	public Boolean getType() {
		return type;
	}

	public void setType(Boolean type) {
		this.type = type;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedTime() {
		if (null != this.updatedTime) {
			return this.updatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} else {
			return null;
		}
	}

	public void setUpdatedTime(String updatedTime) {
		if (null != this.updatedTime) {
			LocalDateTime t = LocalDateTime.parse(updatedTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			this.updatedTime = t;
		}
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Boolean getCaseType() {
		return caseType;
	}

	public void setCaseType(Boolean caseType) {
		this.caseType = caseType;
	}

	public List<TestCaseInterfaceFront> getTestCaseInterfaces() {
		return testCaseInterfaces;
	}

	public void setTestCaseInterfaces(List<TestCaseInterfaceFront> testCaseInterfaces) {
		this.testCaseInterfaces = testCaseInterfaces;
	}

	public Short getOptstatus() {
		return optstatus;
	}

	public void setOptstatus(Short optstatus) {
		this.optstatus = optstatus;
	}

	public TestCase convert() {
		TestCase testCase = new TestCase();
		
		testCase.setId(this.id);
		testCase.setpId(this.pId);
		testCase.setCaseType(this.caseType);
		testCase.setEnv(this.env);
		testCase.setCreatedBy(this.createdBy);
		testCase.setCreatedTime(this.createdTime);
		testCase.setDescription(this.description);
		testCase.setName(this.name);
		testCase.setType(this.type);
		testCase.setUpdatedBy(this.updatedBy);
		testCase.setUpdatedTime(this.updatedTime);
		testCase.setProjectName(this.projectName);
		
		if(this.getTestCaseInterfaces() != null){
			testCase.setTestCaseInterfaces(this.getTestCaseInterfaces().stream().map(x->{return x.convert();}).collect(Collectors.toList()));
		}
		
		testCase.setOptstatus(this.optstatus);
		
		return testCase;
	}
}
