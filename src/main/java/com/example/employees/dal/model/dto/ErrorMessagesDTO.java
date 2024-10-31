package com.example.employees.dal.model.dto;

import java.util.List;

public class ErrorMessagesDTO {
    private List<ErrorMessageDTO> errors;

    public ErrorMessagesDTO(List<ErrorMessageDTO> errors) {
        this.errors = errors;
    }

    public List<ErrorMessageDTO> getErrors() {
        return errors;
    }
}
