package com.ecommerce.project.controller;

import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.utils.AuthUtil;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    CartRepository cartRepository;

    @PostMapping("/public/product/{productId}/quantity/{quantity}")
    private ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity){
            CartDTO cartDTO = cartService.addProductToCart(productId, quantity);

            return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/admin/carts")
    private ResponseEntity<List<CartDTO>> getAllCarts(){
        List<CartDTO> cartDTOList = cartService.getAllCarts();

        return new ResponseEntity<List<CartDTO>>(cartDTOList,HttpStatus.OK);

    }

    @GetMapping("/public/user/cart")
    private ResponseEntity<CartDTO> getUserCart(){
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getUserCart(email,cartId);

        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }

    //update cart product
    @PutMapping("/public/cart/products/{productId}/quantity/{operation}")
    private ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId, @PathVariable String operation){
        int operationValue = 1;
        if(operation.equalsIgnoreCase("delete")){
            operationValue = -1;
        }
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Long cartId = cart.getCartId();

        CartDTO cartDTO = cartService.updateCartProduct(productId,operationValue,cartId);

        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("/public/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
