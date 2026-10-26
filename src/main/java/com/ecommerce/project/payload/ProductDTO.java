package com.ecommerce.project.payload;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {

    private Long productId;
    private String productName;
    private String image;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

}
