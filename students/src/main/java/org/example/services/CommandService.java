package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.StudentEvent;
import org.example.events.StudentFindEvent;
import org.example.events.StudentRemovedEvent;
import org.example.model.Student;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Random;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class CommandService {

    private final ApplicationEventPublisher publisher;

    private Integer idRange = 1000000;

    @ShellMethod("Главное меню с описанием используемых команд")
    public void init() {

        System.out.print("Программа учета студентов"
                + "\r\n" +
                "- Для ввода нового студента введите команду 'create' [имя] [фамилия] [возраст(число)]" + "\r\n" +
                "- Для удаления студента введите команду 'delete' [id студента]" + "\r\n" +
                "- Для поиска студента введите команду 'find [данные студента любой точности" + "\r\n" +
                "- Для вывода всех студентов введите команду 'find *'" + "\r\n" +
                "- Для очистки списка студентов введите команду 'delete 0" + "\r\n" +
                "- Для выхода введите команду 'exit'");

    }

    @ShellMethod("команда добавления нового студента в формате [имя] [фамилия] [возраст]")
    public void create(@ShellOption(value = "firstname") String firstName,
                       @ShellOption(value = "lastname") String lastName,
                       @ShellOption(value = "age") String age) {

        if (firstName.matches("[a-zA-Zа-яА-ЯёЁ]+")
                && lastName.matches("[a-zA-Zа-яА-ЯёЁ]+")
                && age.matches("[0-9]+")) {

            Student student = new Student();
            student.setId(new Random().nextInt(idRange));
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setAge(Integer.valueOf(age));

            publisher.publishEvent(new StudentEvent(student));
        } else {
            log.info("Введены неверные данные.");
        }

    }

    @ShellMethod(value = "команда удаления студента по его Id либо очистки списка по * ")
    public void delete(@ShellOption(value = "id") String id) {

        if (id.matches("[0-9*]+")) {
            publisher.publishEvent(new StudentRemovedEvent(id));
        } else {
            log.info("Неверный формат ввода");
        }
    }

    @ShellMethod(value = "команда поиска студента по любому параметру либо вывод всех студентов по * ")
    public void find(@ShellOption(value = "pattern") String pattern) {

        publisher.publishEvent(new StudentFindEvent(pattern));
    }


}
