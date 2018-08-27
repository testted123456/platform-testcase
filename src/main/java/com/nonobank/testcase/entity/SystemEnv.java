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
	
	@Column(nullable=true, columnDefinition="varchar(200) COMMENT '环境dns'")
	private String dns;
	
	@Column(nullable=true, columnDefinition="varchar(255) COMMENT '0:Http;1:Https;2:MQ'")
	String apiType;
	
	@Column(nullable=true, columnDefinition="varchar(255) COMMENT '消息头，json格式，可指定Content-Type等'")
	String head;
	
	//请求消息模板
	@Column(nullable=true, columnDefinition="varchar(255) COMMENT '请求消息格式:addFormKey'")
	String requestTemplate;
	
	//响应消息模板
	@Column(nullable=true, columnDefinition="varchar(255) COMMENT '响应消息格式:0:josn;1:text;2:html;3:xml'")
	String responseTemplate;

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
	

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getRequestTemplate() {
		return requestTemplate;
	}

	public void setRequestTemplate(String requestTemplate) {
		this.requestTemplate = requestTemplate;
	}

	public String getResponseTemplate() {
		return responseTemplate;
	}

	public void setResponseTemplate(String responseTemplate) {
		this.responseTemplate = responseTemplate;
	}
	
}
