package com.example.skinet.controller;

import com.example.skinet.core.entity.Product;
import com.example.skinet.core.entity.ProductBrand;
import com.example.skinet.core.entity.ProductType;
import com.example.skinet.service.ProductBrandService;
import com.example.skinet.service.ProductService;
import com.example.skinet.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductTypeService typeService;
    private final ProductBrandService brandService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
        return ResponseEntity.of(productService.getProduct(id));
    }

    @GetMapping("/brands")
    public ResponseEntity<List<ProductBrand>> getProductBrands() {
        return ResponseEntity.ok(brandService.getProductBrands());
    }

    @GetMapping("/types")
    public ResponseEntity<List<ProductType>> getProductTypes() {
        return ResponseEntity.ok(typeService.getProductTypes());
    }
}
