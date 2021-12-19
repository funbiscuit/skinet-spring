package com.example.skinet.repo;

import com.example.skinet.core.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {

}
