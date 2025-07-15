package com.ecommerce.platform.authservice.repository;

import com.ecommerce.platform.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Находит пользователя по email (уникальное поле)
     * @param email email пользователя
     * @return Optional с пользователем или empty, если не найден
     */
    Optional<User> findByEmail(String email);
    /**
     * Проверяет существование пользователя с указанным email
     * @param email email для проверки
     * @return true если пользователь существует
     */
    boolean existsByEmail(String email);

}
