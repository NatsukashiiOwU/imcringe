package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.News;
import com.bezkoder.spring.security.postgresql.payload.request.LoginRequest;
import com.bezkoder.spring.security.postgresql.payload.response.JwtResponse;
import com.bezkoder.spring.security.postgresql.security.services.NewsServices;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.security.postgresql.repository.NewsRepository;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class AppController {

    private final NewsRepository newsRepository;

    AppController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Autowired
    private NewsServices newsService;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/news")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    List<News> all() {
        return newsRepository.findAll();
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
                    news.setCdcRelId(newNews.getCdcRelId());
                    news.setUserRelId(newNews.getUserRelId());
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
