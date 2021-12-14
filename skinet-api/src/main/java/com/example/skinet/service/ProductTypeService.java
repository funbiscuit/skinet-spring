package com.example.skinet.service;

import com.example.skinet.core.entity.ProductType;
import com.example.skinet.repo.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductTypeService {
    private final ProductTypeRepository typeRepository;

    public List<ProductType> getProductTypes() {
        return typeRepository.findAll();
    }

    public long typesCount() {
        return typeRepository.count();
    }
}
