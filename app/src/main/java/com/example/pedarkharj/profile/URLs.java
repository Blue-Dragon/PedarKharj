package com.example.pedarkharj.profile;

public class URLs {

    /**
     * WHY THIS CLASS?
     * We need to define our URL that call the API of server-side.
     */

    // ROOT URL
//    private static final String ROOT_URL = "http://192.168.43.103:8080/JavaTPoint/";//Laptop
    private static final String ROOT_URL = "http://192.168.43.54:8080/pedarKharj/";  //PC myhotspot
//    private static final String ROOT_URL = "http://192.168.1.39:8080/pedarKharj/";  //PC Hamishe Sabz

    public static final String LOGIN_OR_OUT = ROOT_URL + "registrationapi.php?apicall=";

    public static final String URL_REGISTER = LOGIN_OR_OUT + "signup";
    public static final String URL_LOGIN = LOGIN_OR_OUT + "login";
    public static final String URL_UPDATE = LOGIN_OR_OUT + "update";
    public static final String URL_GET_USER_INFO = LOGIN_OR_OUT + "get_info";

    public static final String URL_IMAGE_DIR = ROOT_URL+ "profile_pics/";


}
