package com.ecommerce.platform.authservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "role_name" , unique = true)
    private String role;

    @Override
    public String getAuthority() {
        return role;
    }
}
