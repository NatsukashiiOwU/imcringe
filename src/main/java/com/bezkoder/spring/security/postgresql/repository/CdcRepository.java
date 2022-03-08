package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Cdc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CdcRepository extends JpaRepository<Cdc, Long> {

	@Query("SELECT u FROM Cdc u WHERE u.cdcUniversityName = ?1")
	public Cdc findByCdcUniversityName(String cdcUnivercityName);

    public boolean existsByCdcName(String cdcUnivercityName);
}
