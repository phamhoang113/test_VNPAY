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
            User user = userRepository.findByUsername(loginRequest.getUserName())
                    .orElseThrow(() -> new RuntimeException("User Not Found with username: " + loginRequest.getUserName()));
            log.info(user.getPassword());
            log.info(loginRequest.getPassword());
            if(BCrypt.checkpw(loginRequest.getPassword(),user.getPassword())){
                String accessToken = jwtUtils.generateJwtTokenFromUserName(user.getUsername());
                Set<String> roles = user.getRoles().stream().map(item ->item.getName().toString()).collect(Collectors.toSet());
                RefreshToken refreshToken = this.createRefreshToken(user.getId());
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setCode("200");
                loginResponse.setDesc("Login successfully!");
                loginResponse.setAccessToken(accessToken);
                loginResponse.setRefreshToken(refreshToken.getToken());
                loginResponse.setRoles(roles);
                return ResponseEntity.ok(loginResponse);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Login fail! Username or password is incorrect");
            }
        }
        catch (Exception e){
            log.error("Login fail: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Login fail! " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUserName())) {
            return new ResponseEntity<>(new BaseResponse("400","Username existed!!!"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(new BaseResponse("400","Email existed!!!"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(registerRequest.getUserName(),
                registerRequest.getEmail(),
                BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(4)),  registerRequest.getPhone(), registerRequest.getAddress());

        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                        if (role.equals("admin") || role.equals("ROLE_ADMIN")) {
                            Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                        }
                        else if (role.equals("user") || role.equals("ROLE_USER")) {
                            Role adminRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                        }
                        else {
                            Role userRole = roleRepository.findByName(RoleEnum.ROLE_CUSTOMER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                        }
                    }
            );
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new BaseResponse("200", "Register successful!"));
    }

    @Override
    public ResponseEntity<?> resetAccessToken(ResetRequest refreshToken) {
        RefreshToken refreshToken1 = refreshTokenRepository.findByToken(refreshToken.getToken()).orElseThrow(
                () -> new RuntimeException("Token is not found")
        );
        if(refreshToken1.getExpiryDate().compareTo(new Date())<0){
            throw new RuntimeException("Token is Expiried. Please Login again");
        }
        else{
            String accessToken = jwtUtils.generateJwtTokenFromUserName(refreshToken1.getUser().getUsername());
            return ResponseEntity.ok(new ResetResponse(accessToken,"200","Reset access token successfully!"));
        }
    }

    @Override
    public ResponseEntity<?> resetRefreshToken(ResetRequest refreshToken) {
        RefreshToken refreshToken1 = refreshTokenRepository.findByToken(refreshToken.getToken()).orElseThrow(
                () -> new RuntimeException("Reset Fail!!!")
        );
        refreshTokenRepository.delete(refreshToken1);
        RefreshToken refreshToken2 = this.createRefreshToken(refreshToken1.getUser().getId());
        return ResponseEntity.ok(new ResetResponse(refreshToken2.getToken()));
    }

    @Override
    public ResponseEntity<?> validateAccessToken(String validateRequest) {
        if (null==validateRequest||validateRequest.isEmpty()){
            Set<String> permissions = new HashSet<>();
            permissions.add(PERMISSION.READ.toString());
            return ResponseEntity.ok(new ValidationResponse(null, null, permissions));
        }
        if(jwtUtils.validateToken(validateRequest)){
            Set<String> permissions = new HashSet<>();
            String userName = jwtUtils.getUserNameFromToken(validateRequest);
            User user = userRepository.findByUsername(userName).orElseThrow(
                    () -> new RuntimeException("User not found!!!")
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
            return ResponseEntity.ok(new ValidationResponse(userName, user.getId().toString(),permissions,"200","validation Successful"));
        }
        else{
            Set<String> permissions = new HashSet<>();
            permissions.add(PERMISSION.READ.toString());
            return ResponseEntity.ok(new ValidationResponse(null, null, permissions));
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
            return ResponseEntity.ok(new BaseResponse("200", "Log out successfully!"));
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse("400","Log out fail!!!"), HttpStatus.BAD_REQUEST);
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
