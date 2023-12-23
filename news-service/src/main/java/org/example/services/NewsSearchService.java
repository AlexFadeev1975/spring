package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.CommentDto;
import org.example.dto.NewsDto;
import org.example.dto.NewsSearchDto;
import org.example.mappers.NewsMapper;
import org.example.model.News;
import org.example.repository.NewsRepository;
import org.example.repository.UserRepository;
import org.example.search.Filter;
import org.example.search.NewsFilterBuilder;
import org.example.search.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsSearchService {

    private final UserRepository userRepository;
    private final NewsFilterBuilder filterBuilder;
    private final SpecificationBuilder specificationBuilder;

    private final NewsRepository newsRepository;

    public List<NewsDto> searchNewsByAuthorAndCategory (NewsSearchDto dto) {


    List<Long> authorIds = (dto.getAuthor() != null) ? getUserIdsFromAuthor(dto.getAuthor()) : new ArrayList<>();

    List<Filter> filter = filterBuilder.createFilter(dto, authorIds);

        if (filter.isEmpty()) {

        return new ArrayList<>();
    }

    Specification<News> specification =
            (Specification<News>) specificationBuilder.getSpecificationFromFilters(filter);

    Page<News> pageResult = newsRepository.findAll(specification, PageRequest.of(0,10));

        return NewsMapper.INSTANCE.toListNewsDto(pageResult.getContent());
}

    private List<Long> getUserIdsFromAuthor(String author) {

        if (author.isEmpty()) {
            return new ArrayList<>();
        }

        String[] authorNames = author.split(" ");

        return userRepository.findAllIdsByName(authorNames);


    }

}
