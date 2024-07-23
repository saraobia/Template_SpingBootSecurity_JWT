package com.project.config;

import com.project.exception.UserException;
import com.project.model.enums.ErrorCode;
import com.project.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@EnableWebMvc
@RestControllerAdvice
public class ExceptionHandlerConfig {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    @ExceptionHandler({UserException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        logStacktrace(e.getResponse(), e);
        return new ResponseEntity<>(e.getResponse(), e.getResponse().getStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> noHandlerFoundException(NoHandlerFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.RNF, getMessage(e));
        logStacktrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (ObjectError objError : result.getAllErrors()) {
            FieldError fieldError = (FieldError) objError;
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ISE, errors);
        logStacktrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ISE, getMessage(e));
        logStacktrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> missingRequestHeaderException(MissingRequestHeaderException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ISE, getMessage(e));
        logStacktrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> genericException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ISE, getMessage(e));
        logStacktrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsException(BadCredentialsException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.EBC, getMessage(e));
        logStacktrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> missingElementException(NoSuchElementException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.IB, getMessage(e));
        logStacktrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> usernameNotFoundException(UsernameNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BR, getMessage(e));
        logStacktrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> expiredJwtException(ExpiredJwtException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.EXT, getMessage(e));
        logStacktrace(errorResponse, e);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    private void logStacktrace(ErrorResponse errorResponse, Exception e) {
        log.error(errorResponse.getMessage());
        for (StackTraceElement element : Arrays.copyOfRange(e.getStackTrace(), 0, Math.min(e.getStackTrace().length, 20))) {
            log.error(element.toString());
        }
    }

    private String getMessage(Exception e) {
        return e.getMessage() != null && !e.getMessage().isEmpty() ? e.getMessage() : null;
    }
}
