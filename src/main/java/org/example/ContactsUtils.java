package org.example;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;


@Component

public interface ContactsUtils {

    Set<String> getAllContacts() throws IOException;

    void saveContact(String contact) throws IOException;

    void deleteContact(String contact) throws IOException;


}
