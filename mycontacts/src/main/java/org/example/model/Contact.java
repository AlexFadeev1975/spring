package org.example.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data

public class Contact {

    long id;

    String firstName;

    String lastName;

    String email;

    String phone;


}
