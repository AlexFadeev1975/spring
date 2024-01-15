package org.example.test.controllers;

public final class ApiCollection {

    public static String CREATE_BOOK = "/books/api/create";

    public static String UPDATE_BOOK = "/books/api/update";

    public static String FIND_BOOK_BY_AUTHOR_AND_TITLE = "/books/api/book/{author}/{title}";

    public  static String FIND_BOOKS_BY_CATEGORY = "/books/api/books/{category}";

    public  static String DELETE_BOOK = "/books/api/delete/{id}";
}
