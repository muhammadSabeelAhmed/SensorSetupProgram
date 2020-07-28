package com.discoveritech.sensorsetupprogram.GeneralClasses;

import java.util.ArrayList;
import java.util.List;

public class Global {
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
}
