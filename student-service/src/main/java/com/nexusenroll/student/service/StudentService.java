package com.nexusenroll.student.service;

import com.nexusenroll.student.model.Student;
import com.nexusenroll.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        if (id == null)
            throw new IllegalArgumentException("ID must not be null");
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setEmail(studentDetails.getEmail());
        student.setDegreeProgram(studentDetails.getDegreeProgram());
        student.setTotalCredits(studentDetails.getTotalCredits());
        student.setCourseGrades(studentDetails.getCourseGrades());

        return studentRepository.save(student);
    }

    public void deactivateStudent(Long id) {
        if (id == null)
            throw new IllegalArgumentException("ID must not be null");
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        // Simple deactivation logic (could be a status field)
        studentRepository.delete(student);
    }

    public List<String> getRequiredCourses(String studentId) {
        Student student = getStudentById(studentId);
        return student.getRequiredCourses();
    }
}
