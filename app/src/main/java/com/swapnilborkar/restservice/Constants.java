package com.swapnilborkar.restservice;

/**
 * Created by SWAPNIL on 24-07-2016.
 */
public class Constants {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 100000;
    public static final int STATUS_ERROR = 400;
    public static final int STATUS_UNAUTHORIZED = 401;


    //APPLICATION KEY AND SECRET FOR API ACCESS
    public static final String APP_KEY = "ENTER_YOUR_APP_KEY_HERE";
    public static final String APP_SECRET = "ENTER_YOUR_SECRET_HERE";

    //ENDPOINT FOR API ACCESS
    public static final String END_POINT = "http://YOUR_DOMAIN/public_html/api";
    public static final String LOGIN_URL = END_POINT + "/login.php";
    public static final String SIGNUP_URL = END_POINT + "/signup.php";
    public static final String INFO_URL = END_POINT + "/info.php";
    public static final String UPDATE_URL  = END_POINT + "/update.php";
    public static final String DELETE_URL = END_POINT + "/delete.php";
    public static final String RESET_URL = END_POINT + "/reset.php";

    //CONSTANTS USED FOR JSON PARSING
    public static final String AUTHORIZATION = "Authorization";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String GRANT_TYPE = "grant_type";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ACCESS = "access";
    public static final String INFO = "info";
    public static final String STATUS = "status";
    public static final String MESSAGE = "msg";
    public static final String ID = "id";
    public static final String ID_INFO = "ID";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String NOTE = "note";
    public static final String NAME ="name";

    public static final String CONNECTION_MESSAGE = "No Internet Connection!";

}
