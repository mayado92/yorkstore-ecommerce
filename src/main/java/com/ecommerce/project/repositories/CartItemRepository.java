package com.ecommerce.project.repositories;

import com.ecommerce.project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("SELECT ci FROM cart_items ci WHERE ci.cart.cartId = ?1 AND ci.product.productId = ?2 ")
    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM cart_items ci WHERE ci.cart.cartId = ?1 AND ci.product.productId = ?2 ")
    void deleteByCartIdAndProductId(Long cartId, Long productId);
}
