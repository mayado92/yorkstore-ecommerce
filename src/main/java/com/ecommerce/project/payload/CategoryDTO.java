package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Category Data Transfer Object is used for data transfer between the representation layer and the service layer and vice versa.
//DTO is what is sent to the client and received from the client.
public class CategoryDTO {
    String CategoryName;
    Long CategoryId;
}
