package com.ecommerce.platform.authservice.exception;

public class UserAlreadyExistsException extends BusinessException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
