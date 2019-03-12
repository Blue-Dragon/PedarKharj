package com.example.pedarkharj.profile;

public class URLs {
    /**
     * WHY THIS CLASS?
     * We need to define our URL that call the API of server-side.
     */
//    private static final String ROOT_URL = "http://192.168.43.103/JavaTPoint/registrationapi.php?apicall="; //Laptop
    private static final String ROOT_URL = "http://192.168.43.54:8080/pedarKharj/registrationapi.php?apicall=";  //PC myhotspot
//    private static final String ROOT_URL = "http://192.168.1.34/pedarKharj/registrationapi.php?apicall=";  //PC Hamishe Sabz
    public static final String URL_REGISTER = ROOT_URL + "signup";
    public static final String URL_LOGIN= ROOT_URL + "login";

}
