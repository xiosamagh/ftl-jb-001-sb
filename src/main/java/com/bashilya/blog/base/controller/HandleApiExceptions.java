package com.bashilya.blog.base.controller;

import com.bashilya.blog.auth.exceptions.AuthException;
import com.bashilya.blog.auth.exceptions.NotAccessException;
import com.bashilya.blog.base.api.response.ErrorResponse;
import com.bashilya.blog.comment.exception.CommentExistException;
import com.bashilya.blog.todoTask.exception.TodoTaskNotExistException;
import com.bashilya.blog.user.exception.UserExistException;
import com.bashilya.blog.user.exception.UserNotExistException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HandleApiExceptions extends ResponseEntityExceptionHandler {
    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<Object> notFoundException(ChangeSetPersister.NotFoundException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("NotFoundException", HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<Object> userExistException(UserExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("UserExistException", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> badRequest(UserNotExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("ResponseStatusException", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<Object> userNotExistException(UserNotExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("UserNotExistException", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> authException(AuthException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("AuthException", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(NotAccessException.class)
    public ResponseEntity<Object> notAccessException(NotAccessException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("NotAccessException", HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(TodoTaskNotExistException.class)
    public ResponseEntity<Object> todoTaskNotExistException(TodoTaskNotExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("TodoTaskNotExistException", HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(CommentExistException.class)
    public ResponseEntity<Object> commentExistException(CommentExistException ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("CommentExistException", HttpStatus.NOT_FOUND));
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex, WebRequest request) {
        return buildResponseEntity(ErrorResponse.of("Exception", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
