package com.discoveritech.sensorsetupprogram.TreeSystem;

import java.util.ArrayList;

public class HeaderInfo {

    private String name;
    private String date_time;
    private ArrayList<DetailInfo> productList = new ArrayList<DetailInfo>();;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<DetailInfo> getProductList() {
        return productList;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public void setProductList(ArrayList<DetailInfo> productList) {
        this.productList = productList;
    }

}
