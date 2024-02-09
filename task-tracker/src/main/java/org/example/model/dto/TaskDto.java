package org.example.model.dto;

import lombok.Data;
import org.example.model.User;

import java.time.Instant;
import java.util.Set;

@Data
public class TaskDto {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private String status;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds;

    private User author;

    private User assignee;

    private Set<User> observers;

    private String authorName;

    private String assigneeName;

    private String observersName;

}
