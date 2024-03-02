package org.example.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.NewsDto;
import org.example.dto.NewsDtoComCount;
import org.example.mappers.NewsMapper;
import org.example.model.News;
import org.example.model.User;
import org.example.repository.NewsRepository;
import org.example.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final CategoryService categoryService;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    private final NewsMapper mapper;

    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    public NewsDto create(NewsDto dto) {

        dto.setCreatedTime(LocalDateTime.now());
        dto.setUpdatedTime(LocalDateTime.now());

        dto.setCategory(categoryService.findByCategoryName(dto.getCategoryName()));

        dto.setUserId(getUserIdFromPrincipal().toString());

        return mapper.newsToNewsDto(newsRepository.save(mapper.newsDtoToNews(dto)));


    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public List<NewsDtoComCount> findAll(Pageable pageable) {

        Page<News> news = newsRepository.findAll(pageable);

        List<News> newsList = news.getContent();
        return newsList.stream().map(mapper::newsToNewsDtoComCount).toList();

    }

    @PreAuthorize("#userId == authentication.principal.id")
    public NewsDto update(NewsDto dto, Long userId) {

        News news = newsRepository.findById(Long.parseLong(dto.getId())).orElseThrow();

        news.setUpdatedTime(LocalDateTime.now());
        news.setText(dto.getText());
        news.setCategory(categoryService.findByCategoryName(dto.getCategoryName()));


        return mapper.newsToNewsDto(newsRepository.save(news));
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or (hasRole('ROLE_USER') and #userId == authentication.principal.id)")
    public void delete(Long id, Long userId) {

        News news = newsRepository.findById(id).orElseThrow();
        newsRepository.delete(news);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public NewsDto findById(long id) {

        News news = newsRepository.findById(id).orElseThrow();

        return mapper.newsToNewsDto(news);
    }

    private Long getUserIdFromPrincipal() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        var authentication = (Authentication) request.getUserPrincipal();
        var userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return user.getId();

    }

    public Long getUserIdFromNews(String id) {

        NewsDto news = findById(Long.parseLong(id));

        return Long.parseLong(news.getUserId());
    }
}
