package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.NewsDto;
import org.example.dto.NewsDtoComCount;
import org.example.dto.NewsSearchDto;
import org.example.services.NewsSearchService;
import org.example.services.NewsService;
import org.springframework.data.domain.PageRequest;
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
    public NewsDto createNews(@RequestBody @Valid NewsDto dto) {


        return newsService.create(dto);
    }

    @GetMapping("/list")
    public List<NewsDtoComCount> findAllNews() {

        return newsService.findAll(PageRequest.of(0, 10));
    }

    @GetMapping("/find/{id}")
    public NewsDto findNewsById(@PathVariable("id") String id) {

        return newsService.findById(Long.parseLong(id));
    }

    @PutMapping("/update")

    public NewsDto update(@RequestBody @Valid NewsDto dto) {

        return newsService.update(dto, Long.valueOf(dto.getUserId()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @Valid Long newsId) {

        ;
        newsService.delete(newsId, newsService.getUserIdFromNews(newsId.toString()));

        return ResponseEntity.ok("News успешно удалена");
    }

    @GetMapping("/search")
    public List<NewsDto> searchNews(@RequestBody NewsSearchDto dto) {

        return newsSearchService.searchNewsByAuthorAndCategory(dto);
    }
}
