package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Component
@Profile("init")
public class ContactFileUtils implements ContactsUtils {
    @Value("${app.save_source}")
    String path;


    @Override
    public Set<String> getAllContacts() throws IOException {

        String lineContacts = "error";

        try {
            FileInputStream inputStream = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            int character;
            StringBuilder builder = new StringBuilder();

            while ((character = reader.read()) != -1) {
                builder.append((char) character);
            }

            lineContacts = builder.toString();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");

        }

        if (lineContacts.equals("error")) {
            return new HashSet<>();
        }

        String[] arrayContacts = lineContacts.split("\r\n");

        return Set.of(arrayContacts);
    }

    @Override
    public void saveContact(String contact) throws IOException {

        Set<String> contacts = new HashSet<>(getAllContacts());
        contacts.add(contact);
        saveContacts(contacts);
    }

    @Override
    public void deleteContact(String contact) throws IOException {
        Set<String> allContacts = new HashSet<>(getAllContacts());
        if (allContacts.remove(contact)) {
            System.out.println("Контакт успешно удален");
            saveContacts(allContacts);
        };

    }

    private void saveContacts(Set<String> contacts) throws IOException {

        String outputLine = String.join("\r\n", contacts);
        outputLine = (outputLine.startsWith("\r\n")) ? outputLine.substring(2) : outputLine;
        File file = new File(path);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(outputLine);

        bufferedWriter.close();

    }
}
