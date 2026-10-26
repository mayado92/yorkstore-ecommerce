package com.ecommerce.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//lombook annotations to reduce boilerplate code
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//With @entity we are telling that this class is an entity and it will be mapped to a table in JPA
@Entity(name = "categories")
public class Category {
    //to tell the entity that this is the ID (required)
    @Id
    //to auto generate id values
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    //comes from hibernate validation dependency
    @NotBlank
    @Size(min = 4, message = "Category name must be at least 4 characters")
    private String categoryName;

//
//    public Category(){
//
//    }
//    public Category(Long categoryID, String categoryName) {
//        this.categoryID = categoryID;
//        this.categoryName = categoryName;
//    }
//
//    public Long getCategoryID() {
//        return categoryID;
//    }
//
//    public void setCategoryID(Long categoryID) {
//        this.categoryID = categoryID;
//    }
//
//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//    }
}
