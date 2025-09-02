package com.ecommerce.platform.authservice.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table( name ="refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String refreshToken; // ← Это поле должно существовать
    @OneToOne
    private User user;

    private Instant expiryDate;
}
