package com.example.waregarage.model;

import com.google.gson.annotations.SerializedName;

public class Section extends Serializable {
    public int warehouse_id;
    public String name;
    public CommodityType commodity_type;
    public int rem_capacity;
}
