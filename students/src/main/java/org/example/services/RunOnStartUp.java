package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.configuration.properties.StudentsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class RunOnStartUp {

    @Autowired
    private final StudentService studentService;

    private final StudentsProperties studentsProperties;

    @EventListener(value = ApplicationStartedEvent.class, condition = "@studentsProperties.isLoaded")
    public void getStudentsListFromFile() throws IOException {

        studentService.getStudentsList();

        log.info("Список студентов загружен из файла");

    }
}
