package com.nonobank.testcase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class CodeCover {
	
	@Id
	@GeneratedValue
	Integer id;

	@NotEmpty(message="ip不能为空")
	@Column(nullable=false, columnDefinition="varchar(155) COMMENT 'ip名称'")
	private String ip;
	
	@NotNull(message="port不能为空")
	private Integer port;
	
	@NotEmpty(message="system不能为空")
	@Column(nullable=false, columnDefinition="varchar(255) COMMENT 'system名称'")
	private String system;
	
	@NotEmpty(message="branch不能为空")
	@Column(nullable=false, columnDefinition="varchar(255) COMMENT 'branch名称'")
	private String branch;
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getIp() {
		return ip;
	}
	
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public String getSystem() {
		return system;
	}
	
	public void setSystem(String system) {
		this.system = system;
	}
	
	public String getBranch() {
		return branch;
	}
	
	public void setBranch(String branch) {
		this.branch = branch;
	}
}
