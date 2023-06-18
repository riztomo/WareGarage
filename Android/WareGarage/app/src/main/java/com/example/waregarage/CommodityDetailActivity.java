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
import android.widget.TextView;
import android.widget.Toast;

import com.example.waregarage.model.Commodity;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommodityDetailActivity extends AppCompatActivity {

    protected static Commodity selectedCommodity;
    RetrofitInterface retroInt;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_detail);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        TextView name = findViewById(R.id.commodityName);
        name.setText(selectedCommodity.name);
        TextView volume = findViewById(R.id.commodityVolume);
        volume.setText(selectedCommodity.volume);
        TextView type = findViewById(R.id.commodityType);
        type.setText((SectionActivity.lookedSection.commodity_type).toString());
        TextView id = findViewById(R.id.commodityInternalId);
        id.setText(selectedCommodity.commodity_id);
        Button release = findViewById(R.id.commodityRelease);

        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("commodity_id", selectedCommodity.commodity_id);
                map.put("section_id", valueOf(selectedCommodity.section_id));
                map.put("volume", valueOf(selectedCommodity.volume));
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
                String formattedDate = df.format(c);
                map.put("out_date", formattedDate);

                Call<Void> call1 = retroInt.commodityOut(map);
                call1.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call1, Response<Void> response) {

                        if (response.code() == 200) {
                            SectionActivity.lookedSection.rem_capacity += selectedCommodity.volume;
                            selectedCommodity = null;
                            Intent intent = new Intent(CommodityDetailActivity.this, SectionActivity.class);
                            startActivity(intent);
                        } else if (response.code() == 400) {
                            Toast.makeText(CommodityDetailActivity.this, "Internal Error.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call1, Throwable t) {
                        Toast.makeText(CommodityDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
                intent = new Intent(this, SectionActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}