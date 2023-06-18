package com.example.waregarage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.waregarage.model.Employee;
import com.example.waregarage.model.Section;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignManagerActivity extends AppCompatActivity {

    protected static List<Employee> managers;

    RetrofitInterface retroInt;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_manager);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        ListView managerList = findViewById(R.id.assignManagerList);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("company_id", MainActivity.loggedEmployee.company_id);
        Call<List<Employee>> call1 = retroInt.getManager(map);
        call1.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call1, Response<List<Employee>> response) {

                if (response.code() == 200) {
                    AssignManagerActivity.managers = response.body();
                } else if (response.code() == 400) {
                    Toast.makeText(AssignManagerActivity.this, "Cannot get managers.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call1, Throwable t) {
                Toast.makeText(AssignManagerActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        if (managers != null) {
            List<String> warehouseNames = managers.stream().map(names -> names.name).collect(Collectors.toList());

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, warehouseNames);
            managerList.setAdapter(arrayAdapter);
        }

        managerList.setClickable(true);
        managerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                HashMap<String, Integer> map = new HashMap<>();

                map.put("manager_id", managers.get(position).id);
                map.put("warehouse_id", WarehouseActivity.lookedWarehouse.id);

                Call<Void> call = retroInt.assignManager(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            Intent intent = new Intent(AssignManagerActivity.this, WarehouseActivity.class);
                            startActivity(intent);
                        } else if (response.code() == 400) {
                            Toast.makeText(AssignManagerActivity.this, "Internal error.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AssignManagerActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    /**
     * Defines the top option menu item selection action.
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person_button:
                Intent intent = new Intent(this, EmployeeDetailsActivity.class);
                EmployeeDetailsActivity.previousAct = AssignManagerActivity.class;
                this.startActivity(intent);
                return true;
            case R.id.home_button:
                intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.logout_button:
                intent = new Intent(this, LoginActivity.class);
                MainActivity.loggedEmployee = null;
                this.startActivity(intent);
                return true;
            case R.id.inout_button:
                intent = new Intent(this, InoutLogActivity.class);
                InoutLogActivity.previousAct = AssignManagerActivity.class;
                this.startActivity(intent);
                return true;
            case R.id.back_button:
                intent = new Intent(this, WarehouseActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}