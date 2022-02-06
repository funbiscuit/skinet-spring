package com.example.skinet.controller;

import com.example.skinet.aop.CacheResponse;
import com.example.skinet.core.entity.Product;
import com.example.skinet.core.entity.ProductBrand;
import com.example.skinet.core.entity.ProductDTO;
import com.example.skinet.core.entity.ProductType;
import com.example.skinet.error.ApiException;
import com.example.skinet.error.ErrorResponse;
import com.example.skinet.repo.ProductQueryParams;
import com.example.skinet.service.ProductBrandService;
import com.example.skinet.service.ProductService;
import com.example.skinet.service.ProductTypeService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProductController {

    private final ProductService productService;
    private final ProductTypeService typeService;
    private final ProductBrandService brandService;
    private final ModelMapper modelMapper;

    @CacheResponse
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) Integer brandId,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) String search
    ) {
        ProductQueryParams params = new ProductQueryParams(brandId, typeId, search);
        Page<Product> products = productService.getProducts(pageable, params);
        return ResponseEntity.ok(new PageImpl<>(products
                .stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList()),
                pageable, products.getTotalElements()));
    }

    @CacheResponse
    @ApiResponse(responseCode = "200", description = "Product is found",
            content = {@Content(schema = @Schema(implementation = ProductDTO.class))})
    @ApiResponse(responseCode = "400", description = "Invalid id supplied",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProduct(id)
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND)));
    }

    @CacheResponse
    @GetMapping("/brands")
    public ResponseEntity<List<ProductBrand>> getProductBrands() {
        return ResponseEntity.ok(brandService.getProductBrands());
    }

    @CacheResponse
    @GetMapping("/types")
    public ResponseEntity<List<ProductType>> getProductTypes() {
        return ResponseEntity.ok(typeService.getProductTypes());
    }
}
