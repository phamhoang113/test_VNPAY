package com.test.vnpay.repository;

import java.util.Optional;

import com.test.vnpay.models.user.RoleEnum;
import com.test.vnpay.models.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(RoleEnum name);
}
