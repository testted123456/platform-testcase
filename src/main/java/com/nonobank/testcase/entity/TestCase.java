package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TestCase {
	@Id
	@GeneratedValue
	Integer id;
	
	Integer pId;
	
	@Column(nullable=false, columnDefinition="varchar(300) COMMENT 'case名称'")
	String name;
	
	@Column(nullable=false, columnDefinition="varchar(500) COMMENT 'case描述'")
	String description;
	
	@Column(nullable=false, columnDefinition="bit(1) COMMENT '0:目录，1:testCase'")
	Boolean type;
	
	@Column(columnDefinition="varchar(20) COMMENT '运行环境'")
	String env;
	
	@Column(columnDefinition="varchar(100) COMMENT '项目'")
	String projectName;
	
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
