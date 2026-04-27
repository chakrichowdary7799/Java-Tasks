package com.studentservice.student;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StudentserviceApplication {
public static void main(String[] args) {
		SpringApplication.run(StudentserviceApplication.class, args);

	}
	@Bean
    CommandLineRunner initDatabase(StudentService studentService) {
        return args -> {
			Student s2 = new Student();
			s2.setName("Chakri");
			s2.setEmail("chakri@gmail.com");
			s2.setCourse("Computer Science");
			studentService.addStudent(s2);

			Student s3 = new Student();
			s3.setName("Nitheesh");
			s3.setEmail("nitheesh@gmail.com");
			s3.setCourse("Agriculture Engineering");
			studentService.addStudent(s3);

			List<Student> students = studentService.getAllStudents();
			students.forEach(student -> System.out.println("Student ID: " + student.getId() + ", Student Name: " + student.getName() 
			+ ", Email: " + student.getEmail() + ", Course: " + student.getCourse()));

			Student studentById = studentService.getStudentById(13L);
			System.out.println("Student found with ID 13: " + studentById.getName());

			Student updateStudent = new Student();
			updateStudent.setName("Sagar");
			updateStudent.setEmail("sagar@gmail.com");
			updateStudent.setCourse("Civil Engineering");
			Student updatedStudent = studentService.updateStudent(14L, updateStudent);
			System.out.println("Updated Student with ID 14: " + updatedStudent.getName());

			studentService.deleteStudent(4L);
			System.out.println("Deleted Student with ID 4");
		};
	}
}