package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Profile("default")
public class ContactSetUtils implements ContactsUtils {

    private final static Set<String> contacts = new HashSet<>();

    @Override
    public Set<String> getAllContacts() {

        return contacts;
    }

    @Override
    public void saveContact(String contact) {

        contacts.add(contact);
    }

    @Override
    public void deleteContact(String contact) {

        if (contacts.remove(contact)) {
            System.out.println("Контакт успешно удален");
        };


    }
}
