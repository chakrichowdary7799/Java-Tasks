package com.example.hrms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hrms.dto.LeaveRequestDTO;
import com.example.hrms.exception.ResourceNotFoundException;
import com.example.hrms.model.Employee;
import com.example.hrms.model.LeaveRequest;
import com.example.hrms.model.LeaveStatus;
import com.example.hrms.repository.EmployeeRepository;
import com.example.hrms.repository.LeaveRepository;

@Service
public class LeaveService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveService.class);

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveService(LeaveRepository leaveRepository, EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    @SuppressWarnings("null")
    public LeaveRequest submitLeaveRequest(LeaveRequestDTO dto) {
        logger.info("Submitting leave workflow for employee ID: {}", dto.getEmployeeId());
        Employee emp = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + dto.getEmployeeId()));

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(emp);
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setStatus(LeaveStatus.PENDING);

        return leaveRepository.save(request);
    }

    @Transactional
    @SuppressWarnings("null")
    public LeaveRequest processLeaveApproval(Long leaveId, String statusAction) {
        logger.info("Updating leave processing status for leave entry record ID: {}", leaveId);
        LeaveRequest leaveRequest = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request entry not found with ID: " + leaveId));

        LeaveStatus newStatus = LeaveStatus.valueOf(statusAction.toUpperCase());
        leaveRequest.setStatus(newStatus);
        
        return leaveRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRepository.findAll();
    }
}