package com.example.waregarage;

import static java.lang.String.valueOf;
import static java.sql.Types.NULL;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waregarage.model.Company;
import com.example.waregarage.model.Employee;
import com.example.waregarage.model.Role;
import com.example.waregarage.model.Warehouse;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    protected static Employee loggedEmployee;
    RetrofitInterface retroInt;
    Context mContext;

    protected static List<Warehouse> warehouses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        // get company name

        TextView mainCompanyName = findViewById(R.id.mainCompanyName);
        TextView id = findViewById(R.id.mainCompanyID);
        id.setText(valueOf(MainActivity.loggedEmployee.company_id));

        HashMap<String, Integer> map = new HashMap<>();
        map.put("company_id", MainActivity.loggedEmployee.company_id);
        Call<Company> call = retroInt.getCompany(map);
        call.enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {

                if (response.code() == 200) {
                    mainCompanyName.setText(response.body().name);
                } else if (response.code() == 400) {
                    Toast.makeText(MainActivity.this, "Cannot get company name.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // gets warehouses

        ListView mainWarehouseList = findViewById(R.id.mainWarehouseList);

        map = new HashMap<>();
        map.put("company_id", MainActivity.loggedEmployee.company_id);
        Call<List<Warehouse>> call1 = retroInt.companyContents(map);
        call1.enqueue(new Callback<List<Warehouse>>() {
            @Override
            public void onResponse(Call<List<Warehouse>> call1, Response<List<Warehouse>> response) {

                if (response.code() == 200) {
                    MainActivity.warehouses = response.body();
                } else if (response.code() == 400) {
                    Toast.makeText(MainActivity.this, "Internal Error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Warehouse>> call1, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        TextView yourWarehouse = findViewById(R.id.mainWarehouseListTitle);
        if (loggedEmployee.role == Role.MANAGER) {
            yourWarehouse.setText("Your assigned warehouse(s)");
            warehouses = warehouses.stream().filter(p -> p.manager_id > loggedEmployee.id).collect(Collectors.toList());
        } else {
            yourWarehouse.setText("Warehouse list");
        }

        if (warehouses != null) {
            List<String> warehouseNames = warehouses.stream().map(names -> names.name).collect(Collectors.toList());

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, warehouseNames);
            mainWarehouseList.setAdapter(arrayAdapter);
        }

        // see warehouse on list

        mainWarehouseList.setClickable(true);
        mainWarehouseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                WarehouseActivity.lookedWarehouse = warehouses.get(position);
                Intent intent = new Intent(MainActivity.this, WarehouseActivity.class);
                startActivity(intent);
            }
        });

        // create a new warehouse

        Button createWarehouse = findViewById(R.id.mainCreateWarehouse);
        createWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this, CreateWarehouseActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu, menu);

        menu.findItem(R.id.back_button).setVisible(false);

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
            default: return super.onOptionsItemSelected(item);
        }
    }
}