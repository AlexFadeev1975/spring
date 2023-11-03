package org.example;

import org.example.config.DefaultConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ApplicationContext context = new AnnotationConfigApplicationContext(DefaultConfig.class);

        ContactService contactService = context.getBean(ContactService.class);

        contactService.getMainMenu();


    }
}
