package com.example.skinet.controller;

import com.example.skinet.core.entity.order.DeliveryMethod;
import com.example.skinet.core.entity.order.Order;
import com.example.skinet.core.entity.order.OrderAddress;
import com.example.skinet.dto.CreateOrderDto;
import com.example.skinet.dto.OrderDto;
import com.example.skinet.error.ApiException;
import com.example.skinet.service.AuthService;
import com.example.skinet.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrdersForUser(Principal principal) {
        List<Order> userOrders = orderService.getUserOrders(authService.getUserEmailFromPrincipal(principal));

        List<OrderDto> collect = userOrders.stream()
                .map(o -> modelMapper.map(o, OrderDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderForUser(Principal principal, @PathVariable Integer id) {
        Order order = orderService.getOrderById(id, authService.getUserEmailFromPrincipal(principal))
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Order not found"));
        return ResponseEntity.ok(modelMapper.map(order, OrderDto.class));
    }

    @GetMapping("/delivery-methods")
    public ResponseEntity<List<DeliveryMethod>> getDeliveryMethods() {
        return ResponseEntity.ok(orderService.getDeliveryMethods());
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(Principal principal, @RequestBody CreateOrderDto orderDto) {
        String email = authService.getUserEmailFromPrincipal(principal);

        OrderAddress address = modelMapper.map(orderDto.getShippingAddress(), OrderAddress.class);

        try {
            Order order = orderService.createOrder(email, orderDto.getDeliveryMethodId(),
                    orderDto.getBasketId(), address);

            return ResponseEntity.ok(modelMapper.map(order, OrderDto.class));
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Can't create order");
        }
    }
}
