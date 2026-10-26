package com.ecommerce.project.service;


import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    public ProductDTO addProduct(ProductDTO productDTO,Long categoryId) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category","CategoryId",categoryId));

        Product product = modelMapper.map(productDTO,Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() -
                ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        productRepository.save(product);
        return modelMapper.map(product,ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder){

        //Here we use the sort module from Spring data JPA
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        // Pageable is a Spring Data interface that allows pagination of query results
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        // Retrieve a page of Category entities from the repository

        Page<Product> ProductPage = productRepository.findAll(pageDetails);
        List<Product> productsList = ProductPage.getContent();
        if(productsList.isEmpty()){
            throw  new APIException("No products found!");
        }
        List<ProductDTO> productDTOList = productsList.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse response = new ProductResponse();
        response.setContent(productDTOList);
        response.setContent(productDTOList);
        response.setPageNumber(ProductPage.getNumber());
        response.setPageSize(ProductPage.getSize());
        response.setTotalPages(ProductPage.getTotalPages());
        response.setTotalElements(ProductPage.getTotalElements());
        response.setLastPage(ProductPage.isLast());
        return response;
    }


    public ProductResponse getProductsByCategory(Long categoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category","CategoryId",categoryId));

        //Here we use the sort module from Spring data JPA
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        // Pageable is a Spring Data interface that allows pagination of query results
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        // Retrieve a page of Category entities from the repository

        Page<Product> ProductPage = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> productsList = ProductPage.getContent();

        if(productsList.isEmpty()){
            throw  new APIException("No products found!");
        }
        List<ProductDTO> productDTOList = productsList.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse response = new ProductResponse();
        response.setContent(productDTOList);
        response.setPageNumber(ProductPage.getNumber());
        response.setPageSize(ProductPage.getSize());
        response.setTotalPages(ProductPage.getTotalPages());
        response.setTotalElements(ProductPage.getTotalElements());
        response.setLastPage(ProductPage.isLast());
        return response;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        //Here we use the sort module from Spring data JPA
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        // Pageable is a Spring Data interface that allows pagination of query results
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        // Retrieve a page of Category entities from the repository

        Page<Product> ProductPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%",pageDetails);
        List<Product> productsList = ProductPage.getContent();
        if(productsList.isEmpty()){
            throw  new APIException("No products found!");
        }
        List<ProductDTO> productDTOList = productsList.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse response = new ProductResponse();
        response.setContent(productDTOList);
        response.setPageNumber(ProductPage.getNumber());
        response.setPageSize(ProductPage.getSize());
        response.setTotalPages(ProductPage.getTotalPages());
        response.setTotalElements(ProductPage.getTotalElements());
        response.setLastPage(ProductPage.isLast());
        response.setContent(productDTOList);
        return response;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product productFromDb = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","ProductId",productId));
        Product product = modelMapper.map(productDTO,Product.class);

        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

        productRepository.save(productFromDb);
        ProductDTO newProductDTO = modelMapper.map(productFromDb,ProductDTO.class);
        return newProductDTO;


    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productFromDb = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","ProductId",productId));
        productRepository.delete(productFromDb);

        return modelMapper.map(productFromDb,ProductDTO.class);
    }

    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);
        productFromDb.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }


}
