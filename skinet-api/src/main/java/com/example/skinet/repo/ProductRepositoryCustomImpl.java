package com.example.skinet.repo;

import com.example.skinet.core.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<Product> getProducts(Pageable pageable, ProductQueryParams params) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> productQuery = cb.createQuery(Product.class);
        Root<Product> root = productQuery.from(Product.class);
        // fetch brands and types in one query
        root.fetch("brand");
        root.fetch("type");

        // add filters
        List<Predicate> predicates = new ArrayList<>();
        if (params.brandId() != null) {
            predicates.add(cb.equal(root.get("brand"), params.brandId()));
        }
        if (params.typeId() != null) {
            predicates.add(cb.equal(root.get("type"), params.typeId()));
        }
        if (params.search() != null) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + params.search().toLowerCase() + "%"));
        }
        if (!predicates.isEmpty()) {
            productQuery.where(predicates.toArray(new Predicate[0]));
        }

        // apply sorting
        Sort sort = pageable.getSort();
        if (sort.isUnsorted()) {
            sort = Sort.by(Sort.Direction.ASC, "name");
        }
        productQuery.orderBy(sort.stream()
                .map(o -> {
                    Path<Object> sortProp = root.get(o.getProperty());
                    return o.isAscending() ? cb.asc(sortProp) :
                            cb.desc(sortProp);
                })
                .toList());

        productQuery.select(root);
        List<Product> products = entityManager.createQuery(productQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // count total to return inside Page
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Product.class)));
        long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(products, pageable, totalCount);
    }
}
