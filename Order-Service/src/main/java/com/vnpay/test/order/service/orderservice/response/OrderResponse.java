package com.vnpay.test.order.service.orderservice.response;

import com.vnpay.test.order.service.orderservice.entity.Order;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderResponse extends BaseResponse{
    private Order order;

    public OrderResponse(int code, String desc, Order order) {
        super(code, desc);
        this.order = order;
    }
}
