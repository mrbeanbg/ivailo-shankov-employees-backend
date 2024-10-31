package com.example.employees.dal.exceptions;

public class WrongMimeTypeException extends RuntimeException {
    private String message;

    public WrongMimeTypeException() {
        super("Wrong file type. Please upload CSV file");
        this.message = "Wrong file type. Please upload CSV file";
    }
}
