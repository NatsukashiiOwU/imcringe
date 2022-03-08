package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
	@Query("SELECT u FROM News u WHERE u.article = ?1")
	public News findByArticle(String article);

	@Query(value = "SELECT * FROM News u WHERE u.cdc_id = ?1",
			nativeQuery = true)
	public List<News> findAllByCdcId(Long id);
}
