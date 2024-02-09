package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.NewsDto;
import org.example.dto.NewsDtoComCount;
import org.example.mappers.NewsMapper;
import org.example.mappers.NewsMapperImpl;
import org.example.model.News;
import org.example.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final CategoryService categoryService;
    private final NewsRepository newsRepository;

    private final NewsMapper mapper;

    @Transactional
    public NewsDto create(NewsDto dto, Long userId) {

        dto.setCreatedTime(LocalDateTime.now());
        dto.setUpdatedTime(LocalDateTime.now());

        dto.setCategory(categoryService.findByCategoryName(dto.getCategoryName()));

        dto.setUserId(userId.toString());

        return mapper.newsToNewsDto(newsRepository.save(mapper.newsDtoToNews(dto)));


    }

    public List<NewsDtoComCount> findAll(Pageable pageable) {

        Page<News> news = newsRepository.findAll(pageable);

        List<News> newsList = news.getContent();
        return newsList.stream().map(mapper::newsToNewsDtoComCount).toList();

    }

    public NewsDto update(NewsDto dto) {

        News news = newsRepository.findById(Long.parseLong(dto.getId())).orElseThrow();

        news.setUpdatedTime(LocalDateTime.now());
        news.setText(dto.getText());
        news.setCategory(categoryService.findByCategoryName(dto.getCategoryName()));


        return mapper.newsToNewsDto(newsRepository.save(news));
    }

    public void delete(Long id) {

        News news = newsRepository.findById(id).orElseThrow();
        newsRepository.delete(news);
    }

    public NewsDto getNews(Long newsId) {

        News news = newsRepository.findById(newsId).orElseThrow();

        return mapper.newsToNewsDto(news);
    }

}
