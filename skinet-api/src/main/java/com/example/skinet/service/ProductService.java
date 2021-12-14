package com.example.skinet.service;

import com.example.skinet.core.entity.Product;
import com.example.skinet.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<Product> getProduct(Integer id) {
        return productRepository.findById(id);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public long productsCount() {
        return productRepository.count();
    }
}
