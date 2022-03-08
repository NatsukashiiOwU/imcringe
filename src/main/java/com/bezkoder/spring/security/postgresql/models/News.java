package com.bezkoder.spring.security.postgresql.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "news")
public class News {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long cdc_id;

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

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "news_images",
			joinColumns = @JoinColumn(name = "news_id"),
			inverseJoinColumns = @JoinColumn(name = "image_id"))
	private Set<Images> images = new HashSet<>();

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

	public Long getCdc_id() {
		return cdc_id;
	}

	public void setCdc_id(Long cdc_id) {
		this.cdc_id = cdc_id;
	}

	public Set<Images> getImages() {
		return images;
	}

	public void setImages(Set<Images> images) {
		this.images = images;
	}
}
