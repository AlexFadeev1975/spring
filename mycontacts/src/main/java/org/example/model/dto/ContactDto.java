package org.example.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {

    long id;

    String firstName;

    String lastName;

    String email;

    String phone;

    String action;

    boolean fnError = false;

    boolean lnError = false;

    boolean emError = false;

    boolean phError = false;

    String errorMessage = "Неверный формат либо поле не заполнено";

    public ContactDto(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public ContactDto(String firstName, String lastName, String email, String phone, String id) {
        this.id = Long.parseLong(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public ContactDto(String firstName, String lastName, String email, String phone, boolean fnError, boolean lnError, boolean emError, boolean phError) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.fnError = fnError;
        this.lnError = lnError;
        this.emError = emError;
        this.phError = phError;
    }
}
