package com.example.skinet.service;

import com.example.skinet.core.entity.BasketItem;
import com.example.skinet.core.entity.CustomerBasket;
import com.example.skinet.core.entity.Product;
import com.example.skinet.core.entity.order.*;
import com.example.skinet.repo.DeliveryMethodRepository;
import com.example.skinet.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final DeliveryMethodRepository deliveryMethodRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final BasketService basketService;

    public long getDeliveryMethodsCount() {
        return deliveryMethodRepository.count();
    }

    @Transactional
    public Order createOrder(String buyerEmail,
                             int deliveryMethodId,
                             String basketId,
                             OrderAddress shippingAddress) {
        // get basket from basket repo
        CustomerBasket basket = basketService.getBasket(basketId).orElseThrow();

        // get items from product repo
        List<OrderItem> items = basket.getItems().stream()
                .map(this::mapBasketItemToOrderItem)
                .collect(Collectors.toList());

        // get delivery method
        DeliveryMethod deliveryMethod = deliveryMethodRepository.findById(deliveryMethodId).orElseThrow();

        // calc subtotal
        BigDecimal subtotal = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // create order
        Order order = new Order(items, buyerEmail, shippingAddress, deliveryMethod, subtotal);

        // save to db
        order = orderRepository.save(order);

        // delete basket
        basketService.deleteBasket(basketId);

        return order;
    }

    public List<Order> getUserOrders(String email) {
        return orderRepository.getUserOrders(email);
    }

    public Optional<Order> getOrderById(int id, String buyerEmail) {
        return orderRepository.getUserOrderById(buyerEmail, id);
    }

    public List<DeliveryMethod> getDeliveryMethods() {
        return deliveryMethodRepository.findAll();
    }

    private OrderItem mapBasketItemToOrderItem(BasketItem item) {
        Product product = productService.getProduct(item.getId()).orElseThrow();

        ProductItemOrdered itemOrdered = new ProductItemOrdered(product.getId(), product.getName(), product.getPictureUrl());

        return new OrderItem(itemOrdered, product.getPrice(), item.getQuantity());
    }
}
