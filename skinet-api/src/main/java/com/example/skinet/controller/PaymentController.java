package com.example.skinet.controller;

import com.example.skinet.core.entity.CustomerBasket;
import com.example.skinet.core.entity.order.Order;
import com.example.skinet.error.ApiException;
import com.example.skinet.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    private final String whSecret = "whsec_516cb1c8c7a84dd79bbc3b7db0d8ebe58746c66636b2612dbaace0eb8b790d33";

    @Secured("ROLE_USER")
    @PostMapping("{id}")
    public ResponseEntity<CustomerBasket> createOrUpdatePaymentIntent(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.createOrUpdatePaymentIntent(id));
    }

    @PostMapping("webhook")
    public void stripeWebhook(@RequestBody String payload,
                              @RequestHeader("Stripe-Signature") String sigHeader) {


        PaymentIntent intent;
        Order order;


        Event event = null;

        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, whSecret
            );
//        }
//        catch (JsonSyntaxException e) {
//            // Invalid payload
//            throw new ApiException(HttpStatus.BAD_REQUEST);
//            return "";
        } catch (SignatureVerificationException e) {
            // Invalid signature
            throw new ApiException(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_REQUEST);
        }

        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
            throw new ApiException(HttpStatus.BAD_REQUEST);
        }
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded": {
                intent = (PaymentIntent) stripeObject;
                log.info("Payment succeded: {}", intent.getId());
                paymentService.updateOrderPaymentSucceeded(intent.getId());
                break;
            }
            case "payment_intent.payment_failed": {
                intent = (PaymentIntent) stripeObject;
                log.info("Payment failed: {}", intent.getId());
                paymentService.updateOrderPaymentFailed(intent.getId());
                break;
            }

            default:
                log.info("Unhandled event type: " + event.getType());
                break;
        }


    }

}
