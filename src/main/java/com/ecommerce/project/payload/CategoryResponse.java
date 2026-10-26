package com.ecommerce.project.payload;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

// CategoryResponse is used to send a list of CategoryDTO to the client.
// Its a wrapper class for List<CategoryDTO>
public class CategoryResponse {
    public List<CategoryDTO> content;
    public Integer pageNumber;
    public Integer pageSize;
    public Long totalElements;
    public Integer totalPages;
    public boolean lastPage;

}
