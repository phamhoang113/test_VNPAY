package com.vnpay.test.order.service.orderservice.controller;

import com.vnpay.test.order.service.orderservice.request.InsertOrderRequest;
import com.vnpay.test.order.service.orderservice.request.CancelOrderRequest;
import com.vnpay.test.order.service.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private enum PERMISSION {READ, INSERT, UPDATE, DELETE};

    @Autowired
    OrderService orderService;

    @GetMapping@PostMapping
    String info(){
        return "This is Order Service";
    }

    @GetMapping(value = {"/list_order"})
    ResponseEntity<?> listOrders(@RequestHeader HttpHeaders headers,
                               @RequestParam(name = "page_number") Optional<Integer> pageNumber) {
        String permission = headers.get("x-user-permission").get(0);
        String userId = headers.get("x-user-id").get(0);
        logger.info(permission);
        if(permission.contains(PERMISSION.READ.toString())) {
            return orderService.getListOrder(userId, pageNumber);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have permission to access resource");
        }
    }

    @PostMapping(value = {"/insert_order"})
    ResponseEntity<?> insertOrder(@RequestHeader HttpHeaders headers,
                                  @Valid @RequestBody InsertOrderRequest insertOrderRequest) {
        String permission = headers.get("x-user-permission").get(0);
        String userId = headers.get("x-user-id").get(0);
        logger.info(permission);
        if(permission.contains(PERMISSION.INSERT.toString())) {
            return orderService.insertOrder(userId, insertOrderRequest);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have permission to access resource");
        }
    }

    @PostMapping(value = {"/cancel_order"})
    ResponseEntity<?> updateOrder(@RequestHeader HttpHeaders headers,
                                  @Valid @RequestBody CancelOrderRequest cancelOrderRequest
    ) {
        String permission = headers.get("x-user-permission").get(0);
        String userId = headers.get("x-user-id").get(0);
        logger.info(permission);
        if(permission.contains(PERMISSION.UPDATE.toString())) {
            return orderService.cancelOrder(userId, cancelOrderRequest);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have permission to access resource");
        }
    }

    @PostMapping(value = {"/get_order/id/{order_id}"})
    ResponseEntity<?> getOrderInfo(@RequestHeader HttpHeaders headers,
                                  @PathVariable(name = "order_id") Optional<Long> orderId
    ) {
        String permission = headers.get("x-user-permission").get(0);
        String userId = headers.get("x-user-id").get(0);
        logger.info(permission);
        if(permission.contains(PERMISSION.READ.toString())) {
            return orderService.getInfoOrder(userId, orderId);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Don't have permission to access resource");
        }
    }

}
