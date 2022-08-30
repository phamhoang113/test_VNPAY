package com.vnpay.test.order.service.orderservice.service.impl;

import com.vnpay.test.order.service.orderservice.config.FeignBookConfig;
import com.vnpay.test.order.service.orderservice.config.FeignUserConfig;
import com.vnpay.test.order.service.orderservice.dto.BookDto;
import com.vnpay.test.order.service.orderservice.dto.UserDto;
import com.vnpay.test.order.service.orderservice.entity.Order;
import com.vnpay.test.order.service.orderservice.entity.OrderBook;
import com.vnpay.test.order.service.orderservice.entity.StatusOrderEnum;
import com.vnpay.test.order.service.orderservice.repository.OrderRepository;
import com.vnpay.test.order.service.orderservice.request.CancelOrderRequest;
import com.vnpay.test.order.service.orderservice.request.InsertOrderRequest;
import com.vnpay.test.order.service.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final int pageSize = 10;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FeignUserConfig.UserService userService;

    @Autowired
    private FeignBookConfig.BookService bookService;

    @Override
    public ResponseEntity<?> getListOrder(String userId, Optional<Integer> pageNumber) {
        try {
            int pageNum = 1;
            if(pageNumber.isPresent()){
                pageNum = pageNumber.get();
            }
            ResponseEntity<UserDto> userEntity = userService.getUserInfo(userId);
            if (userEntity != null && userEntity.getStatusCode() == HttpStatus.OK) {
                Pageable pageable = PageRequest.of(pageNum-1,pageSize, Sort.by("date"));
                return ResponseEntity.ok(orderRepository.findByUserId(userEntity.getBody().getId(),pageable).toList());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("user not found!!!");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("get list orders fail!!!");
        }
    }

    @Override
    public ResponseEntity<?> insertOrder(String userId, InsertOrderRequest insertOrderRequest) {
        try{
            if(!userId.equalsIgnoreCase(insertOrderRequest.getUserId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You dont have permission to insert Order!!!");
            }
            else{
                ResponseEntity<UserDto> userEntity = userService.getUserInfo(userId);
                if (userEntity != null && userEntity.getStatusCode() == HttpStatus.OK) {
                    Order order = new Order();
                    order.setUserId(userEntity.getBody().getId());
                    order.setOrderDate(new Date());
                    order.setStatus(StatusOrderEnum.PENDING);
                    Set<OrderBook> orderBookSet = new HashSet<>();
                    double total =0;
                    for(int i=0;i<insertOrderRequest.getBooks().size();i++){
                        ResponseEntity<BookDto> bookEntity = bookService.getBookInfo(insertOrderRequest.getBooks().get(i).getBookId());
                        if(bookEntity!=null&&bookEntity.getStatusCode()==HttpStatus.OK) {
                            OrderBook orderBook = new OrderBook();
                            orderBook.setBookId(bookEntity.getBody().getId());
                            orderBook.setQuantity(insertOrderRequest.getBooks().get(i).getQuantity());
                            orderBookSet.add(orderBook);
                            total+=orderBook.getQuantity()*bookEntity.getBody().getPrice();
                        }
                        else{
                            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("book id not found "+insertOrderRequest.getBooks().get(i).getBookId()+"!!!");
                        }
                    }
                    order.setTotal(total);
                    orderRepository.save(order);
                    return ResponseEntity.ok("Insert Successfully!!!");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("user not found!!!");
                }
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("insert fail!!!");
        }
    }

    @Override
    public ResponseEntity<?> cancelOrder(String userId, CancelOrderRequest cancelOrderRequest) {
        try {
            if(!userId.equals(cancelOrderRequest.getUserId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You dont have permission to cancel Order!!!");
            }
            ResponseEntity<UserDto> userEntity = userService.getUserInfo(userId);
            if (userEntity != null && userEntity.getStatusCode() == HttpStatus.OK) {
                Order order = orderRepository.findById(cancelOrderRequest.getOrderId()).orElseThrow(
                        () -> new RuntimeException("Order not found!!!")
                );
                StatusOrderEnum status = order.getStatus();
                if(status.compareTo(StatusOrderEnum.PENDING)==0){
                    order.setStatus(StatusOrderEnum.CANCEL);
                    orderRepository.save(order);
                    return ResponseEntity.ok("Cancel Successfully!!!");
                }
                else{
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Your order is Shipped!!!");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("user not found!!!");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("get list orders fail!!!");
        }
    }

    @Override
    public ResponseEntity<?> getInfoOrder(String userId, Optional<Long> orderId) {
        try {
            ResponseEntity<UserDto> userEntity = userService.getUserInfo(userId);
            if (userEntity != null && userEntity.getStatusCode() == HttpStatus.OK) {
                Order order = orderRepository.findById(orderId.get()).orElseThrow(
                        () -> new RuntimeException("Order not found!!!")
                );
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("user not found!!!");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("get order detail fail!!!");
        }
    }
}
