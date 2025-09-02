package com.ecommerce.platform.authservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles") // Указываем имя таблицы
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String name; // Название роли (например: ROLE_USER, ROLE_ADMIN)
}