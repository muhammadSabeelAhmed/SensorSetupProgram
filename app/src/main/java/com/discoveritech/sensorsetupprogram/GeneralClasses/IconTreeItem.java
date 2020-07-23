package com.discoveritech.sensorsetupprogram.GeneralClasses;

public class IconTreeItem {
    public String name;
    public String value;

    public IconTreeItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
