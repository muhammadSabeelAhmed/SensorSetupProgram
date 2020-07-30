package com.discoveritech.sensorsetupprogram.GeneralClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class Global {
    public static ArrayList<String> myPorts = new ArrayList<>();

    public static void removeDuplicates(List<?> list) {
        int count = list.size();

        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (list.get(i).equals(list.get(j))) {
                    list.remove(j--);
                    count--;
                }
            }
        }
    }

    public static void changeActivity(Context context, Activity activity) {

        Intent in = new Intent();
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.setClass(context, activity.getClass());
        context.startActivity(in);

    }
}
