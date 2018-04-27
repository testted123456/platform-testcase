package com.nonobank.testcase.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ResultHistory {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(nullable = true, columnDefinition = "int(11) COMMENT '执行group时group id'")
	private Integer groupId;

	@Column(nullable = true, columnDefinition = "int(11) COMMENT '执行case时tc id'")
	private Integer tcId;

	@Column(nullable = true, columnDefinition = "varchar(500) COMMENT '执行group时，group中的case列表'")
	private String tcIds;
	
	@Column(nullable = true, columnDefinition = "varchar(500) COMMENT '执行case时，case中的api列表'")
	private String apiIds;

	@Column(nullable = true, columnDefinition = "int(11) COMMENT 'group中的case数量或case中的api数量'")
	private Integer totalSize;
	
	@Column(nullable = true, columnDefinition="char(1) COMMENT '0:case，1:flowCase'")
	Character tcType;

	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime createdTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getTcId() {
		return tcId;
	}

	public void setTcId(Integer tcId) {
		this.tcId = tcId;
	}

	public String getTcIds() {
		return tcIds;
	}

	public void setTcIds(String tcIds) {
		this.tcIds = tcIds;
	}
	
	public String getApiIds() {
		return apiIds;
	}

	public void setApiIds(String apiIds) {
		this.apiIds = apiIds;
	}

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public String getCreatedTime() {

		if (null != this.createdTime) {
			return this.createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} else {
			return null;
		}
	}

	public Character getTcType() {
		return tcType;
	}

	public void setTcType(Character tcType) {
		this.tcType = tcType;
	}

	public void setCreatedTime(String createdTime) {
		if (null != createdTime) {
			this.createdTime = LocalDateTime.parse(createdTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
}
