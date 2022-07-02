package com.vnpay.test.entities.user;

import com.vnpay.test.entities.role.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "pass_word", nullable = false)
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "token_expried")
    private boolean tokenExpired;

    @Column(name = "mobile_phone", length = 10)
    private String mobilePhone;

    @Column(name = "address", length = 10)
    private String address;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
}
