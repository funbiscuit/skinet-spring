package com.example.skinet.service;

import com.example.skinet.core.entity.ProductBrand;
import com.example.skinet.core.entity.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JsonDbImporter {
    private final ProductService productService;
    private final ProductTypeService typeService;
    private final ProductBrandService brandService;
    private final ObjectMapper objectMapper;

    private final ResourceReader resourceReader;

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void importData() throws Exception {
        if (brandService.brandsCount() == 0) {
            String brands = resourceReader.readString("classpath:db/data/brands.json");
            importBrands(brands);
        }
        if (typeService.typesCount() == 0) {
            String types = resourceReader.readString("classpath:db/data/types.json");
            importTypes(types);
        }
        if (productService.productsCount() == 0) {
            String products = resourceReader.readString("classpath:db/data/products.json");
            importProducts(products);
        }
    }

    private void importBrands(String encodedBrands) throws Exception {
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, ProductBrand.class);
        List<ProductBrand> brands = objectMapper.readValue(encodedBrands, collectionType);

        brands.forEach(brand -> jdbcTemplate.update(
                "insert into product_brands (id, name) values (?, ?);",
                brand.getId(), brand.getName()));
    }

    private void importTypes(String encodedTypes) throws Exception {
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, ProductType.class);
        List<ProductType> types = objectMapper.readValue(encodedTypes, collectionType);

        types.forEach(type -> jdbcTemplate.update(
                "insert into product_types (id, name) values (?, ?);",
                type.getId(), type.getName()));
    }

    private void importProducts(String encodedTypes) throws Exception {
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, EncodedProduct.class);
        List<EncodedProduct> products = objectMapper.readValue(encodedTypes, collectionType);

        products.forEach(product -> jdbcTemplate.update(
                "insert into products (name, description, picture_url, price, type_id, brand_id) values (?, ?, ?, ?, ?, ?);",
                product.name, product.description, product.pictureUrl,
                product.price, product.typeId, product.brandId));
    }

    private static class EncodedProduct {
        public String name;

        public String description;

        public String pictureUrl;

        public BigDecimal price;

        public int typeId;

        public int brandId;
    }
}
