package com.test.vnpay.controllers.order;

import com.test.vnpay.models.orders.Orders;
import com.test.vnpay.models.orders.StatusOrderEnum;
import com.test.vnpay.repository.OrdersRepository;
import com.test.vnpay.security.exception.InsertErrorException;
import com.test.vnpay.security.exception.ParameterException;
import com.test.vnpay.security.exception.UpdateException;
import com.test.vnpay.security.jwt.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrdersRepository ordersRepository;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping(value = {"/list_order", "/list_orders"})
    List<Orders> getListOrder(@RequestParam(required = false) String id) {
        try {
            if (id != null && !id.isEmpty()) {
                Long idOrder = Long.parseLong(id);
                Orders order = ordersRepository.findById(idOrder).get();
                List<Orders> resultList = new ArrayList<>();
                resultList.add(order);
                return resultList;
            } else return ordersRepository.findAll();
        } catch (NumberFormatException numberFormatException) {
            throw new ParameterException("id", "integer");
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = {"/add_order"})
    public ResponseEntity<?> createOrder(@Valid @RequestBody Orders orders) {
        try {
            Orders orders1 = new Orders(orders.getDate(), StatusOrderEnum.PENDING,orders.getUser(),orders.getBooks());
            ordersRepository.save(orders1);
            return ResponseEntity.ok(new MessageResponse("Insert Successfully!"));
        }
        catch (Exception e){
            throw new InsertErrorException();
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = {"/modify_order"})
    public ResponseEntity<?> modifyBook(@Valid @RequestBody Orders orders) {
        try {
            ordersRepository. save(orders);
            return ResponseEntity.ok(new MessageResponse("Update successfully!"));
        }
        catch (Exception e){
            throw new UpdateException();
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = {"/delete_order"})
    public ResponseEntity<?> modifyBook(@Valid @RequestBody String id) {
        try {
            ordersRepository.deleteById(Long.parseLong(id));
            return ResponseEntity.ok(new MessageResponse("Delete successfully!"));
        } catch (NumberFormatException nbEx) {
            throw new ParameterException("id", "integer");
        } catch (Exception e) {
            throw new UpdateException();
        }
    }
}
