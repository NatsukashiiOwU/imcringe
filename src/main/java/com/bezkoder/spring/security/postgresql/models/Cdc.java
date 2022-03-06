package com.bezkoder.spring.security.postgresql.models;

import javax.persistence.*;

@Entity
@Table(name = "cdc")
public class Cdc {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String cdcName;

	@Column
	private String cdcUniversityName;

	@Column
	private String cdcRegion;

	@Column
	private String cdcCity;

	@Column
	private String cdcAdress;

	@Column
	private String cdcAdminName;

	@Column
	private String cdcAvatar;

	@Column
	private String cdcBanner;

	@Column
	private String cdcLogo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCdcName() {
		return cdcName;
	}

	public void setCdcName(String cdcName) {
		this.cdcName = cdcName;
	}

	public String getUniversityName() {
		return cdcUniversityName;
	}

	public void setUniversityName(String universityName) {
		this.cdcUniversityName = universityName;
	}

	public String getCdcUniversityName() {
		return cdcUniversityName;
	}

	public void setCdcUniversityName(String cdcUniversityName) {
		this.cdcUniversityName = cdcUniversityName;
	}

	public String getCdcRegion() {
		return cdcRegion;
	}

	public void setCdcRegion(String cdcRegion) {
		this.cdcRegion = cdcRegion;
	}

	public String getCdcCity() {
		return cdcCity;
	}

	public void setCdcCity(String cdcCity) {
		this.cdcCity = cdcCity;
	}

	public String getCdcAdress() {
		return cdcAdress;
	}

	public void setCdcAdress(String cdcAdress) {
		this.cdcAdress = cdcAdress;
	}

	public String getCdcAdminName() {
		return cdcAdminName;
	}

	public void setCdcAdminName(String cdcAdminName) {
		this.cdcAdminName = cdcAdminName;
	}

	public String getCdcAvatar() {
		return cdcAvatar;
	}

	public void setCdcAvatar(String cdcAvatar) {
		this.cdcAvatar = cdcAvatar;
	}

	public String getCdcBanner() {
		return cdcBanner;
	}

	public void setCdcBanner(String cdcBanner) {
		this.cdcBanner = cdcBanner;
	}

	public String getCdcLogo() {
		return cdcLogo;
	}

	public void setCdcLogo(String cdcLogo) {
		this.cdcLogo = cdcLogo;
	}
}
