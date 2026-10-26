package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDTO {
     Long cartItemId;
     CartDTO cart;
     ProductDTO productDTO;
     Integer quantity;
     Double discount;
     Double productPrice;


}
