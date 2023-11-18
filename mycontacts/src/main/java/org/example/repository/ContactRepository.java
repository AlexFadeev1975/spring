package org.example.repository;

import org.example.model.Contact;

import java.util.List;

public interface ContactRepository {

    Contact save(Contact contact);

    Contact update(Contact contact);

    Contact findById(Long id);

    List<Contact> findAll();

    Contact deleteById(Long id);

    void create();

    Contact findByEmail(String email);


}
