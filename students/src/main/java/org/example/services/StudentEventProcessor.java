package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.StudentEvent;
import org.example.events.StudentFindEvent;
import org.example.events.StudentRemovedEvent;
import org.example.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class StudentEventProcessor {
    @Autowired
    private final StudentService studentService;


    @EventListener(StudentEvent.class)
    public void listenStudentEvent(ApplicationEvent event) {

        Student student = (Student) event.getSource();

        log.info("{} успешно добавлен.", studentService.addNewStudent(student).toString());


    }

    @EventListener(StudentRemovedEvent.class)
    public void listenStudentRemovedEvent(ApplicationEvent event) {

        String id = (String) event.getSource();

        if (id.matches("[0-9]+")) {

            Student student = studentService.deleteStudentById(id);
            if (student != null) {
                log.info("{} успешно удален.", student);
            } else {
                log.info("Студент с id {} не найден", id);
            }

        } else {
            studentService.deleteStudentById(id);

            log.info("Список студентов пуст");
        }


    }

    @EventListener(StudentFindEvent.class)
    public void listenStudentFindEvent(ApplicationEvent event) {

        String pattern = (String) event.getSource();

        studentService.findStudents(pattern).forEach(x -> {

            log.info("{}", x.toString());
        });
    }
}
