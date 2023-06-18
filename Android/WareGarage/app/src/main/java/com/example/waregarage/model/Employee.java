package com.example.waregarage.model;

import com.google.gson.annotations.SerializedName;

public class Employee extends Serializable {
    public String username;
    public String password;
    public String name;
    public int company_id;
    public Role role;
}
