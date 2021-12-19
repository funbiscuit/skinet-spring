package com.example.skinet.service;

import com.example.skinet.core.entity.Product;
import com.example.skinet.repo.ProductQueryParams;
import com.example.skinet.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<Product> getProduct(Integer id) {
        return productRepository.findById(id);
    }

    public Page<Product> getProducts(Pageable pageable, ProductQueryParams params) {
        return productRepository.getProducts(pageable, params);
    }

    public long productsCount() {
        return productRepository.count();
    }
}
