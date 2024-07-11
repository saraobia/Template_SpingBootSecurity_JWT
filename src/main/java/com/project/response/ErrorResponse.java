package com.project.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.model.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private ErrorCode errorCode;
    private String message;
    private HttpStatus status;
    private Integer statusCode;

    public Integer getStatusCode() {
        return errorCode.getStatus().value();
    }

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }

    public ErrorResponse(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.setMessage(errorMessage != null ? errorMessage : errorCode.getMessage());
    }

    public ErrorResponse(ErrorCode errorCode, Map<String, String> errorMessage) {
        this.errorCode = errorCode;
        this.setMessage(!errorMessage.isEmpty() ? errorMessage.toString() : errorCode.getMessage());
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.setMessage(errorCode.getMessage());
    }



}
