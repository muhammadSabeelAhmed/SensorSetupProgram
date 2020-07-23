package com.discoveritech.sensorsetupprogram.GeneralClasses;

import com.macasaet.fernet.Token;

public class Constants {
    public static int PORT = 5000;
    private Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
