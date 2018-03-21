package com.nonobank.testcase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Env {
	@Id
	@GeneratedValue
	Integer id;

	@Column(nullable=false, columnDefinition="varchar(20) COMMENT '环境名称'")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="dbGroupId", nullable=true)
	DBGroup dbGroup;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DBGroup getDbGroup() {
		return dbGroup;
	}

	public void setDbGroup(DBGroup dbGroup) {
		this.dbGroup = dbGroup;
	}
	
}
