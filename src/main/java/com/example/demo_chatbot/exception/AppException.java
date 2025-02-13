package com.example.demo_chatbot.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException{

    public AppException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    ErrorResponse errorResponse;
}
