package com.ecommerce.project.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    //one product could be in multiple cartItems
    @OneToMany(mappedBy = "product", cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<CartItem> productInCartItems = new ArrayList<>();

}
