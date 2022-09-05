package com.vnpay.test.auth.service.authservice.service.impl;

import com.vnpay.test.auth.service.authservice.entity.RefreshToken;
import com.vnpay.test.auth.service.authservice.entity.Role;
import com.vnpay.test.auth.service.authservice.entity.RoleEnum;
import com.vnpay.test.auth.service.authservice.entity.User;
import com.vnpay.test.auth.service.authservice.repository.RefreshTokenRepository;
import com.vnpay.test.auth.service.authservice.repository.RoleRepository;
import com.vnpay.test.auth.service.authservice.repository.UserRepository;
import com.vnpay.test.auth.service.authservice.request.LoginRequest;
import com.vnpay.test.auth.service.authservice.request.RegisterRequest;
import com.vnpay.test.auth.service.authservice.request.ResetRequest;
import com.vnpay.test.auth.service.authservice.response.BaseResponse;
import com.vnpay.test.auth.service.authservice.response.login.LoginResponse;
import com.vnpay.test.auth.service.authservice.response.reset.ResetResponse;
import com.vnpay.test.auth.service.authservice.response.validate.ValidationResponse;
import com.vnpay.test.auth.service.authservice.service.AuthService;
import com.vnpay.test.auth.service.authservice.service.JwtUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    private final static Logger log = LoggerFactory.getLogger(AuthService.class);

    private enum PERMISSION {READ, INSERT, UPDATE, DELETE};

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${jwt.refreshExpirationDateInMs}")
    private Integer refreshTokenDurationMs;

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try{
            log.info("request: {}",loginRequest);
            User user = userRepository.findByUsername(loginRequest.getUserName())
                    .orElseThrow(() -> new Exception("user is not found"));
            log.info(user.getPassword());
            log.info(loginRequest.getPassword());
            if(BCrypt.checkpw(loginRequest.getPassword(),user.getPassword())){
                String accessToken = jwtUtils.generateJwtTokenFromUserName(user.getUsername());
                Set<String> roles = user.getRoles().stream().map(item ->item.getName().toString()).collect(Collectors.toSet());
                RefreshToken refreshToken = this.createRefreshToken(user.getId());
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setCode(HttpStatus.OK.value());
                loginResponse.setDesc("Login successfully!");
                loginResponse.setAccessToken(accessToken);
                loginResponse.setRefreshToken(refreshToken.getToken());
                loginResponse.setRoles(roles);
                return ResponseEntity.ok(loginResponse);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),"Username or password error!"));
            }
        }
        catch (Exception e){
            log.error("Login fail: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),"Login fail!"));
        }
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        try{
            log.info("request: {}",registerRequest);
            if (userRepository.existsByUsername(registerRequest.getUserName())) {
                return new ResponseEntity<>(new BaseResponse(HttpStatus.BAD_REQUEST.value(), "Username existed!!!"), HttpStatus.BAD_REQUEST);
            }

            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return new ResponseEntity<>(new BaseResponse(HttpStatus.BAD_REQUEST.value(),"Email existed!!!"), HttpStatus.BAD_REQUEST);
            }

            User user = new User(registerRequest.getUserName(),
                    registerRequest.getEmail(),
                    BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(4)),  registerRequest.getPhone(), registerRequest.getAddress());

            Set<String> strRoles = registerRequest.getRoles();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(RoleEnum.ROLE_CUSTOMER)
                        .orElseThrow(() -> new Exception("Role " + RoleEnum.ROLE_CUSTOMER + "is not found"));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                            if (role.equals("admin") || role.equals("ROLE_ADMIN")) {
                                Role adminRole = null;
                                try {
                                    adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                            .orElseThrow(() -> new Exception("Role " + RoleEnum.ROLE_ADMIN + "is not found"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                roles.add(adminRole);
                            }
                            else if (role.equals("user") || role.equals("ROLE_USER")) {
                                Role adminRole = null;
                                try {
                                    adminRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                            .orElseThrow(() -> new Exception("Role " + RoleEnum.ROLE_USER + "is not found"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                roles.add(adminRole);
                            }
                            else {
                                Role userRole = null;
                                try {
                                    userRole = roleRepository.findByName(RoleEnum.ROLE_CUSTOMER)
                                            .orElseThrow(() -> new Exception("Role " + RoleEnum.ROLE_CUSTOMER + "is not found"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                roles.add(userRole);
                            }
                        }
                );
            }
            user.setRoles(roles);
            userRepository.save(user);
            log.info("Successfully!");
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "Register successful!"));
        }
        catch (Exception e){
            log.error("Register fail!" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(HttpStatus.BAD_REQUEST.value(),"Register fail!"));
        }
    }

    @Override
    public ResponseEntity<?> resetAccessToken(ResetRequest refreshToken) {
        try{
            RefreshToken refreshToken1 = refreshTokenRepository.findByToken(refreshToken.getToken()).orElseThrow(
                    () -> new Exception("Token is not found")
        );
            if(refreshToken1.getExpiryDate().compareTo(new Date())<0){
                throw new  Exception("Token is expiried. please Login again");
            }
            else{
                String accessToken = jwtUtils.generateJwtTokenFromUserName(refreshToken1.getUser().getUsername());
                return ResponseEntity.ok(new ResetResponse(HttpStatus.OK.value(),"Reset access token successfully!", accessToken));
            }
        }
        catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> resetRefreshToken(ResetRequest refreshToken) {
        try {
            RefreshToken refreshToken1 = refreshTokenRepository.findByToken(refreshToken.getToken()).orElseThrow(
                    () -> new Exception("Reset fail!")
            );
            refreshTokenRepository.delete(refreshToken1);
            RefreshToken refreshToken2 = this.createRefreshToken(refreshToken1.getUser().getId());
            return ResponseEntity.ok(new ResetResponse(HttpStatus.OK.value(),"Successfully!",refreshToken2.getToken()));
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> validateAccessToken(String validateRequest) {
        try{
            if (null==validateRequest||validateRequest.isEmpty()){
                Set<String> permissions = new HashSet<>();
                permissions.add(PERMISSION.READ.toString());
                return ResponseEntity.ok(new ValidationResponse(HttpStatus.BAD_REQUEST.value(), "Fail",null, null, permissions));
            }
            if(jwtUtils.validateToken(validateRequest)){
                Set<String> permissions = new HashSet<>();
                String userName = jwtUtils.getUserNameFromToken(validateRequest);
                User user = userRepository.findByUsername(userName).orElseThrow(
                        () -> new Exception("User is not found!")
            );
                for(Role role : user.getRoles()){
                    if(role.getName() == RoleEnum.ROLE_ADMIN){
                        permissions.add(PERMISSION.READ.toString());
                        permissions.add(PERMISSION.UPDATE.toString());
                        permissions.add(PERMISSION.INSERT.toString());
                        permissions.add(PERMISSION.DELETE.toString());
                    }
                    else if(role.getName() == RoleEnum.ROLE_USER){
                        permissions.add(PERMISSION.READ.toString());
                        permissions.add(PERMISSION.UPDATE.toString());
                        permissions.add(PERMISSION.INSERT.toString());
                    }
                    else{
                        permissions.add(PERMISSION.READ.toString());
                    }
                }
                return ResponseEntity.ok( new ValidationResponse(HttpStatus.OK.value(), "validation Successful",userName, user.getId().toString(),permissions));
            }
            else{
                Set<String> permissions = new HashSet<>();
                permissions.add(PERMISSION.READ.toString());
                return ResponseEntity.ok(new ValidationResponse(HttpStatus.BAD_REQUEST.value(), "Fail",null, null, permissions));
            }
        }
        catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new BaseResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage()));
        }

    }

    @Override
    public ResponseEntity<?> signOut(String userId) {
        try {
            RefreshToken refresh = refreshTokenRepository.findByUser(userRepository.findById(Long.parseLong(userId)).orElseThrow(
                    () -> new Exception("Log out error!")
            )).orElseThrow(
                    () -> new Exception("Log out error!")
            );
            refreshTokenRepository.delete(refresh);
            return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), "Log out successfully!"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new BaseResponse(HttpStatus.OK.value(),"Log out fail!!!"), HttpStatus.BAD_REQUEST);
        }
    }


    private RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, refreshTokenDurationMs);
        refreshToken.setExpiryDate(calendar.getTime());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
}
