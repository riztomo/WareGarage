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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waregarage.model.Company;
import com.example.waregarage.model.Employee;
import com.example.waregarage.model.Role;
import com.example.waregarage.model.Section;
import com.example.waregarage.model.Warehouse;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarehouseActivity extends AppCompatActivity {

    protected static Warehouse lookedWarehouse;
    protected static List<Section> sections;
    RetrofitInterface retroInt;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        TextView warehouseName = findViewById(R.id.warehouseName);
        warehouseName.setText(lookedWarehouse.name);

        // gets sections

        ListView warehouseSectionList = findViewById(R.id.warehouseSectionList);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("company_id", MainActivity.loggedEmployee.company_id);
        Call<List<Section>> call1 = retroInt.warehouseContents(map);
        call1.enqueue(new Callback<List<Section>>() {
            @Override
            public void onResponse(Call<List<Section>> call1, Response<List<Section>> response) {

                if (response.code() == 200) {
                    WarehouseActivity.sections = response.body();
                } else if (response.code() == 400) {
                    Toast.makeText(WarehouseActivity.this, "Internal Error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Section>> call1, Throwable t) {
                Toast.makeText(WarehouseActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        if (sections != null) {
            List<String> warehouseNames = sections.stream().map(names -> names.name).collect(Collectors.toList());

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, warehouseNames);
            warehouseSectionList.setAdapter(arrayAdapter);
        }

        warehouseSectionList.setClickable(true);
        warehouseSectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SectionActivity.lookedSection = sections.get(position);
                Intent intent = new Intent(WarehouseActivity.this, SectionActivity.class);
                startActivity(intent);
            }
        });

        TextView warehouseManager = findViewById(R.id.warehouseManager);
        Button warehouseAssignManager = findViewById(R.id.warehouseAssignManager);
        Button warehouseUnassignManager = findViewById(R.id.warehouseUnassignManager);

        map = new HashMap<>();
        map.put("manager_id", WarehouseActivity.lookedWarehouse.manager_id);
        Call<Employee> call2 = retroInt.getManagerName(map);
        call2.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call2, Response<Employee> response) {

                if (response.code() == 200) {
                    if (response.body().name == null) {
                        warehouseManager.setText("None");
                        warehouseAssignManager.setVisibility(View.VISIBLE);
                        warehouseUnassignManager.setVisibility(View.INVISIBLE);
                    } else {
                        warehouseManager.setText(response.body().name);
                        warehouseAssignManager.setVisibility(View.INVISIBLE);

                        if (MainActivity.loggedEmployee.role == Role.MANAGER)
                            warehouseUnassignManager.setVisibility(View.VISIBLE);
                        else
                            warehouseUnassignManager.setVisibility(View.INVISIBLE);
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(WarehouseActivity.this, "Cannot get manager name.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call2, Throwable t) {
                Toast.makeText(WarehouseActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Button createSection = findViewById(R.id.warehouseCreateSection);
        Button deleteWareHouse = findViewById(R.id.warehouseDeleteWarehouse);

        createSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(WarehouseActivity.this, CreateSectionActivity.class);
                startActivity(registerIntent);
            }
        });

        deleteWareHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Integer> map = new HashMap<>();

                map.put("warehouse_id", WarehouseActivity.lookedWarehouse.id);

                Call<Void> call = retroInt.deleteWarehouse(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            WarehouseActivity.lookedWarehouse = null;
                            Intent registerIntent = new Intent(WarehouseActivity.this, MainActivity.class);
                            startActivity(registerIntent);
                        } else if (response.code() == 400) {
                            Toast.makeText(WarehouseActivity.this, "Internal error.", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 500) {
                            Toast.makeText(WarehouseActivity.this, "Section(s) still exist.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(WarehouseActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

        warehouseAssignManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(WarehouseActivity.this, AssignManagerActivity.class);
                startActivity(registerIntent);
            }
        });

        warehouseUnassignManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Integer> map = new HashMap<>();

                map.put("warehouse_id", WarehouseActivity.lookedWarehouse.id);

                Call<Void> call = retroInt.unassignManager(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            WarehouseActivity.lookedWarehouse.manager_id = Integer.parseInt(null);
                            Intent registerIntent = new Intent(WarehouseActivity.this, MainActivity.class);
                            startActivity(registerIntent);
                        } else if (response.code() == 400) {
                            Toast.makeText(WarehouseActivity.this, "Internal error.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(WarehouseActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
                EmployeeDetailsActivity.previousAct = this.getClass();
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
                InoutLogActivity.previousAct = this.getClass();
                this.startActivity(intent);
                return true;
            case R.id.back_button:
                intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}