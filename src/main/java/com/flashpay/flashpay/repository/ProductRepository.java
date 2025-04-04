package com.flashpay.flashpay.repository;

import com.flashpay.flashpay.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}