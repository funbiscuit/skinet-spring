package com.example.skinet.repo;

import com.example.skinet.core.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> getProducts(Pageable pageable, ProductQueryParams params);

}
