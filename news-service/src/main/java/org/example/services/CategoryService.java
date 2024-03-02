package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.CategoryDto;
import org.example.mappers.NewsMapper;
import org.example.model.Category;
import org.example.model.News;
import org.example.repository.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final NewsMapper mapper;

    @Transactional
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public CategoryDto create(CategoryDto dto) {

        Category category = mapper.categoryDtoToCategory(dto);

        return mapper.categoryToCategoryDto(categoryRepository.save(category));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Category> findAll(Pageable pageable) {

        return categoryRepository.findAll(pageable).getContent();
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public CategoryDto update(CategoryDto dto) {

        Category category = categoryRepository.findById(Long.parseLong(dto.getId())).orElseThrow();

        category.setCategoryName(dto.getCategoryName());

        return mapper.categoryToCategoryDto(categoryRepository.save(category));
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void delete(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow();

        categoryRepository.delete(category);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public CategoryDto findById(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow();
        return mapper.categoryToCategoryDto(category);
    }

    public Category findByCategoryName(String categoryName) {

        Category category = categoryRepository.findByCategoryName(categoryName);

        return (category == null)
                ? categoryRepository.save(new Category(categoryName, new ArrayList<News>()))
                : category;
    }


}
