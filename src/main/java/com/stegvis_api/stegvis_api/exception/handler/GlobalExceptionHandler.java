package com.stegvis_api.stegvis_api.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.stegvis_api.stegvis_api.exception.dto.ErrorResponse;
import com.stegvis_api.stegvis_api.exception.type.AuthenticationException;
import com.stegvis_api.stegvis_api.exception.type.Generic;
import com.stegvis_api.stegvis_api.exception.type.TodoNotFoundException;
import com.stegvis_api.stegvis_api.exception.type.UserAlreadyExistsException;
import com.stegvis_api.stegvis_api.exception.type.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTodoNotFound(TodoNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationFailure(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    @ExceptionHandler(Generic.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Generic ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ett oväntat fel inträffade"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String firstErrorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Ogiltig inmatning");

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), firstErrorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Otillåten åtkomst"));
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex)
    // {
    // Map<String, Object> errorResponse = Map.of(
    // "timestamp", LocalDateTime.now(),
    // "message", ex.getMessage(),
    // "status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    // return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
}