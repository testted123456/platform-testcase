package com.nonobank.testcase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SystemEnv {
	
	@Id
	@GeneratedValue
	Integer id;
	
	@Column(nullable=false, columnDefinition="varchar(20) COMMENT '系统名称'")
	private String systemName;
	
	@Column(nullable=false, columnDefinition="varchar(20) COMMENT '环境名称'")
	private String envName;
	
	@Column(nullable=false, columnDefinition="varchar(100) COMMENT '环境域名'")
	private String domain;
	
	@Column(nullable=false, columnDefinition="varchar(200) COMMENT '环境dns'")
	private String dns;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}
}
