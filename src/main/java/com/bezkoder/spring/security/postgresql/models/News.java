package com.bezkoder.spring.security.postgresql.models;

import javax.persistence.*;

@Entity
@Table(name = "news")
public class News {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String userRelId;

	@Column
	private String cdcRelId;

	@Column
	private String article;
	
	@Column
	private String date;
	
	@Column
	private String viewcount;

	@Column
	private String contents;
	
	@Column(length = 1)
	private Integer state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getViewcount() {
		return viewcount;
	}

	public void setViewcount(String viewcount) {
		this.viewcount = viewcount;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getState() {	return state; }

	public void setState(Integer state) { this.state = state; }

	public String getCdcRelId() {
		return cdcRelId;
	}

	public void setCdcRelId(String cdcRelId) {
		this.cdcRelId = cdcRelId;
	}

	public String getUserRelId() {
		return userRelId;
	}

	public void setUserRelId(String userRelId) {
		this.userRelId = userRelId;
	}

}
