package com.example.skinet.repo;

import com.example.skinet.core.entity.order.Order;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public List<Order> getUserOrders(String email) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> ordersQuery = cb.createQuery(Order.class);
        Root<Order> root = ordersQuery.from(Order.class);
        // fetch everything in one query
        root.fetch("deliveryMethod");
        root.fetch("orderItems");

        // add filter
        ordersQuery.where(cb.equal(root.get("buyerEmail"), email));

        // apply sorting
        ordersQuery.orderBy(cb.desc(root.get("orderDate")));

        ordersQuery.select(root);

        return entityManager.createQuery(ordersQuery)
                .getResultList();
    }

    @Override
    public Optional<Order> getUserOrderById(String email, Integer id) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> ordersQuery = cb.createQuery(Order.class);
        Root<Order> root = ordersQuery.from(Order.class);
        // fetch everything in one query
        root.fetch("deliveryMethod");
        root.fetch("orderItems");

        ordersQuery.where(cb.equal(root.get("buyerEmail"), email),
                cb.equal(root.get("id"), id));

        ordersQuery.select(root);

        try {
            return Optional.of(entityManager.createQuery(ordersQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
