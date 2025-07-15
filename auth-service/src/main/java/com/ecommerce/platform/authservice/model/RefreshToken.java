package com.ecommerce.platform.authservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false, name = "expires_date")
    private Instant expiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

//    public boolean isExpired(){
//        return expiryDate.isBefore(Instant.now());
//    }

    public RefreshToken( String token,Instant expiryDate, User user) {
        this.expiryDate = expiryDate;
        this.token = token;
        this.user = user;
    }
}
