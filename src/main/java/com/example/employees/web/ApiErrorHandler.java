package com.example.employees.web;

import com.example.employees.dal.exceptions.CSVIOException;
import com.example.employees.dal.exceptions.CsvRowsWithBadDateFormatException;
import com.example.employees.dal.exceptions.MissingRequiredColumnsException;
import com.example.employees.dal.exceptions.WrongMimeTypeException;
import com.example.employees.dal.model.dto.ErrorMessageDTO;
import com.example.employees.dal.model.dto.ErrorMessagesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiErrorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ApiErrorHandler.class);

    @ExceptionHandler
    public ResponseEntity<ErrorMessagesDTO> hangleBadRequestException(CSVIOException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOG.debug(MessageFormat.format("{0}: {1}", httpStatus, httpStatus.getReasonPhrase()), e);

        ErrorMessagesDTO errorMessagesDTO = new ErrorMessagesDTO(
                Arrays.asList(new ErrorMessageDTO("csvfile", e.getMessage())));
        return ResponseEntity.status(httpStatus).body(errorMessagesDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessagesDTO> hangleBadRequestException(WrongMimeTypeException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOG.debug(MessageFormat.format("{0}: {1}", httpStatus, httpStatus.getReasonPhrase()), e);

        ErrorMessagesDTO errorMessagesDTO = new ErrorMessagesDTO(
                Arrays.asList(new ErrorMessageDTO("csvfile", e.getMessage())));
        return ResponseEntity.status(httpStatus).body(errorMessagesDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessagesDTO> hangleBadRequestException(MissingRequiredColumnsException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOG.debug(MessageFormat.format("{0}: {1}", httpStatus, httpStatus.getReasonPhrase()), e);

        List<ErrorMessageDTO> missingColumnsErrors = new ArrayList<>();
        missingColumnsErrors.addAll(
                e.getMissingColumns().stream()
                        .map(columnName -> {
                            return new ErrorMessageDTO(
                                    String.format("csvfile"),
                                    String.format("The CSV file is missing the required %s column", columnName)
                            );
                        }).collect(Collectors.toList())
        );
        ErrorMessagesDTO errorMessagesDTO = new ErrorMessagesDTO(missingColumnsErrors);
        return ResponseEntity.status(httpStatus).body(errorMessagesDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessagesDTO> hangleBadRequestException(CsvRowsWithBadDateFormatException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOG.debug(MessageFormat.format("{0}: {1}", httpStatus, httpStatus.getReasonPhrase()), e);

        List<ErrorMessageDTO> badDateFormatErrors = new ArrayList<>();
        badDateFormatErrors.addAll(
                e.getRowsWithBadDateFrom().stream()
                        .map(rowLong -> {
                            return new ErrorMessageDTO(
                                    String.format("Row #%d", rowLong),
                                    String.format("Row #%d has invalid or missing Date From format", rowLong)
                            );
                        }).collect(Collectors.toList())
                );
        badDateFormatErrors.addAll(
                e.getRowsWithBadDateTo().stream()
                        .map(rowLong -> {
                            return new ErrorMessageDTO(
                                    String.format("Row #%d", rowLong),
                                    String.format("Row #%d has invalid or missing Date To format", rowLong)
                            );
                        }).collect(Collectors.toList())
        );
        ErrorMessagesDTO errorMessagesDTO = new ErrorMessagesDTO(badDateFormatErrors);
        return ResponseEntity.status(httpStatus).body(errorMessagesDTO);
    }
}
