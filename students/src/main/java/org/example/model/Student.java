package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Student {

    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;
    private Integer age;

    public String toString() {

        return "*  Студент: ID - " + id + " Имя - " + firstName + " Фамилия - "
                + lastName + " возраст - " + age + " лет";
    }
}
