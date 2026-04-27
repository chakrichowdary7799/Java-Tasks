package com.studentservice.student;

import java.util.List;

public interface StudentService {
    Student addStudent(Student student);
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student getStudentByName(String name);
    Student updateStudent(Long id, Student student);
    void deleteStudent(Long id);
}