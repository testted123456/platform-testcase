package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class TestCase implements Cloneable {
	@Id
	@GeneratedValue
	Integer id;
	
    @NotNull(message="pId不能为空")	
	Integer pId;
	
    @NotEmpty(message="名称不能为空")
	@Column(nullable=false, columnDefinition="varchar(300) COMMENT 'case名称'")
	String name;
	
	@Column(nullable=true, columnDefinition="varchar(500) COMMENT 'case描述'")
	String description;
	
	@NotNull
	@Column(nullable=true, columnDefinition="bit(1) COMMENT '0:目录，1:testCase'")
	Boolean type;
	
	@Column(columnDefinition="varchar(20) COMMENT '运行环境'")
	String env;
	
	@Column(nullable=true, columnDefinition="varchar(100) COMMENT '项目'")
	String projectName;
	
	@Column(nullable=true, columnDefinition="varchar(100) COMMENT '创建人'")
	String createdBy;
	
	@Column(nullable=true,columnDefinition="datetime")
	LocalDateTime createdTime;
	
	@Column(nullable=true, columnDefinition="varchar(100) COMMENT '更新人'")
	String updatedBy;
	
	@Column(nullable=true, columnDefinition=" datetime")
	LocalDateTime updatedTime;
	
	@Column(nullable=true, columnDefinition="bit(1) COMMENT '0:流程接口，1:非流程接口'")
	Boolean caseType;
	
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

	public String getUpdatedTime() {
		if(null != this.updatedTime){
			return this.updatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}else{
			return null;
		}
	}

	public void setUpdatedTime(String updatedTime) {
		LocalDateTime t = LocalDateTime.parse(updatedTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		this.updatedTime = t;
	}
	
	public void setUpdatedTime(LocalDateTime updatedTime){
		this.updatedTime = updatedTime;
	}

	public Boolean getCaseType() {
		return caseType;
	}

	public void setCaseType(Boolean caseType) {
		this.caseType = caseType;
	}

	public Short getOptstatus() {
		return optstatus;
	}

	public void setOptstatus(Short optstatus) {
		this.optstatus = optstatus;
	}
	
	public TestCase clone() throws CloneNotSupportedException{
		return (TestCase)super.clone();
	}
	
	public static void main(String [] args){
		LocalDateTime t = LocalDateTime.parse("2018-03-20 02:08:39", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		System.out.println(t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		
	}

}
