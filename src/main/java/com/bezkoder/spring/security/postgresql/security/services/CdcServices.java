package com.bezkoder.spring.security.postgresql.security.services;

import com.bezkoder.spring.security.postgresql.models.Cdc;
import com.bezkoder.spring.security.postgresql.repository.CdcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CdcServices {

	@Autowired
	private CdcRepository repo;
	
	public List<Cdc> listAll() {
		return repo.findAll();
	}

	public Cdc cdcCreate(String cdcUnivercityName) {
		Cdc newCdc = new Cdc();
		newCdc.setUniversityName(cdcUnivercityName);

		return repo.save(newCdc);
	}

	public Long cdcLink(String cdcName) {
		if(repo.existsByCdcName(cdcName))
			cdcCreate(cdcName);

		Cdc cdc = repo.findByCdcUniversityName(cdcName);

		return cdc.getId();
	}

	public boolean cdcVerifyName(String cdcName) {
		Cdc cdc = repo.findByCdcUniversityName(cdcName);

		if(!Objects.equals(cdc, cdcName)) {
			return false;
		}
		return true;
	}

	public boolean cdcVerifyId(Long cdcRelId) {
		if(repo.existsById(cdcRelId));
			return true;
	}

}
