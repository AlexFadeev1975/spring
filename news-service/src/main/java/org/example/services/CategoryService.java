package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.CategoryDto;
import org.example.mappers.NewsMapper;
import org.example.model.Category;
import org.example.model.News;
import org.example.repository.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;



    @Transactional
    public CategoryDto create (CategoryDto dto) {

        return NewsMapper.INSTANCE.categoryToCategoryDto(categoryRepository.save(NewsMapper.INSTANCE.categoryDtoToCategory(dto)));
    }

    public Category findByCategoryName (String categoryName) {

        Category category = categoryRepository.findByCategoryName(categoryName);

        return (category == null)
                ? categoryRepository.save(new Category(categoryName, new ArrayList<News>()))
                : category;
    }

    public List<Category> findAll(Pageable pageable) {

        return categoryRepository.findAll(pageable).getContent();
    }

    public CategoryDto update (CategoryDto dto) {

        Category category = categoryRepository.findById(Long.parseLong(dto.getId())).orElseThrow();

        category.setCategoryName(dto.getCategoryName());

        return NewsMapper.INSTANCE.categoryToCategoryDto(categoryRepository.save(category));
    }

    public void delete (String categoryId) {

       Category category = categoryRepository.findById(Long.parseLong(categoryId)).orElseThrow();

       categoryRepository.delete(category);


    }


}
