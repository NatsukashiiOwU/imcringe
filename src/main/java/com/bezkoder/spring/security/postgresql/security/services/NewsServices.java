package com.bezkoder.spring.security.postgresql.security.services;

import com.bezkoder.spring.security.postgresql.models.News;
import com.bezkoder.spring.security.postgresql.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServices {

	@Autowired
	private NewsRepository repo;
	
	public List<News> listAll() {
		return repo.findAll();
	}



}
