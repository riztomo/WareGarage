package com.example.waregarage.model;

import com.google.gson.annotations.SerializedName;

public class Warehouse extends Serializable {
    public String address;
    public String name;
    public int company_id;
    public int manager_id;
    public int rem_capacity;
}
