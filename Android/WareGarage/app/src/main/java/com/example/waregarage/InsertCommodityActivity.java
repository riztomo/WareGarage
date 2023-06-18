package com.example.waregarage;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertCommodityActivity extends AppCompatActivity {

    RetrofitInterface retroInt;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_commodity);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        EditText name = findViewById(R.id.insertCommodityName);
        EditText volume = findViewById(R.id.insertCommodityVolume);

        Button insert = findViewById(R.id.insertCommodityButton);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.parseInt(volume.getText().toString()) <= SectionActivity.lookedSection.rem_capacity) {

                    HashMap<String, String> map = new HashMap<>();

                    map.put("company_id", valueOf(MainActivity.loggedEmployee.company_id));
                    map.put("warehouse_id", valueOf(WarehouseActivity.lookedWarehouse.id));
                    map.put("section_id", valueOf(SectionActivity.lookedSection.id));
                    map.put("name", name.getText().toString());
                    map.put("type", String.valueOf(SectionActivity.lookedSection.commodity_type));
                    map.put("volume", volume.getText().toString());
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
                    String formattedDate = df.format(c);
                    map.put("in_time", formattedDate);

                    Call<Void> call = retroInt.commodityIn(map);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            if (response.code() == 200) {
                                SectionActivity.lookedSection.rem_capacity -= Integer.parseInt(volume.getText().toString());
                                Intent intent = new Intent(InsertCommodityActivity.this, SectionActivity.class);
                                startActivity(intent);
                            } else if (response.code() == 400) {
                                Toast.makeText(InsertCommodityActivity.this, "Internal server error.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(InsertCommodityActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(InsertCommodityActivity.this, "Commodity volume exceeding remaining section capacity.", Toast.LENGTH_LONG).show();
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
                intent = new Intent(this, SectionActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}