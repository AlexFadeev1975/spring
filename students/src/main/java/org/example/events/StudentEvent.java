package org.example.events;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Student;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class StudentEvent extends ApplicationEvent {

    private Student student;

    public StudentEvent(Student source) {
        super(source);
    }


}
