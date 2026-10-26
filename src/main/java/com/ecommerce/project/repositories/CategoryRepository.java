package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

//the repository works on the object entity with the id
//JPA repository has crud functionalities that we can use readily
//no sql needed since hibernate will take care of it
public interface CategoryRepository extends JpaRepository<Category,Long> {

    //custom method to find category by name
    //Just need camel case and JPA will understand
    Category findByCategoryName(String categoryName);
}
