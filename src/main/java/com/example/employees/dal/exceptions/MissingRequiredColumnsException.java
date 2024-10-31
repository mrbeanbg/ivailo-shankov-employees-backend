package com.example.employees.dal.exceptions;

import java.util.List;

public class MissingRequiredColumnsException extends RuntimeException {
    private List<String> missingColumns;

    public MissingRequiredColumnsException(List<String> missingColumns) {
        super("Missing Required Columns: " + String.join(", ", missingColumns));
        this.missingColumns = missingColumns;
    }

    public List<String> getMissingColumns() {
        return missingColumns;
    }
}
