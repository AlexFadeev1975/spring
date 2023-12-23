package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.NewsDto;
import org.example.dto.NewsDtoComCount;
import org.example.dto.NewsSearchDto;
import org.example.dto.UserDto;
import org.example.model.News;
import org.example.services.NewsSearchService;
import org.example.services.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news/api")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    private final NewsSearchService newsSearchService;

    @PostMapping("/create")
    public NewsDto createNews(@RequestBody @Valid NewsDto dto, HttpServletRequest request) {

        String userId = request.getHeader("userId");

        return newsService.create(dto, userId);
    }

    @GetMapping("/list")
    public List<NewsDtoComCount> findAllNews() {

        return newsService.findAll(PageRequest.of(0, 10));
    }

    @PutMapping("/update")
    public NewsDto update(@RequestBody @Valid NewsDto dto) {

        return newsService.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String newsId) {

        newsService.delete(newsId);

        return ResponseEntity.ok("News успешно удалена");
    }

    @GetMapping("/search")
    public List<NewsDto> searchNews(@RequestBody NewsSearchDto dto) {

        return newsSearchService.searchNewsByAuthorAndCategory(dto);
    }
}
