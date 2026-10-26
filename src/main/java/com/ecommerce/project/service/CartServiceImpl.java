package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.CartItemDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    AuthUtil authUtils;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Check if cart exists
        Cart userCart = createCart();

        //Check if product exist
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "poductId", productId));

        //Find Cart Item
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(userCart.getCartId(),productId);

        //Add validation
        if(cartItem != null){
            throw new APIException("Item already in the cart");
        }
        if(product.getQuantity() == 0){
            throw new APIException("Out of stock");
        }
        if(quantity > product.getQuantity()){
            throw new APIException("We do not have enough stock please reduce your quantity. Current stock available (" + product.getQuantity() + ")");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(userCart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice((int)product.getSpecialPrice());
        cartItemRepository.save(newCartItem);

        //here we can subtract with the quantity we are purchasing too  or acc do it when we place an order
        product.setQuantity(product.getQuantity());
        //total price of cart
        userCart.setTotalPrice(userCart.getTotalPrice() + newCartItem.getProductPrice() * newCartItem.getQuantity());
        //adding cart Item to cart
        userCart.getCartItems().add(newCartItem);
        cartRepository.save(userCart);


        //this is for returnning as DTO
        CartDTO cartDTO = modelMapper.map(userCart,CartDTO.class);

        //we need a list of productDTOs to pass to cartDTO
        List<CartItem> cartItemList = userCart.getCartItems();

        List<ProductDTO> productDTOList = cartItemList.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        }).toList();

        cartDTO.setProductDTOS(productDTOList);

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        //find all the carts
        List<Cart> cartList = cartRepository.findAll();

        // Ste up DTO to send back
        List<CartDTO> cartDTOS = cartList.stream().map(cart -> {
            //to map each cart
            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
            //to map each cartItem in the cart
            List<ProductDTO> productDTOList = cart.getCartItems().stream().map(cartItem -> {
                ProductDTO productDTO = modelMapper.map(cartItem.getProduct(),ProductDTO.class);
                productDTO.setQuantity(cartItem.getQuantity());
                return productDTO;
            }).toList();
            cartDTO.setProductDTOS(productDTOList);
            return cartDTO;
        }).toList();
        return cartDTOS;
    }

    @Override
    public CartDTO getUserCart(String email, Long cartId) {
        //
        Cart cart = cartRepository.findCartByEmailAndCartId(email,cartId);
        if(cart == null){
            throw new ResourceNotFoundException("cart","cartId",cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<ProductDTO> productDTOList = cart.getCartItems().stream().map(cartItem-> {
                ProductDTO productDTO = modelMapper.map(cartItem,ProductDTO.class);
                productDTO.setQuantity(cartItem.getQuantity());
                return productDTO;
        }).toList();

        cartDTO.setProductDTOS(productDTOList);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateCartProduct(Long productId, int operationValue, Long cartId) {
        Product product = productRepository.findById(productId).
                orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));;
        Cart cart = cartRepository.findById(cartId).
                orElseThrow(()->new ResourceNotFoundException("Cart","CartId",cartId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId,productId);


        if(product.getQuantity() == 0){
            throw new APIException("Out of stock");
        }
        if(cartItem.getQuantity() + operationValue > product.getQuantity()){
            throw new APIException("We do not have enough stock please reduce your quantity. Current stock available (" + product.getQuantity() + ")");
        }
        if(cartItem.getQuantity() + operationValue < 0){
            throw new APIException("You cannot choose a item with quantity less that 0");
        }


        cartItem.setQuantity(cartItem.getQuantity() + operationValue);
        cart.setTotalPrice(cart.getTotalPrice() + product.getSpecialPrice() * operationValue);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        Cart updatedCart = cartRepository.save(cart);

        if(updatedCartItem.getQuantity() == 0){
            deleteProductFromCart(cartId,productId);
        }


        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });


        cartDTO.setProductDTOS(productStream.toList());

        return cartDTO;

    }

    @Override
    @Modifying
    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity()));

        //We need to find the Cart item in the repository and we can do that using cart item and product Id
        cartItemRepository.deleteByCartIdAndProductId(cartId,productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
    }

    private Cart createCart(){
        Cart currentCart = cartRepository.findCartByEmail(authUtils.loggedInEmail());
        if(currentCart != null){
            return currentCart;
        }

        Cart newCart = new Cart();
        newCart.setUser(authUtils.loggedInUser());
        newCart.setTotalPrice(0);
        cartRepository.save(newCart);
        return newCart;
    }

}
