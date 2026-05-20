package com.example.hrms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.hrms.dto.DepartmentDTO;
import com.example.hrms.model.Department;
import com.example.hrms.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(DepartmentDTO dto) {
        logger.info("Attempting to create department with name: {}", dto.getName());
        if (departmentRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Department name already exists");
        }
        Department department = new Department();
        department.setName(dto.getName());
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}