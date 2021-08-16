package com.gitpolio.gitpolioserver.advice.error;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

@Getter
public class ErrorResponse {
    private ErrorResponse() {
        this.id = System.currentTimeMillis();
    }

    @Builder
    public ErrorResponse(ErrorStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    @Builder
    public ErrorResponse(ErrorStatus status, String message, @URL String docUrl) {
        this();
        this.status = status;
        this.message = message;
        this.docUrl = docUrl;
    }

    private Long id;
    private ErrorStatus status;
    private String message;
    @URL
    private String docUrl;
}