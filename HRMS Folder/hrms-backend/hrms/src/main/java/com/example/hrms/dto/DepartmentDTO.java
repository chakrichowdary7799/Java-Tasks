package com.example.hrms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentDTO {

    @NotBlank(message = "Department name is mandatory")
    @Size(min = 2, max = 50, message = "Department name must be between 2 and 50 characters")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}