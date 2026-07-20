package com.lec_dev.product_service.service;

import com.lec_dev.product_service.dto.ProductRequest;
import com.lec_dev.product_service.dto.ProductResponse;
import com.lec_dev.product_service.model.Product;
import com.lec_dev.product_service.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        this.productRepository.save(product);
        log.info("Product {} is created", product.getId());

    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = this.productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
            return ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .build();
    }
}
