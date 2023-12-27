package org.example.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler({java.lang.NullPointerException.class, NoSuchElementException.class})
    public ResponseEntity<ExceptionResponse> handleNPE() {

        ExceptionResponse response = new ExceptionResponse("Объект не найден");

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> onConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<ExceptionResponse> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new ExceptionResponse(
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return ResponseEntity.ok(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<?> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<ExceptionResponse> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ExceptionResponse(error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(violations);
    }

    @ExceptionHandler(NotAccessException.class)
    public ResponseEntity<ExceptionResponse> handleNAE() {

        ExceptionResponse response = new ExceptionResponse("Текущий пользователь не имеет прав на изменение либо удаление объекта");

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIAE() {

        ExceptionResponse response = new ExceptionResponse("Неверный формат данных");

        return ResponseEntity.ok(response);
    }

}
