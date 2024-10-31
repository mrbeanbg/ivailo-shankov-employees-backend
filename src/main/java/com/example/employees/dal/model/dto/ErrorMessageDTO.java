package com.example.employees.dal.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorMessageDTO {
    @Schema(nullable = true)
    private String fieldName;

    @Schema(nullable = true)
    private Long errorCode;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String errorMessage;

    public ErrorMessageDTO(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    public ErrorMessageDTO(String fieldName, Long errorCode, String errorMessage) {
        this.fieldName = fieldName;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
