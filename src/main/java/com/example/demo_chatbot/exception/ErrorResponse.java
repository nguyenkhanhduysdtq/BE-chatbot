package com.example.demo_chatbot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorResponse {

    USER_EXISTED(1001,"user existed", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(1999,"Uncategorized exception",HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_CHARACTER(1002,"your username must be at least 5 character !", HttpStatus.BAD_REQUEST),
    PASSWORD_CHARACTER(1003,"your password must be at least 8 character !", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004,"user NOT existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(100,"token not exactly!",HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1007,"you not denied", HttpStatus.FORBIDDEN),
    PERMISSION_NOT_EXISTED(1008,"Permission not existed",HttpStatus.NOT_FOUND),
    INVALID_DATE(1008,"invalid date of birth",HttpStatus.BAD_REQUEST),
    FILE_NOT_ACCEPT(1009,"file not accurate",HttpStatus.BAD_REQUEST)

    ;



    private int code ;

    private String message;

    private HttpStatusCode statusCode ;

    ErrorResponse( int code,String message,HttpStatusCode statusCode) {
        this.message = message;
        this.code = code;
        this.statusCode=statusCode;
    }
}
