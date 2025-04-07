package com.flashpay.flashpay.service;

import com.flashpay.flashpay.domain.Product;
import com.flashpay.flashpay.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(String title, int price, int stock, LocalDateTime startTime) {
        Product product = Product.builder()
                .title(title)
                .price(price)
                .stock(stock)
                .startTime(startTime)
                .createdAt(LocalDateTime.now())
                .build();
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}