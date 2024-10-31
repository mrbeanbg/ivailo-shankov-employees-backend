package com.example.employees.web;

import com.example.employees.dal.model.dto.ErrorMessagesDTO;
import com.example.employees.dal.model.dto.ProjectColleaguesPairRPO;
import com.example.employees.dal.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/employees-assignments")
@Tag(name = "Employees Assignments")
public class EmployeesAssignmentsController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Uploaded CSV file containing employee assignments to projects." +
                    "It returns a list of employee pairs for each project, " +
                    "consisting of the pair that worked together the longest on that specific project"
    )
    @ApiResponse(
            responseCode = "201",
            description = "List of employee pairs for each project (if any), consisting of the pair that worked together the longest on the project",
            content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectColleaguesPairRPO.class)))
            }
    )
    @ApiResponse(
            responseCode = "400",
            description = "The uploaded file is invalid. Returns list of validation errors",
            content = {
                    @Content(schema = @Schema(implementation = ErrorMessagesDTO.class))
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<ProjectColleaguesPairRPO>> uploadEmployeeProjectAssignmentsCSVFile(
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        employeeService.uploadEmployeeProjectAssignmentsCSV(file)
                );
    }
}
