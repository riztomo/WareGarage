package com.example.waregarage.request;

import com.example.waregarage.model.Commodity;
import com.example.waregarage.model.Company;
import com.example.waregarage.model.Employee;
import com.example.waregarage.model.InoutRecords;
import com.example.waregarage.model.Section;
import com.example.waregarage.model.Warehouse;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<Employee> login(@Body HashMap<String, String> map);

    @POST("/register-owner")
    Call<Void> registerOwner(@Body HashMap<String, String> map);

    @POST("/register-manager")
    Call<Void> registerManager(@Body HashMap<String, String> map);

    @POST("/get-company-name")
    Call<Company> getCompany(@Body HashMap<String, Integer> map);

    @POST("/company")
    Call<List<Warehouse>> companyContents(@Body HashMap<String, Integer> map);

    @POST("/create-warehouse")
    Call<Void> createWarehouse(@Body HashMap<String, String> map);

    @POST("/delete-warehouse")
    Call<Void> deleteWarehouse(@Body HashMap<String, Integer> map);

    @POST("/get-manager")
    Call<List<Employee>> getManager(@Body HashMap<String, Integer> map);

    @POST("/get-manager-name")
    Call<Employee> getManagerName(@Body HashMap<String, Integer> map);

    @POST("/assign-manager")
    Call<Void> assignManager(@Body HashMap<String, Integer> map);

    @POST("/unassign-manager")
    Call<Void> unassignManager(@Body HashMap<String, Integer> map);

    @POST("/warehouse")
    Call<List<Section>> warehouseContents(@Body HashMap<String, Integer> map);

    @POST("/create-section")
    Call<Void> createSection(@Body HashMap<String, String> map);

    @POST("/delete-section")
    Call<Void> deleteSection(@Body HashMap<String, Integer> map);

    @POST("/section")
    Call<List<Commodity>> sectionContents(@Body HashMap<String, Integer> map);

    @POST("/commodity-in")
    Call<Void> commodityIn(@Body HashMap<String, String> map);

    @POST("/commodity-out")
    Call<Void> commodityOut(@Body HashMap<String, String> map);

    @POST("/inout-records")
    Call<List<InoutRecords>> inOut(@Body HashMap<String, String> map);}
