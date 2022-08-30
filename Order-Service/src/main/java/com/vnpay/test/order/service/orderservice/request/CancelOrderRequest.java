package com.vnpay.test.order.service.orderservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CancelOrderRequest {
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("order_id")
    private long orderId;
}
