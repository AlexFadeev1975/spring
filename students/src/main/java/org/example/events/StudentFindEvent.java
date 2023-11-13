package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter

public class StudentFindEvent extends ApplicationEvent {

    private String pattern;

    public StudentFindEvent(String source) {
        super(source);
    }
}
