package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.CategoryDto;
import org.example.model.Category;
import org.example.services.CategoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto dto) {

        return categoryService.create(dto);
    }

    @GetMapping("/list")
    public List<Category> getCategories() {

        return categoryService.findAll(PageRequest.of(0, 10));
    }

    @PutMapping("/update")
    public CategoryDto updateCategory(@RequestBody CategoryDto dto) {

        return categoryService.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long categoryId) {

        categoryService.delete(categoryId);

        return ResponseEntity.ok("Category успешно удалена");
    }


}
