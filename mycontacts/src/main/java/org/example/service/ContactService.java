package org.example.service;

import org.example.model.Contact;
import org.example.model.FillingError;
import org.example.model.dto.ContactDto;

import java.util.List;

public interface ContactService {

    boolean save (Contact contact);

    void update (Contact contact);

    List<ContactDto> findAll();

    void deleteById (Long id);

    void create();

    Contact findById (Long id);

    FillingError validateData (String firstName, String lastName, String email, String phone);


}
