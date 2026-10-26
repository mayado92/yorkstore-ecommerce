package com.ecommerce.project.controller;

import com.ecommerce.project.configurations.Constants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/public/categories")
    //@request parameter is what is sent from the client to query page number and size
    public ResponseEntity<CategoryResponse> getCategories(@RequestParam (name="pageNumber",defaultValue = Constants.PAGE_NUMBER) Integer PageNumber ,
                                                          @RequestParam (name = "pageSize", defaultValue = Constants.PAGE_SIZE) Integer PageSize,
                                                          @RequestParam (name = "sortBy", defaultValue = Constants.CATEGORY_SORT_BY) String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_ORDER) String sortOrder
                                                          )
    {
        return new ResponseEntity<>(categoryService.getCategories(PageNumber, PageSize, sortBy,sortOrder),HttpStatus.OK);
    }

    //
    @PostMapping("/public/echo")
    public ResponseEntity<String> echo(@RequestParam(value="message", defaultValue = "hello") String message){
        return new ResponseEntity<>(message,HttpStatus.OK);

    }

    //@valid is used to validate the request body based on the constraints defined in the Category model
    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>("Category added sucessfully",HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
            CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);

    }
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
            CategoryDTO newCategoryDTO = categoryService.updateCategory(categoryDTO,categoryId);
            return new ResponseEntity<>(newCategoryDTO,HttpStatus.OK);



    }
}
