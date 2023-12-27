package org.example.test.controllers;

public final class ApiCollection {

    static String CREATE_USER = "/users/api/create";
    static String GET_ALL_USERS = "/users/api/list";
    static String UPDATE_USER = "/users/api/update";
    static String DELETE_USER = "/users/api/delete/{id}";


    static String CREATE_NEWS = "/news/api/create";
    static String GET_ALL_NEWS = "/news/api/list";
    static String UPDATE_NEWS = "/news/api/update";
    static String DELETE_NEWS = "/news/api/delete/{id}";


    static String CREATE_COMMENT = "/comments/api/create/{newsId}";
    static String GET_ALL_COMMENTS = "/comments/api/list/{newsId}";
    static String UPDATE_COMMENT = "/comments/api/update";
    static String DELETE_COMMENT = "/comments/api/delete/{commentId}";

    static String CREATE_CATEGORY = "/category/api/create";
    static String GET_ALL_CATEGORY = "/category/api/list";
    static String UPDATE_CATEGORY = "/category/api/update";
    static String DELETE_CATEGORY = "/category/api/delete/{id}";

}
