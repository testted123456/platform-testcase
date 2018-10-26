package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;
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
	
	@Column(columnDefinition="varchar(20) COMMENT '系统'")
	String system;
	
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
	
	@Column(nullable=true, columnDefinition="bit(1) COMMENT '1:流程接口，0:非流程接口'")
	Boolean caseType;
	
	@OneToMany(mappedBy="testCase", cascade={CascadeType.ALL}, fetch = FetchType.EAGER)
//	@JsonManagedReference
//	@JoinColumn(name="testCaseId")
	@Where(clause="optstatus!=2")
	List<TestCaseInterface> testCaseInterfaces;
	
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
	
	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
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

	public Boolean getCaseType() {
		return caseType;
	}

	public void setCaseType(Boolean caseType) {
		this.caseType = caseType;
	}

	public List<TestCaseInterface> getTestCaseInterfaces() {
		return testCaseInterfaces;
	}

	public void setTestCaseInterfaces(List<TestCaseInterface> testCaseInterfaces) {
		this.testCaseInterfaces = testCaseInterfaces;
	}

	public Short getOptstatus() {
		return optstatus;
	}

	public void setOptstatus(Short optstatus) {
		this.optstatus = optstatus;
	}
	
	public TestCaseFront convert(){
		TestCaseFront testCaseFront = new TestCaseFront();
		
		testCaseFront.setId(this.id);
		testCaseFront.setpId(this.pId);
		testCaseFront.setCaseType(this.caseType);
		testCaseFront.setEnv(this.env);
		testCaseFront.setSystem(this.system);
		testCaseFront.setCreatedBy(this.createdBy);
		testCaseFront.setCreatedTime(this.createdTime);
		testCaseFront.setDescription(this.description);
		testCaseFront.setName(this.name);
		testCaseFront.setType(this.type);
		testCaseFront.setUpdatedBy(this.updatedBy);
		testCaseFront.setUpdatedTime(this.updatedTime);
		testCaseFront.setProjectName(this.projectName);
		
		if(null != this.getTestCaseInterfaces()){
			testCaseFront.setTestCaseInterfaces(this.getTestCaseInterfaces().stream().map(x->{return x.convert();})
					.sorted(Comparator.comparing(TestCaseInterfaceFront::getOrderNo))
					.collect(Collectors.toList()));
		}
		
		testCaseFront.setOptstatus(this.optstatus);
		
		return testCaseFront;
	}
	
	public TestCase clone() throws CloneNotSupportedException{
		return (TestCase)super.clone();
	}
}
