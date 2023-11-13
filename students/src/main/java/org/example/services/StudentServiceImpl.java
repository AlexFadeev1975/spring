package org.example.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Student;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StudentServiceImpl implements StudentService {

    private final List<Student> studentList = new ArrayList<>();


    @Override
    public void getStudentsList() throws IOException {


        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream = getClass().getResourceAsStream("/data.json");
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Student> studentListFromFile = mapper.readValue(inputStream, mapper.getTypeFactory().constructCollectionType(List.class, Student.class));

        int countStudentsFromFile = studentListFromFile.size();

        int listSize = new Random().nextInt(countStudentsFromFile - 1);

        for (int i = 0; i <= listSize; i++) {

            studentList.add(studentListFromFile.get(new Random().nextInt(countStudentsFromFile - 1)));
        }
    }

    @Override
    public Student addNewStudent(Student student) {
        studentList.add(student);

        return student;

    }

    @Override
    public Student deleteStudentById(String num) {
        if (num.equals("*")) {
            studentList.clear();
            return null;
        }
        int id = Integer.parseInt(num);

        Student student = studentList.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);

        return studentList.remove(student) ? student : null;
    }

    @Override
    public List<Student> findStudents(String pattern) {

        return studentList.stream().filter(x -> x.toString().contains(pattern)).toList();
    }


}
