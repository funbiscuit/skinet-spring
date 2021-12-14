package com.example.skinet.service;

import com.example.skinet.core.entity.ProductBrand;
import com.example.skinet.repo.ProductBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductBrandService {
    private final ProductBrandRepository brandRepository;

    public List<ProductBrand> getProductBrands() {
        return brandRepository.findAll();
    }

    public void saveBrands(List<ProductBrand> brands) {
        brandRepository.saveAll(brands);
    }

    public void saveBrand(ProductBrand brand) {
        brandRepository.save(brand);
    }

    public long brandsCount() {
        return brandRepository.count();
    }


}
