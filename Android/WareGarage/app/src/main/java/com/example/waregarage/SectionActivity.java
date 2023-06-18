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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waregarage.model.Commodity;
import com.example.waregarage.model.Section;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionActivity extends AppCompatActivity {

    protected static Section lookedSection;
    protected static List<Commodity> commodities;
    RetrofitInterface retroInt;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        TextView sectionTitle = findViewById(R.id.sectionName);
        sectionTitle.setText(lookedSection.name);
        ListView sectionCommodityList = findViewById(R.id.sectionCommodityList);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("section_id", SectionActivity.lookedSection.id);
        Call<List<Commodity>> call1 = retroInt.sectionContents(map);
        call1.enqueue(new Callback<List<Commodity>>() {
            @Override
            public void onResponse(Call<List<Commodity>> call1, Response<List<Commodity>> response) {

                if (response.code() == 200) {
                    SectionActivity.commodities = response.body();
                } else if (response.code() == 400) {
                    Toast.makeText(SectionActivity.this, "Internal Error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Commodity>> call1, Throwable t) {
                Toast.makeText(SectionActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        if (commodities != null) {
            List<String> commodityNames = commodities.stream().map(names -> names.name).collect(Collectors.toList());

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, commodityNames);
            sectionCommodityList.setAdapter(arrayAdapter);
        }

        sectionCommodityList.setClickable(true);
        sectionCommodityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CommodityDetailActivity.selectedCommodity = commodities.get(position);
                Intent intent = new Intent(SectionActivity.this, CommodityDetailActivity.class);
                startActivity(intent);
            }
        });

        Button deleteSection = findViewById(R.id.sectionDeleteSection);
        Button insertCommodity = findViewById(R.id.sectionInsertCommodity);

        insertCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(SectionActivity.this, InsertCommodityActivity.class);
                startActivity(registerIntent);
            }
        });

        deleteSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Integer> map = new HashMap<>();

                map.put("section_id", SectionActivity.lookedSection.id);
                map.put("rem_capacity", SectionActivity.lookedSection.rem_capacity);
                map.put("warehouse_id", WarehouseActivity.lookedWarehouse.id);

                Call<Void> call = retroInt.deleteSection(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            WarehouseActivity.lookedWarehouse.rem_capacity += SectionActivity.lookedSection.rem_capacity;
                            SectionActivity.lookedSection = null;
                            Intent registerIntent = new Intent(SectionActivity.this, WarehouseActivity.class);
                            startActivity(registerIntent);
                        } else if (response.code() == 400) {
                            Toast.makeText(SectionActivity.this, "Internal error.", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 500) {
                            Toast.makeText(SectionActivity.this, "Commodity(ies) still exist.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(SectionActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
                intent = new Intent(this, WarehouseActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}