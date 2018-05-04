package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class FlowCaseFront {
	
	Integer id;
	
	@NotNull(message="pId不能为空")	
	Integer pId;
	
	@NotEmpty(message="名称不能为空")
	String name;
	
	String env;
	
	String description;
	
	public List<TestCase> testCases;
	
	@NotNull
	Boolean type;
	
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

	public Boolean getType() {
		return type;
	}

	public void setType(Boolean type) {
		this.type = type;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public void setOptstatus(Short optstatus) {
		this.optstatus = optstatus;
	}
}
