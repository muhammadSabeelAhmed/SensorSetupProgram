package com.discoveritech.sensorsetupprogram.GeneralClasses;

import com.macasaet.fernet.Token;

public class Constants {
    public static int SENDING_PORT = 5000;
    public static int RECEVING_PORT = 5001;

    public static String OPERATION="";
    private Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
