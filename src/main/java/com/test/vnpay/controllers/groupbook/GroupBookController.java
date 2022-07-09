package com.test.vnpay.controllers.groupbook;

import com.test.vnpay.models.book.BookGroup;
import com.test.vnpay.repository.GroupBookrepository;
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
@RequestMapping("/api/book_group")
public class GroupBookController {

    @Autowired
    GroupBookrepository groupBookrepository;


    @GetMapping(value = {"/list_group_book"})
    List<BookGroup> getListGroupBook(@RequestParam(required = false) String id,
                                     @RequestParam(required = false) String name) {
        if (id != null && !id.isEmpty()) {
            try {
                int bookGroupId = Integer.parseInt(id);
                List<BookGroup> resultList = new ArrayList<>();
                BookGroup bookGroup = groupBookrepository.findById(bookGroupId).get();
                resultList.add(bookGroup);
                return resultList;
            }
            catch (NumberFormatException numberFormatException){
                throw new ParameterException("id","integer");
            }
            catch (NoSuchElementException noSuchElementException){
                return new ArrayList<>();
            }
        }
        if (name != null && !name.isEmpty()) {
            try {
                List<BookGroup> resultList = new ArrayList<>();
                BookGroup bookGroup = groupBookrepository.findByName(name).get();
                resultList.add(bookGroup);
                return resultList;
            }
            catch (NoSuchElementException e){
                return new ArrayList<>();
            }
        }
        return groupBookrepository.findAll();

    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = {"/add_group_book"})
    public ResponseEntity<?> createOrder(@Valid @RequestBody BookGroup bookGroup) {
        try {
            BookGroup bookGroup1 = new BookGroup(bookGroup.getName());
            groupBookrepository.save(bookGroup1);
            return ResponseEntity.ok(new MessageResponse("Insert Successfully!"));
        }
        catch (Exception e){
            throw new InsertErrorException();
        }
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = {"/modify_group_book"})
    public ResponseEntity<?> modifyBook(@Valid @RequestBody BookGroup bookGroup) {
        try {
            groupBookrepository. save(bookGroup);
            return ResponseEntity.ok(new MessageResponse("Update successfully!"));
        }
        catch (Exception e){
            throw new UpdateException();
        }
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = {"/delete_group_book"})
    public ResponseEntity<?> modifyBook(@Valid @RequestBody String id) {
        try {
            groupBookrepository.deleteById(Integer.parseInt(id));
            return ResponseEntity.ok(new MessageResponse("Delete successfully!"));
        } catch (NumberFormatException nbEx) {
            throw new ParameterException("id", "integer");
        } catch (Exception e) {
            throw new UpdateException();
        }
    }

}
