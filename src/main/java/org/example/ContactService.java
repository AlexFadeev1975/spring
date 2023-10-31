package org.example;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class ContactService {

    private final ContactsUtils contactsUtils;

    public ContactService(ContactsUtils contactsUtils) {
        this.contactsUtils = contactsUtils;
    }

    public void getMainMenu () throws IOException {

        System.out.println("Главное меню");
        System.out.println();
        System.out.println("1 - Просмотреть все контакты");
        System.out.println("2 - Найти контакты");
        System.out.println("3 - Создать новый конракт");
        System.out.println("4 - Удалить контакт");
        System.out.println("5 - Выход");
        System.out.println();
        System.out.print("Введите номер выбранного раздела :");

        Scanner scanner = new Scanner(System.in);
        boolean key = true;
        String input = "";
        do {

            input = scanner.next();
            if (input.matches("[1-5]")) {
                key = false;
            } else {
                System.out.println("Неверный формат ввода");
            }
        }
        while (key);

        switch (input) {
            case ("1"): showContacts(contactsUtils.getAllContacts().stream().toList());
            case ("2"): getSearchInterface();
            case ("3"): createContact();
            case ("4"): deleteContact();
            case ("5"):
                System.out.println("Работа программы завершена");
                System.exit(0);
        }
    }

    public void getSearchInterface() throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Контакты");

        String input = "";
        do {
            System.out.print("Введите любые данные контакта :");
            input = scanner.next();
            if (input.isEmpty()) {
                System.out.println("Пустое поле ввода");
            }
        } while (input.isEmpty());

        List<String> contacts = searchContacts(input);

        if (contacts.isEmpty()) {
            System.out.println("Контакты не найдены.");

            getMainMenu();


        } else {
            showContacts(contacts);
        }
    }

    public List<String> searchContacts(String input) throws IOException {

        return contactsUtils.getAllContacts().stream().filter(x -> x.contains(input)).toList();
    }

    public void createContact() throws IOException {

        Pattern patternFio = Pattern.compile("^[A-ЯЁ][а-яё]+\\s[A-ЯЁ][а-яё]+\\s[A-ЯЁ][а-яё]+$"
                , Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);


        String fio = getInput(patternFio, "фио полностью ");

        Pattern patternNum = Pattern.compile("\\d{11}"
                , Pattern.UNICODE_CHARACTER_CLASS);
        String phone = getInput(patternNum, "11-значный номер телефона без пробелов");

        Pattern patternEmail = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?``{|}~^.-]+@[a-zA-Z0-9.-]+$"
                , Pattern.UNICODE_CHARACTER_CLASS);
        String email = getInput(patternEmail, "адрес электронной почты");

        saveContact(fio + ";" + phone + ";" + email);

    }

    public void saveContact(String contact) throws IOException {

        contactsUtils.saveContact(contact);

        System.out.println("Контакт :" + contact + "успешно сохранен");

        getMainMenu();

    }

    public void showContacts(List<String> listContacts) throws IOException {

        Map<Integer, String> contactMap = new HashMap<>();
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        String input = "";
        System.out.println();
        System.out.println("Контакты");;
        for (int i = 1; i <= listContacts.size(); i++) {

            contactMap.put(i, listContacts.get(i - 1));
        }

        contactMap.forEach((key, value) -> {
            System.out.println(key + " - " + value);
        });
        System.out.println();

        do {
            System.out.print("Введите q для выхода :");
            input = scanner.next().toLowerCase();
        } while (!input.equals("q"));


        getMainMenu();

    }

    public String getInput(Pattern pattern, String nameItem) {


        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        String input = "";
        do {
            System.out.print("Введите " + nameItem + " :");

            input = scanner.next();

            Matcher m = pattern.matcher(input);
            if (!m.find()) {
                System.out.println("Неверный формат либо пустая строка ввода");
                input = "";
            }
        } while (input.isEmpty());

        return input;
    }

    public void deleteContact() throws IOException {

          String contactForDeleting = "";
          Scanner scanner = new Scanner(System.in);
          String input = "";

        do {
            System.out.println("Введите Email контакта для удаления " +
                    "либо ведите 'q' для выхода :");
            input = scanner.next();

            if (input.equals("q")) {
                input = "exit";

            }

            if (input.matches("^[a-zA-Z0-9_!#$%&'*+/=?``{|}~^.-]+@[a-zA-Z0-9.-]+$")) {

                List<String> listContact = searchContacts(input);
                contactForDeleting = (!listContact.isEmpty()) ? listContact.get(0) : null;

                if (contactForDeleting != null) {
                    contactsUtils.deleteContact(contactForDeleting);


                    input = "exit";
                } else {
                    System.out.println("Контакт не найден");
                }

            } else if (!input.equals("exit")) {
                System.out.println("Неверный формат ввода");
                input = "";
            }
        } while (!input.equals("exit"));

        getMainMenu();
    }
}
