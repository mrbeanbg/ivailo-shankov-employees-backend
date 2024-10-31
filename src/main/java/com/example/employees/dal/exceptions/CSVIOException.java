package com.example.employees.dal.exceptions;

public class CSVIOException extends RuntimeException {
    public CSVIOException(String message) {
        super(message);
    }

    public CSVIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
