package com.example.skinet.service;

import com.example.skinet.config.AppConfigProperties;
import com.example.skinet.core.entity.CustomerBasket;
import com.example.skinet.core.entity.order.DeliveryMethod;
import com.example.skinet.core.entity.order.OrderStatus;
import com.example.skinet.error.ApiException;
import com.example.skinet.repo.DeliveryMethodRepository;
import com.example.skinet.repo.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentUpdateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final BasketService basketService;
    private final ProductService productService;
    private final DeliveryMethodRepository deliveryMethodRepository;
    private final AppConfigProperties appConfigProperties;
    private final OrderRepository orderRepository;

    @PostConstruct
    public void init() {
        if (!isStripeEnabled()) {
            log.warn("Stripe secret key is set to: {}. Stripe is disabled!",
                    appConfigProperties.getStripe().getSecretKey());
            log.warn("Orders with shipping country USA will be successful, " +
                    "UK - failed, all other - pending");
        }
    }

    public CustomerBasket createOrUpdatePaymentIntent(String basketId) {
        CustomerBasket basket = basketService.getBasket(basketId)
                .orElseThrow(() -> ApiException.basketNotFound(basketId));

        long shippingAmount = Optional.ofNullable(basket.getDeliveryMethodId())
                .flatMap(deliveryMethodRepository::findById)
                .map(DeliveryMethod::getPrice)
                .map(v -> v.multiply(BigDecimal.valueOf(100)))
                .map(BigDecimal::longValueExact)
                .orElse(0L);

        basket.getItems().forEach(item -> {
            item.setPrice(productService.getProduct(item.getId()).orElseThrow().getPrice());
        });

        long itemsAmount = basket.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .map(v -> v.multiply(BigDecimal.valueOf(100)))
                .mapToLong(BigDecimal::longValueExact)
                .sum();

        if (isStripeEnabled()) {
            RequestOptions requestOptions = RequestOptions.builder()
                    .setApiKey(appConfigProperties.getStripe().getSecretKey())
                    .build();

            if (StringUtils.isBlank(basket.getPaymentIntentId())) {
                PaymentIntentCreateParams intentCreateParams = PaymentIntentCreateParams.builder()
                        .setAmount(itemsAmount + shippingAmount)
                        .setCurrency("usd")
                        .build();
                PaymentIntent paymentIntent;
                try {
                    paymentIntent = PaymentIntent.create(intentCreateParams, requestOptions);
                } catch (StripeException e) {
                    e.printStackTrace();
                    throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't create payment");
                }
                basket.setPaymentIntentId(paymentIntent.getId());
                basket.setClientSecret(paymentIntent.getClientSecret());
            } else {
                PaymentIntentUpdateParams intentUpdateParams = PaymentIntentUpdateParams.builder()
                        .setAmount(itemsAmount + shippingAmount)
                        .build();

                try {
                    PaymentIntent.retrieve(basket.getPaymentIntentId(), requestOptions)
                            .update(intentUpdateParams, requestOptions);
                } catch (StripeException e) {
                    e.printStackTrace();
                    throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't update payment");
                }
            }
        } else {
            if (StringUtils.isBlank(basket.getPaymentIntentId())) {
                basket.setPaymentIntentId(RandomStringUtils.randomAlphanumeric(32));
            }
        }

        return basketService.updateBasket(basket);
    }

    public void updateOrderPaymentSucceeded(String paymentIntentId) {
        orderRepository.getUserOrderByPaymentIntentId(paymentIntentId)
                .ifPresent(order -> {
                    order.setStatus(OrderStatus.PaymentReceived);
                    orderRepository.save(order);
                });
    }

    public void updateOrderPaymentFailed(String paymentIntentId) {
        orderRepository.getUserOrderByPaymentIntentId(paymentIntentId)
                .ifPresent(order -> {
                    order.setStatus(OrderStatus.PaymentFailed);
                    orderRepository.save(order);
                });
    }

    public boolean isStripeEnabled() {
        return !appConfigProperties.getStripe().getSecretKey()
                .equals("sk_test_YOUR_KEY");
    }
}
