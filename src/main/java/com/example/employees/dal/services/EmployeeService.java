package com.example.employees.dal.services;

import com.example.employees.dal.dataAccess.EmployeeProjectAssignmentsDataAccess;
import com.example.employees.dal.model.dto.ProjectColleaguesPairRPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeProjectAssignmentsDataAccess employeeProjectAssignmentsDataAccess;

    public List<ProjectColleaguesPairRPO> uploadEmployeeProjectAssignmentsCSV(MultipartFile file) {
        return employeeProjectAssignmentsDataAccess.uploadAssignmentsCSV(file);
    }
}
