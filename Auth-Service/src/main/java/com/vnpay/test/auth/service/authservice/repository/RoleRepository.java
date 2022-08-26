package com.vnpay.test.auth.service.authservice.repository;

import com.vnpay.test.auth.service.authservice.entity.Role;
import com.vnpay.test.auth.service.authservice.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
