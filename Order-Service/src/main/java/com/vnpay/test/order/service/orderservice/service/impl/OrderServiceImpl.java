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
import com.vnpay.test.order.service.orderservice.response.BaseResponse;
import com.vnpay.test.order.service.orderservice.response.ListOrderResponse;
import com.vnpay.test.order.service.orderservice.response.OrderResponse;
import com.vnpay.test.order.service.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final int pageSize = 10;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FeignUserConfig.UserService userService;

    @Autowired
    private FeignBookConfig.BookService bookService;

    @Override
    public ResponseEntity<?> getListOrder(String userId, Optional<Integer> pageNumber, HttpHeaders headers) {
        try {
            log.info("Get list order with user id: {}, page number: {}",userId,pageNumber);
            int pageNum = 1;
            if(pageNumber.isPresent()){
                pageNum = pageNumber.get();
            }
            ResponseEntity<UserDto> userEntity = userService.getUserInfo(userId, headers);
            if (userEntity != null && userEntity.getStatusCode() == HttpStatus.OK) {
                Pageable pageable = PageRequest.of(pageNum-1,pageSize, Sort.by("date"));
                long quantity = orderRepository.countByUserId(userEntity.getBody().getId());
                log.info("Successfully!");
                return ResponseEntity.ok(new ListOrderResponse(HttpStatus.OK.value(), "Successfully!",orderRepository.findByUserId(userEntity.getBody().getId(),pageable).toList(),quantity));
            } else {
                log.error("User not found!");
                throw new Exception("User not found!");
            }
        }
        catch (Exception e){
            log.error("Get list orders fail" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> insertOrder(String userId, InsertOrderRequest insertOrderRequest, HttpHeaders headers) {
        try{
            log.info("Insert order with user id: {}, body: {}",userId,insertOrderRequest);
            if(!userId.equalsIgnoreCase(insertOrderRequest.getUserId())){
                throw new Exception("Don't have permission access to This service");
            }
            else{
                ResponseEntity<UserDto> userEntity = userService.getUserInfo(userId, headers);
                if (userEntity != null && userEntity.getStatusCode() == HttpStatus.OK) {
                    Order order = new Order();
                    order.setUserId(userEntity.getBody().getId());
                    order.setOrderDate(new Date());
                    order.setStatus(StatusOrderEnum.PENDING);
                    Set<OrderBook> orderBookSet = new HashSet<>();
                    double total =0;
                    for(int i=0;i<insertOrderRequest.getBooks().size();i++){
                        ResponseEntity<BookDto> bookEntity = bookService.getBookInfo(insertOrderRequest.getBooks().get(i).getBookId(), headers);
                        if(bookEntity!=null&&bookEntity.getStatusCode()==HttpStatus.OK) {
                            OrderBook orderBook = new OrderBook();
                            orderBook.setBookId(bookEntity.getBody().getId());
                            orderBook.setQuantity(insertOrderRequest.getBooks().get(i).getQuantity());
                            orderBookSet.add(orderBook);
                            total+=orderBook.getQuantity()*bookEntity.getBody().getPrice();
                        }
                        else{
                            throw new Exception("Book id is not found!");
                        }
                    }
                    order.setTotal(total);
                    orderRepository.save(order);
                    log.info("successfully!");
                    return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "Successfully!"));
                } else {
                    throw new Exception("User is not found!");
                }
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> cancelOrder(String userId, CancelOrderRequest cancelOrderRequest, HttpHeaders headers) {
        try {
            log.info("Cancel order with user id: {}, body: {}",userId,cancelOrderRequest);
            if(!userId.equals(cancelOrderRequest.getUserId())){
                throw new Exception("Don't have permission access to This service");
            }
            ResponseEntity<UserDto> userEntity = userService.getUserInfo(userId, headers);
            if (userEntity != null && userEntity.getStatusCode() == HttpStatus.OK) {
                Order order = orderRepository.findById(cancelOrderRequest.getOrderId()).orElseThrow(
                        () -> new Exception("Order is not found!")
                );
                StatusOrderEnum status = order.getStatus();
                if(status.compareTo(StatusOrderEnum.PENDING)==0){
                    order.setStatus(StatusOrderEnum.CANCEL);
                    orderRepository.save(order);
                    log.info("Successfully!");
                    return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "Cancel Successfully!!!"));
                }
                else{
                    log.error("Fail");
                    throw new Exception("Your order is shipped!");
                }
            } else {
                log.error("Fail");
                throw new Exception("User is not found!");
            }
        }
        catch (Exception e){
            log.error("Fail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> getInfoOrder(String userId, Optional<Long> orderId, HttpHeaders headers) {
        try {
            log.info("Get order detail with user id: {}, order id: {}",userId,orderId);
            ResponseEntity<UserDto> userEntity = userService.getUserInfo(userId, headers);
            if (userEntity != null && userEntity.getStatusCode() == HttpStatus.OK) {
                Order order = orderRepository.findById(orderId.get()).orElseThrow(
                        () -> new Exception("Order is not found!")
                );
                log.info("Successfully!");
                return ResponseEntity.ok(new OrderResponse(HttpStatus.OK.value(), "Successfully!",order));
            } else {
                log.error("Fail");
                throw new Exception("User is not found!");
            }
        }
        catch (Exception e){
            log.error("Fail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }
}
