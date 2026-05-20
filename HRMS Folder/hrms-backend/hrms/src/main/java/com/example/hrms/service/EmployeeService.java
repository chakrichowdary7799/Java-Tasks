package com.example.hrms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hrms.dto.EmployeeDTO;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.model.Department;
import com.example.hrms.model.Employee;
import com.example.hrms.model.EmployeeHistoryLog;
import com.example.hrms.model.Role;
import com.example.hrms.repository.DepartmentRepository;
import com.example.hrms.repository.EmployeeHistoryRepository;
import com.example.hrms.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeHistoryRepository historyRepository;

    public EmployeeService(EmployeeRepository employeeRepository, 
                           DepartmentRepository departmentRepository, 
                           EmployeeHistoryRepository historyRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    @SuppressWarnings("null")
    public Employee onboardEmployee(EmployeeDTO dto) {
        logger.info("Processing onboarding workflow for email: {}", dto.getEmail());
        
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Duplicate Entry: Employee record already exists with email: " + dto.getEmail());
        }

        Department dept = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        employee.setBaseSalary(dto.getBaseSalary());
        employee.setAttendanceCount(dto.getAttendanceCount());
        employee.setDepartment(dept);

        Employee savedEmployee = employeeRepository.save(employee);

        // Maintain operational history log tracking
        historyRepository.save(new EmployeeHistoryLog(savedEmployee.getId(), "EMPLOYEE_ONBOARDED", LocalDateTime.now()));
        return savedEmployee;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @SuppressWarnings("null")
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }

    public Double calculateSalary(Long employeeId) {
        logger.info("Executing payroll calculation engine for employee ID: {}", employeeId);
        Employee employee = getEmployeeById(employeeId);
        
        // Custom Rule Configuration: Salary based on attendance (Max 22 standard working days metric)
        int standardWorkingDays = 22;
        int standardAttendance = Math.min(employee.getAttendanceCount(), standardWorkingDays);
        
        double dailyRate = employee.getBaseSalary() / standardWorkingDays;
        double finalCalculatedSalary = dailyRate * standardAttendance;
        
        logger.info("Payroll output computed successfully for ID {}: {}", employeeId, finalCalculatedSalary);
        return finalCalculatedSalary;
    }
}