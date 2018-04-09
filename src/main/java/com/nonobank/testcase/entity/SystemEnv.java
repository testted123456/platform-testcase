package com.nonobank.testcase.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.aspectj.weaver.tools.Trace;

@Entity
public class SystemEnv {
	
	@Id
	@GeneratedValue
	Integer id;
	
	@ManyToOne
	@JoinColumn(name="systemCfgId", nullable=false)
	private SystemCfg systemCfg;
	
	@ManyToOne
	@JoinColumn(name="envId", nullable=false)
	private Env env;
	
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

	public SystemCfg getSystemCfg() {
		return systemCfg;
	}

	public void setSystemCfg(SystemCfg systemCfg) {
		this.systemCfg = systemCfg;
	}

	public Env getEnv() {
		return env;
	}

	public void setEnv(Env env) {
		this.env = env;
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
