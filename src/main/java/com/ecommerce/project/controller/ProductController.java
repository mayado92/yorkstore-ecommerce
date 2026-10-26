package com.ecommerce.project.controller;

import com.ecommerce.project.configurations.Constants;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class ProductController {

    //Field injection
    @Autowired
    private ProductService productService;

    @PostMapping("admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO, @PathVariable Long categoryId){
        ProductDTO addedProductDTO = productService.addProduct(productDTO,categoryId);

        return new ResponseEntity<>(addedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam (name="pageNumber",defaultValue = Constants.PAGE_NUMBER) Integer pageNumber ,
                                                          @RequestParam (name = "pageSize", defaultValue = Constants.PAGE_SIZE) Integer pageSize,
                                                          @RequestParam (name = "sortBy", defaultValue = Constants.PRODUCT_SORT_BY) String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_ORDER) String sortOrder
    ){
        ProductResponse response = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId, @RequestParam (name="pageNumber",defaultValue = Constants.PAGE_NUMBER) Integer pageNumber ,
                                                                 @RequestParam (name = "pageSize", defaultValue = Constants.PAGE_SIZE) Integer pageSize,
                                                                 @RequestParam (name = "sortBy", defaultValue = Constants.CATEGORY_SORT_BY) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_ORDER) String sortOrder){
        ProductResponse response = productService.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("public/products/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword, @RequestParam (name="pageNumber",defaultValue = Constants.PAGE_NUMBER) Integer pageNumber ,
                                                                @RequestParam (name = "pageSize", defaultValue = Constants.PAGE_SIZE) Integer pageSize,
                                                                @RequestParam (name = "sortBy", defaultValue = Constants.PRODUCT_SORT_BY) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = Constants.SORT_ORDER) String sortOrder){
        ProductResponse response = productService.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("admin/products/update/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO newProductDTO, @PathVariable Long productId){
        ProductDTO updatedProductDTO = productService.updateProduct(newProductDTO,productId);
        return new ResponseEntity<>(updatedProductDTO,HttpStatus.OK);
    }

    @DeleteMapping("admin/products/delete/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO updatedProductDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(updatedProductDTO,HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
