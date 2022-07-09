package com.test.vnpay.controllers.user;

import com.test.vnpay.models.user.User;
import com.test.vnpay.repository.UserRepository;
import com.test.vnpay.security.exception.ParameterException;
import com.test.vnpay.security.exception.UpdateException;
import com.test.vnpay.security.jwt.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Secured({"ROLE_ADMIN"})
    @GetMapping(value = {"/list_user", "/list_users"})
    public List<User> getListUser(@RequestParam(required = false) String id,
                                  @RequestParam(required = false) String name) {
        if (id != null && !id.isEmpty()) {
            try {
                Long userId = Long.parseLong(id);
                List<User> resultList = new ArrayList<>();
                User user = userRepository.findById(userId).get();
                resultList.add(user);
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
                List<User> resultList = new ArrayList<>();
                User user = userRepository.findByUsername(name).get();
                resultList.add(user);
                return resultList;
            }
            catch (NoSuchElementException e){
                return new ArrayList<>();
            }
        }
        return userRepository.findAll();
    }


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping(value = {"/modify_user"})
    public ResponseEntity<?> modifyBook(@Valid @RequestBody User user) {
        try {
            userRepository. save(user);
            return ResponseEntity.ok(new MessageResponse("Update successfully!"));
        }
        catch (Exception e){
            throw new UpdateException();
        }
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = {"/delete_user"})
    public ResponseEntity<?> modifyBook(@Valid @RequestBody String id) {
        try {
            userRepository.deleteById(Long.parseLong(id));
            return ResponseEntity.ok(new MessageResponse("Delete successfully!"));
        } catch (NumberFormatException nbEx) {
            throw new ParameterException("id", "integer");
        } catch (Exception e) {
            throw new UpdateException();
        }
    }
}
