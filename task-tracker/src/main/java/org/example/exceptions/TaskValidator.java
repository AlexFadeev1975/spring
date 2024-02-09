package org.example.exceptions;

import org.example.model.dto.TaskDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TaskValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return TaskDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "name", "name is required");
        ValidationUtils.rejectIfEmpty(errors, "description", "description is required");
        ValidationUtils.rejectIfEmpty(errors, "authorName", "authorName is required");
        ValidationUtils.rejectIfEmpty(errors, "assigneeName", "assigneeName is required");

    }
}
