package com.ecommerce.platform.authservice.exception;

public class InvalidTokenException extends BusinessException{
    public InvalidTokenException(String message) {
        super(message);
    }
}
