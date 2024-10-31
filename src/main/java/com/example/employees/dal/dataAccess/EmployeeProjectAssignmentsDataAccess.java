package com.example.employees.dal.dataAccess;

import com.example.employees.dal.model.dto.ProjectColleaguesPairRPO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeProjectAssignmentsDataAccess {
    public List<ProjectColleaguesPairRPO> uploadAssignmentsCSV(MultipartFile file);
}
