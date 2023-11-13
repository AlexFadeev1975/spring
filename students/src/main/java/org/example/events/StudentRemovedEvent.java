package org.example.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class StudentRemovedEvent extends ApplicationEvent {

    private String id;

    public StudentRemovedEvent(String source) {
        super(source);
    }
}
