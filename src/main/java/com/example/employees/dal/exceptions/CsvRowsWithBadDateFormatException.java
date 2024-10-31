package com.example.employees.dal.exceptions;

import java.util.List;

public class CsvRowsWithBadDateFormatException extends RuntimeException {
    private List<Long> rowsWithBadDateFrom;
    private List<Long> rowsWithBadDateTo;

    public CsvRowsWithBadDateFormatException(List<Long> rowsWithBadDateFrom, List<Long> rowsWithBadDateTo) {
        super("Some rows has Date Format Errors");
        this.rowsWithBadDateFrom = rowsWithBadDateFrom;
        this.rowsWithBadDateTo = rowsWithBadDateTo;
    }

    public List<Long> getRowsWithBadDateFrom() {
        return rowsWithBadDateFrom;
    }

    public List<Long> getRowsWithBadDateTo() {
        return rowsWithBadDateTo;
    }
}
