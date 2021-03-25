package com.easy.canal.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Activity  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1589202981138271356L;
	private Long id;
	private Timestamp createTimestamp = new Timestamp(System.currentTimeMillis());
	private Timestamp updateTimestamp;
	/**
	 * 活动编号
	 */
	private String code;

	/**
	 * 活动名称
	 */
	private String name;

	/**
	 * 活动描述
	 */
	private String description;

	/**
	 * 活动LOGO
	 */
	private String activityLogo;

	/**
	 * SEO title
	 */
	private String seoTitle;

	/**
	 * SEO keywords
	 */
	private String seoDescription;

	/**
	 * SEO description
	 */
	private String seoKeywords;

	/**
	 * 活动开始时间
	 */
	private Date beginTimestamp;

	/**
	 * 活动结束时间
	 */
	private Date endTimestamp;

	/**
	 * 状态枚举
	 */
//	private IStatus status;

	/**
	 * 内容(富文本)
	 */
	private String content;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActivityLogo() {
		return activityLogo;
	}

	public void setActivityLogo(String activityLogo) {
		this.activityLogo = activityLogo;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public Date getBeginTimestamp() {
		return beginTimestamp;
	}

	public Date getEndTimestamp() {
		return endTimestamp;
	}

	public void setBeginTimestamp(Date beginTimestamp) {
		this.beginTimestamp = beginTimestamp;
	}

	public void setEndTimestamp(Date endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

//	public IStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(IStatus status) {
//		this.status = status;
//	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
}
