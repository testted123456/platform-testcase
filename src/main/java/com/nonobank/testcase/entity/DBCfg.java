package com.nonobank.testcase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DBCfg {
	
	@Id
	@GeneratedValue
	Integer id;
	
	@ManyToOne
	@JoinColumn(name="dbGroupId", nullable=false)
	DBGroup dbGroup;
	
	@Column(nullable=false, columnDefinition="varchar(20) COMMENT '数据库'")
	String name;
	
	@Column(nullable=false, columnDefinition="varchar(30) COMMENT '数据库ip'")
	String ip;
	
	@Column(nullable=false, columnDefinition="varchar(20) COMMENT '数据库端口'")
	String port;
	
	@Column(nullable=false, columnDefinition="varchar(20) COMMENT '数据库名称'")
	String dbName;
	
	@Column(nullable=false, columnDefinition="varchar(20) COMMENT '数据库用户名'")
	String userName;
	
	@Column(nullable=false, columnDefinition="varchar(20) COMMENT '数据库密码'")
	String password;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DBGroup getDbGroup() {
		return dbGroup;
	}

	public void setDbGroup(DBGroup dbGroup) {
		this.dbGroup = dbGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
