package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CategoryService {
    public CategoryResponse getCategories(Integer PageNumber,Integer PageSize,String sortBy, String sortOrder);
    public CategoryDTO createCategory(CategoryDTO categoryDTO);
    public CategoryDTO deleteCategory(Long categoryId);
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
