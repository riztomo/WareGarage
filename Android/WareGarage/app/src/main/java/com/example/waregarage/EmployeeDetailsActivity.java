package com.example.waregarage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waregarage.model.Company;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDetailsActivity extends AppCompatActivity {

    RetrofitInterface retroInt;
    Context mContext;
    protected static Class previousAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        TextView name = findViewById(R.id.profileName);
        name.setText(MainActivity.loggedEmployee.name);
        TextView username = findViewById(R.id.profileUsername);
        username.setText(MainActivity.loggedEmployee.username);
        TextView role = findViewById(R.id.profileRole);
        role.setText((MainActivity.loggedEmployee.role).toString());
        TextView company = findViewById(R.id.profileCompany);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("company_id", MainActivity.loggedEmployee.company_id);
        Call<Company> call = retroInt.getCompany(map);
        call.enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {

                if (response.code() == 200) {
                    company.setText(response.body().name);
                } else if (response.code() == 400) {
                    Toast.makeText(EmployeeDetailsActivity.this, "Cannot get company name.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                Toast.makeText(EmployeeDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
                intent = new Intent(this, previousAct);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}