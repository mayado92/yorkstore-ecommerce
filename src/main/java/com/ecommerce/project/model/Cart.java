package com.ecommerce.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "carts")
public class Cart {
    @Id
    //tells
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    //each cart has multiple cart items. will be mapped by the cart attribute in cartItems
    @OneToMany(mappedBy = "cart",cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    //each user has one cart
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private double totalPrice = 0.0;
}
