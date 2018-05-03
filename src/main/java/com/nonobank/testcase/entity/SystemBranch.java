package com.nonobank.testcase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SystemBranch {

	@Id
	@GeneratedValue
	Integer id;

	@Column(nullable = false, columnDefinition = "varchar(20) COMMENT '系统名称'")
	String system;

	@Column(nullable = false, columnDefinition = "varchar(50) COMMENT '系统分支'")
	String branch;
	
	@Column(nullable = true, columnDefinition = "varchar(50) COMMENT '系统分支版本'")
    String version;
	
	@Column(nullable = true, columnDefinition = "bit(1) COMMENT '是否是最新分支'")
	Boolean last;
	
	@Column(nullable = true, columnDefinition = "bit(1) COMMENT '是否已经静态代码检测过,0:未检测，1:已检测'")
	Boolean codeChecked;
	
	@Column(nullable=false, columnDefinition="smallint(1) COMMENT '0:正常，1:已更新，2:已删除,3:同步中，4:同步成功，5:同步失败'")
	Short optstatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean getLast() {
		return last;
	}

	public void setLast(Boolean last) {
		this.last = last;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Short getOptstatus() {
		return optstatus;
	}

	public void setOptstatus(Short optstatus) {
		this.optstatus = optstatus;
	}
}
