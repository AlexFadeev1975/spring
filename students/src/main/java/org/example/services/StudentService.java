package org.example.services;

import org.example.model.Student;

import java.io.IOException;
import java.util.List;

public interface StudentService {


    public Student addNewStudent(Student student);

    public Student deleteStudentById(String num);

    public List<Student> findStudents(String pattern);

    void getStudentsList() throws IOException;
}
