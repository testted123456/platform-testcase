package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class FlowCase {
	
	@Id
	@GeneratedValue
	Integer id;
	
	@NotNull(message="pId不能为空")	
	Integer pId;
	
	@NotEmpty(message="名称不能为空")
	@Column(nullable=false, columnDefinition="varchar(300) COMMENT 'case名称'")
	String name;
	
	@Column(columnDefinition="varchar(20) COMMENT '运行环境'")
	String env;
	
	@Column(nullable=true, columnDefinition="varchar(500) COMMENT 'case描述'")
	String description;
	
//	@OneToMany(cascade={CascadeType.ALL})
//	@JoinColumn(name="flowCaseId")
//	@Where(clause="optstatus!=2")
//	public List<FlowCaseRelation> flowCaseRelations;
	
	@ManyToMany(cascade={CascadeType.REFRESH})
//	@Where(clause="optstatus!=2")
	public List<TestCase> testCases;
	
	@NotNull
	@Column(nullable=true, columnDefinition="bit(1) COMMENT '0:目录，1:flowCase'")
	Boolean type;
	
	@Column(nullable=true, columnDefinition="varchar(100) COMMENT '创建人'")
	String createdBy;
	
	@Column(nullable=true,columnDefinition="datetime")
	LocalDateTime createdTime;
	
	@Column(nullable=true, columnDefinition="varchar(100) COMMENT '更新人'")
	String updatedBy;
	
	@Column(nullable=true, columnDefinition=" datetime")
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

//	public List<FlowCaseRelation> getFlowCaseRelations() {
//		return flowCaseRelations;
//	}
//
//	public void setFlowCaseRelations(List<FlowCaseRelation> flowCaseRelations) {
//		this.flowCaseRelations = flowCaseRelations;
//	}

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
