package com.ecommerce.project.payload;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductResponse {
    private List<ProductDTO> content;
    public Integer pageNumber;
    public Integer pageSize;
    public Long totalElements;
    public Integer totalPages;
    public boolean lastPage;
}
