package com.discoveritech.sensorsetupprogram.GeneralClasses;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHandler {

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    private static final String SENDING_PORT = "";
    private static final String RECEIVING_PORT = "";

    public PreferencesHandler(Context context) {
        pref = context.getSharedPreferences("ssp", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public String getSendingPort() {
        return pref.getString(SENDING_PORT, "5000");
    }

    public void setSendingPort(String port) {
        editor.putString(SENDING_PORT, port);
        editor.apply();
        editor.commit();
    }

    public String getReceivingPort() {
        return pref.getString(RECEIVING_PORT, "5001");
    }

    public void setReceivingPort(String upwd) {
        editor.putString(RECEIVING_PORT, upwd);
        editor.apply();
        editor.commit();
    }

}

