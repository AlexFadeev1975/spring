package org.example.handler;

public class ApiCollection {


    public static String CREATE_USER = "/users/api/v1/create";
    public static String GET_USERS = "/users/api/v1/users";
    public static String GET_USER = "/users/api/v1/{id}";
    public static String UPDATE_USER = "/users/api/v1/update";
    public static String DELETE_USER = "/users/api/v1/{id}";


    public static String CREATE_TASK = "/tasks/api/v1/create";
    public static String FIND_TASK = "/tasks/api/v1/{id}";
    public static String UPDATE_TASK = "/tasks/api/v1/update";
    public static String GET_TASKS = "/tasks/api/v1/tasks";
    public static String ADD_OBSERVER = "/tasks/api/v1/observer";
    public static String DELETE_TASK = "/tasks/api/v1/{id}";
}
