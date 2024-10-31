package com.example.employees.bl.dataAccess;

import com.example.employees.dal.dataAccess.EmployeeProjectAssignmentsDataAccess;
import com.example.employees.dal.exceptions.CSVIOException;
import com.example.employees.dal.exceptions.CsvRowsWithBadDateFormatException;
import com.example.employees.dal.exceptions.MissingRequiredColumnsException;
import com.example.employees.dal.exceptions.WrongMimeTypeException;
import com.example.employees.dal.model.dto.EmployeeProjectAssignmentRPO;
import com.example.employees.dal.model.dto.ProjectColleaguesPairRPO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class EmployeeProjectAssignmentsDataAccessImpl implements EmployeeProjectAssignmentsDataAccess {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeProjectAssignmentsDataAccessImpl.class);
    private static String CSV_FILE_MIME_TYPE = "text/csv";
    private static String EMP_ID = "EmpID";
    private static String PROJECT_ID = "ProjectID";
    private static String DATE_FROM = "DateFrom";
    private static String DATE_TO = "DateTo";

    @Value("${app.csv.dateFormat:yyyy-MM-dd}")
    private String dateFormat;

    @Override
    public List<ProjectColleaguesPairRPO> uploadAssignmentsCSV(MultipartFile file) {
        if (!CSV_FILE_MIME_TYPE.equals(file.getContentType())) {
            throw new WrongMimeTypeException();
        }
        List<EmployeeProjectAssignmentRPO> employeesAssignments = this.parseCSVFile(file);
        return obtainLongestWorkingPairsFromProjects(employeesAssignments);
    }

    private List<EmployeeProjectAssignmentRPO> parseCSVFile(MultipartFile file) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(
                     fileReader,
                     CSVFormat.DEFAULT.builder()
                             .setHeader()
                             .setTrim(true)
                             .build()
             )
        ) {
            List<CSVRecord> csvRecords = csvParser.getRecords();
            List<String> missingColumns = new ArrayList<>();
            if (csvRecords.size() > 0) {
                CSVRecord firstRecord = csvRecords.get(0);
                if (!firstRecord.isMapped(EMP_ID)) {
                    missingColumns.add(EMP_ID);
                }
                if (!firstRecord.isMapped(PROJECT_ID)) {
                    missingColumns.add(PROJECT_ID);
                }
                if (!firstRecord.isMapped(DATE_FROM)) {
                    missingColumns.add(DATE_FROM);
                }
                if (!firstRecord.isMapped(DATE_TO)) {
                    missingColumns.add(DATE_TO);
                }
            }
            if (!missingColumns.isEmpty()) {
                throw new MissingRequiredColumnsException(missingColumns);
            }


//            csvRecords.remove(0);
            List<EmployeeProjectAssignmentRPO> employeeToProjectAssignments = new ArrayList<EmployeeProjectAssignmentRPO>();
            long index = 0;
            List<Long> rowsWithBadDateFrom = new ArrayList<>();
            List<Long> rowsWithBadDateTo = new ArrayList<>();
            for (Iterator<CSVRecord> it = csvRecords.iterator(); it.hasNext(); index++) {
                CSVRecord csvRecord = it.next();
                LOG.debug("Values for row #" + index + 1 + ": " +
                        String.join(", ", csvRecord.stream().map(s -> s).collect(Collectors.toList())));

                LocalDate dateFrom = null;
                LocalDate dateTo = null;
                try {
                    dateFrom = parseDateFrom(csvRecord.get(DATE_FROM));
                } catch (DateTimeParseException e) {
                    rowsWithBadDateFrom.add(index + 1);
                }
                try {
                    dateTo = parseDateTo(csvRecord.get(DATE_TO));
                } catch (DateTimeParseException e) {
                    rowsWithBadDateTo.add(index + 1);
                }
                if (dateFrom == null || dateTo == null) {
                    continue;
                }
                EmployeeProjectAssignmentRPO employeeProjectAssignmentRPO = new EmployeeProjectAssignmentRPO(
                        csvRecord.get(EMP_ID),
                        csvRecord.get(PROJECT_ID),
                        dateFrom,
                        dateTo
                );
                employeeToProjectAssignments.add(employeeProjectAssignmentRPO);
            }
            if (!rowsWithBadDateFrom.isEmpty() && !rowsWithBadDateTo.isEmpty()) {
                throw new CsvRowsWithBadDateFormatException(rowsWithBadDateFrom, rowsWithBadDateTo);
            }

            return employeeToProjectAssignments;
        } catch (IOException e) {
            throw new CSVIOException("Failed to parse the CSV file", e);
        }
    }

    private LocalDate parseDateFrom(String dateFrom) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDate.parse(dateFrom, formatter);
    }

    private LocalDate parseDateTo(String dateTo) {
        if (dateTo == null || dateTo.trim().equals("") || dateTo.trim().equalsIgnoreCase("NULL")) {
            return LocalDate.now();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDate.parse(dateTo, formatter);
    }

    private List<ProjectColleaguesPairRPO> obtainLongestWorkingPairsFromProjects(List<EmployeeProjectAssignmentRPO> employeesAssignments) {
        TreeMap<String, ProjectColleaguesPairRPO> longestWorkingColleaguesPairsPerProjects = new TreeMap<>();
        for(int i = 0; i < employeesAssignments.size(); i++) {
            EmployeeProjectAssignmentRPO employee1 = employeesAssignments.get(i);
            List<EmployeeProjectAssignmentRPO> sameProjectAssignments = employeesAssignments.stream()
                    .skip(i+1)
                    .filter(eAssignment -> eAssignment.getProjectId().equals(employee1.getProjectId()))
                    .collect(Collectors.toList());
            for(EmployeeProjectAssignmentRPO employee2 : sameProjectAssignments) {
                String currentProjectId = employee1.getProjectId();
                ProjectColleaguesPairRPO cPair = new ProjectColleaguesPairRPO(employee1, employee2);
                ProjectColleaguesPairRPO existingPair = longestWorkingColleaguesPairsPerProjects.get(currentProjectId);
                if (cPair.getDaysWorkingTogether() > 0) {
                    if (existingPair == null) {
                        longestWorkingColleaguesPairsPerProjects.put(currentProjectId, cPair);
                    } else if (cPair.getDaysWorkingTogether() > existingPair.getDaysWorkingTogether()) {
                        longestWorkingColleaguesPairsPerProjects.put(currentProjectId, cPair);
                    }
                }
            }
        }
        return longestWorkingColleaguesPairsPerProjects.values().stream().toList();
    }
}
