package org.example.test.controller;

public final class ApiCollection {

    static String CREATE_HOTEL = "/hotels/api/create";

    static String GET_ALL_HOTELS = "/hotels/api/list";

    static String UPDATE_HOTEL = "/hotels/api/update";

    static String RATE_HOTEL = "/hotels/api/rating";

    static String DELETE_HOTEL = "/hotels/api/delete/{id}";

    static String CREATE_ROOM = "/rooms/api/create";

    static String GET_ROOM_BY_ID = "/rooms/api/room/{id}";

    static String UPDATE_ROOM = "/rooms/api/update";

    static String DELETE_ROOM = "/rooms/api/delete/{id}";

    static String CREATE_BOOKING = "/booking/api/create";

    static String GET_ALL_BOOKING_BY_USER = "/booking/api/list/user";


    static String CREATE_USER = "/users/api/create";

    static String GET_ALL_USERS = "/users/api/list";

    static String GET_USER_BY_USERNAME = "/users/api/search/{userName}";

    static String GET_USER_BY_ID = "/users/api/find/{id}";

    static String UPDATE_USER = "/users/api/update";

    static String DELETE_USER_BY_ID = "/users/api/delete/{id}";

}


