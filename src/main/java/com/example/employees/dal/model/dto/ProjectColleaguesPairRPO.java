package com.example.employees.dal.model.dto;

import com.example.employees.dal.util.DateUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProjectColleaguesPairRPO {
    private String projectId;

    private long daysWorkingTogether;
    private EmployeeProjectAssignmentRPO employee1;
    private EmployeeProjectAssignmentRPO employee2;

    public ProjectColleaguesPairRPO(EmployeeProjectAssignmentRPO employee1, EmployeeProjectAssignmentRPO employee2) {
        this.employee1 = employee1;
        this.employee2 = employee2;
    }

    public String getProjectId() {
        return employee1.getProjectId();
    }

    public long getDaysWorkingTogether() {
        LocalDate e1df = this.employee1.getDateFrom();
        LocalDate e1dt = this.employee1.getDateTo();
        LocalDate e2df = this.employee2.getDateFrom();
        LocalDate e2dt = this.employee2.getDateTo();

        if (
                (e2df.compareTo(e1df) >=0 && e2df.compareTo(e1dt) <= 0) ||
                        (e1df.compareTo(e2df) >=0 && e1df.compareTo(e2dt) <= 0)
        ) {
            LocalDate dateFrom = DateUtil.getLatestDate(this.employee1.getDateFrom(), this.employee2.getDateFrom());
            LocalDate dateTo = DateUtil.getEarliestDate(this.employee1.getDateTo(), this.employee2.getDateTo());
            return ChronoUnit.DAYS.between(dateFrom, dateTo) + 1;
        }
        return 0;
    }

    public EmployeeProjectAssignmentRPO getEmployee1() {
        return employee1;
    }

    public EmployeeProjectAssignmentRPO getEmployee2() {
        return employee2;
    }
}
