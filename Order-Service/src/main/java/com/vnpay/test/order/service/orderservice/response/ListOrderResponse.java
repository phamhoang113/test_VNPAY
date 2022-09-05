package com.vnpay.test.order.service.orderservice.response;


import com.vnpay.test.order.service.orderservice.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ListOrderResponse extends BaseResponse{
    private List<Order> orders;
    private long quantity;

    public ListOrderResponse(int code, String desc, List<Order> orders, long quantity) {
        super(code, desc);
        this.orders = orders;
        this.quantity = quantity;
    }
}
