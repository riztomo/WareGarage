package com.example.waregarage;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.waregarage.model.Employee;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateWarehouseActivity extends AppCompatActivity {

    RetrofitInterface retroInt;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_warehouse);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        EditText warehouseName = findViewById(R.id.createWarehouseName);
        EditText warehouseAddress = findViewById(R.id.createWarehouseAddress);
        EditText warehouseCapacity = findViewById(R.id.createWarehouseCapacity);
        Button createWarehouseButton = findViewById(R.id.createWarehouseButton);

        createWarehouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();

                map.put("address", warehouseAddress.getText().toString());
                map.put("name", warehouseName.getText().toString());
                map.put("company_id", valueOf(MainActivity.loggedEmployee.company_id));
                map.put("rem_capacity", warehouseCapacity.getText().toString());

                Call<Void> call = retroInt.createWarehouse(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        System.out.println(map);

                        if (response.code() == 200) {
                            Intent intent = new Intent(CreateWarehouseActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else if (response.code() == 400) {
                            Toast.makeText(CreateWarehouseActivity.this, "Internal server error.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CreateWarehouseActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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