package com.ecommerce.platform.authservice.repository;

import com.ecommerce.platform.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);

}
