package com.trier.trier_report.config;

import com.trier.trier_report.dto.ApiError;
import com.trier.trier_report.exception.DuplicateResourceException;
import com.trier.trier_report.exception.EmailUsedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ApiError response = new ApiError(status.value(), message, path);

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(EmailUsedException.class)
    public ResponseEntity<ApiError> handleException(EmailUsedException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleException(UsernameNotFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleException(EntityNotFoundException ex, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleException(DuplicateResourceException ex, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // Default
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiError> handleBadCredentials(Exception ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
//        ErrorResponse response = new ErrorResponse(401);
//        response.addError(ex.getMessage());
//
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }
//
//    @ExceptionHandler(RefreshTokenExpiredException.class)
//    public ResponseEntity<ErrorResponse> handleBadCredentials(RefreshTokenExpiredException ex) {
//        ErrorResponse response = new ErrorResponse(403);
//        response.addError(ex.getMessage());
//
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }
//
//    @ExceptionHandler(AccessTokenExpiredException.class)
//    public ResponseEntity<ErrorResponse> handleBadCredentials(AccessTokenExpiredException ex) {
//        ErrorResponse response = new ErrorResponse(401);
//        response.addError(ex.getMessage());
//
//        return ResponseEntity.status(response.getStatus()).body(response);
//    }
//
}
