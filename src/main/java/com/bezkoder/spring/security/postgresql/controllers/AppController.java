package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Cdc;
import com.bezkoder.spring.security.postgresql.models.News;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.CdcRepository;
import com.bezkoder.spring.security.postgresql.security.services.NewsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.security.postgresql.repository.NewsRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class AppController {

    private final NewsRepository newsRepository;
    private final CdcRepository cdcRepository;

    AppController(NewsRepository newsRepository, CdcRepository cdcRepository) {
        this.newsRepository = newsRepository;
        this.cdcRepository = cdcRepository;
    }

    @Autowired
    private NewsServices newsService;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/news")
    List<News> all() {
        return newsRepository.findAll();
    }

    @GetMapping("/news/cdc/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    List<News> cdcNews(@PathVariable Long id) {
        return (newsRepository.findAllByCdcId(id));
    }

    @GetMapping("/cdc/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    Optional<Cdc> getCdc(@PathVariable Long id) {
        return cdcRepository.findById(id);
    }

    @PostMapping("/news") // TODO add login verification
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    News newNews(@RequestBody News newNews) {
        return newsRepository.save(newNews);
    }

    @PutMapping("/news/{id}")
    News replaceNews(@RequestBody News newNews, @PathVariable Long id) {

        return newsRepository.findById(id)
                .map(news -> {
                    news.setViewcount(newNews.getViewcount());
                    news.setState(newNews.getState());
                    news.setDate(newNews.getDate());
                    news.setArticle(newNews.getArticle());
                    news.setContents(newNews.getContents());
                    return newsRepository.save(news);
                })
                .orElseGet(() -> {
                    newNews.setId(id);
                    return newsRepository.save(newNews);
                });
    }


    @DeleteMapping("/news/{id}")
    void deleteNews(@PathVariable Long id) {
        newsRepository.deleteById(id);
    }

}
