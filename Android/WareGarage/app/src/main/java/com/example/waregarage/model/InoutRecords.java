package com.example.waregarage.model;

import java.sql.Date;
import com.google.gson.annotations.SerializedName;


public class InoutRecords extends Serializable {
    public int company_id;
    public String warehouse;
    public String section;
    public String commodity_id;
    public String name;
    public CommodityType type;
    public int volume;
    public Date in_time;
    public Date out_time;
}
