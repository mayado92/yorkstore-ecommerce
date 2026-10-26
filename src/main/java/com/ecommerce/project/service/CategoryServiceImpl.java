package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private List<Category> categories = new ArrayList<>();

    @Autowired
    //This will automatically inject the CategoryRepository bean into this class
    //Category Repository extends JpaRepository which provides CRUD operations
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    //fetch all categories from the database
    //we use DTO to avoid exposing the entity directly to the client
    //Category Response is a wrapper class that contains a list of CategoryDTO
    public CategoryResponse getCategories(Integer PageNumber,Integer PageSize,String sortBy, String sortOrder) {
        //Here we use the sort module from Spring data JPA
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        // Pageable is a Spring Data interface that allows pagination of query results
        Pageable pageDetails = PageRequest.of(PageNumber,PageSize,sortByAndOrder);
        // Retrieve a page of Category entities from the repository
        Page<Category> CategoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = CategoryPage.getContent();
        if (categories.isEmpty())
            throw new APIException("No category created till now.");

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class)).toList();
        CategoryResponse response = new CategoryResponse();
        response.setContent(categoryDTOS);
        //We include this as meta data within the response
        response.setPageNumber(CategoryPage.getNumber());
        response.setPageSize(CategoryPage.getSize());
        response.setTotalPages(CategoryPage.getTotalPages());
        response.setTotalElements(CategoryPage.getTotalElements());
        response.setLastPage(CategoryPage.isLast());

        return response;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category checkCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if (checkCategory != null)
            throw new APIException("Category with the name " + categoryDTO.getCategoryName() + " already exists !");

        //convert CategoryDTO to Category entity to add to the database
        //Then save the entity and convert it back to DTO to return to the client
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category createdCategory = categoryRepository.save(category);
        CategoryDTO createdCategoryDTO = modelMapper.map(createdCategory, CategoryDTO.class);
        return createdCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.stream().
                filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Category","CategoryId", categoryId));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);

    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO newCategoryDTO, Long categoryId) {
        //findbyID returns an optional so u need an or else because it will return null if not exisiting
        Category newCategory = modelMapper.map(newCategoryDTO,Category.class);
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","CategoryId", categoryId));
        category.setCategoryName(newCategory.getCategoryName());
        categoryRepository.save(category);

        return modelMapper.map(category,CategoryDTO.class);
    }




}
