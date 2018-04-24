package com.nonobank.testcase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GlobalVariable {
	
	@Id
	@GeneratedValue
	Integer id;
	
	@Column(nullable=false, columnDefinition="varchar(100) COMMENT '变量名称'")
	private String name;
	
	@Column(nullable=false, columnDefinition="varchar(300) COMMENT '变量值'")
	private String value;
	
	@Column(nullable=true, columnDefinition="varchar(300) COMMENT '变量描述'")
	private String description;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
