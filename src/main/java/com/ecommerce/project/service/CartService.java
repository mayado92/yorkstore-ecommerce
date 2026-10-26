package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getUserCart(String email, Long cartId);

    CartDTO updateCartProduct(Long productId, int operationValue, Long cartId);

    String deleteProductFromCart(Long cartId, Long productId);
}
