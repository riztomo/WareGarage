package com.example.waregarage;

import static java.lang.String.valueOf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.waregarage.model.CommodityType;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSectionActivity extends AppCompatActivity {

    RetrofitInterface retroInt;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_warehouse);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        EditText sectionName = findViewById(R.id.createWarehouseName);
        EditText sectionCapacity = findViewById(R.id.createWarehouseCapacity);
        Spinner sectionType = findViewById(R.id.createSectionType);
        sectionType.setAdapter(new ArrayAdapter<CommodityType>(this, android.R.layout.simple_spinner_item, CommodityType.values()));
        Button createWarehouseButton = findViewById(R.id.createWarehouseButton);

        createWarehouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.parseInt(sectionCapacity.getText().toString()) <= WarehouseActivity.lookedWarehouse.rem_capacity) {

                    HashMap<String, String> map = new HashMap<>();

                    map.put("warehouse_id", valueOf(WarehouseActivity.lookedWarehouse.id));
                    map.put("name", sectionName.getText().toString());
                    map.put("commodity_type", sectionType.getSelectedItem().toString());
                    map.put("rem_capacity", sectionCapacity.getText().toString());

                    Call<Void> call = retroInt.createSection(map);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            if (response.code() == 200) {
                                WarehouseActivity.lookedWarehouse.rem_capacity -= Integer.parseInt(sectionCapacity.getText().toString());
                                Intent intent = new Intent(CreateSectionActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else if (response.code() == 400) {
                                Toast.makeText(CreateSectionActivity.this, "Internal server error.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(CreateSectionActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateSectionActivity.this, "Section capacity exceeding remaining warehouse capacity.", Toast.LENGTH_LONG).show();
                }
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
                intent = new Intent(this, WarehouseActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}