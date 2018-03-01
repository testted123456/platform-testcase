package com.nonobank.testcase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SystemCfg {
	@Id
	@GeneratedValue
	Integer id;

	@Column(nullable = false, columnDefinition = "varchar(20) COMMENT '系统名称'")
	String system;

	@Column(nullable = false, columnDefinition = "varchar(20) COMMENT 'git地址'")
	String gitAddress;

	@Column(nullable = false, columnDefinition = "varchar(20) COMMENT '系统别名'")
	String alias;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGitAddress() {
		return gitAddress;
	}

	public void setGitAddress(String gitAddress) {
		this.gitAddress = gitAddress;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}
