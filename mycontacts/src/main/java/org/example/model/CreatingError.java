package org.example.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatingError {

    String message = "Ошибка создания контакта: контакт с таким Email уже существует";
}
