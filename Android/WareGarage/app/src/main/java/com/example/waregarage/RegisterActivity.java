package com.example.waregarage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.waregarage.model.Employee;
import com.example.waregarage.model.Role;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    RetrofitInterface retroInt;
    Context mContext;
    EditText name, username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        name = findViewById(R.id.editTextRegisterName);
        username = findViewById(R.id.editTextRegisterUsername);
        password = findViewById(R.id.editTextRegisterPassword);

        Spinner roleSpinner = findViewById(R.id.spinnerRegisterRole);
        roleSpinner.setAdapter(new ArrayAdapter<Role>(this, android.R.layout.simple_spinner_item, Role.values()));

        EditText companyName = findViewById(R.id.editTextRegisterCompanyName);
        EditText companyId = findViewById(R.id.editTextRegisterCompanyId);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (roleSpinner.getSelectedItem() == Role.OWNER) {
                    companyName.setVisibility(View.VISIBLE);
                    companyId.setVisibility(View.INVISIBLE);
                } else {
                    companyName.setVisibility(View.INVISIBLE);
                    companyId.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                companyName.setVisibility(View.INVISIBLE);
                companyId.setVisibility(View.INVISIBLE);
            }
        });

        Button register = findViewById(R.id.buttonRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roleSpinner.getSelectedItem() == Role.OWNER) {

                    HashMap<String, String> map = new HashMap<>();

                    map.put("username", username.getText().toString());
                    map.put("password", password.getText().toString());
                    map.put("name", name.getText().toString());
                    map.put("company_name", companyName.getText().toString());

                    Call<Void> call = retroInt.registerOwner(map);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else if (response.code() == 400) {
                                Toast.makeText(RegisterActivity.this, "Most likely registered username already in use.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                } else {

                    HashMap<String, String> map = new HashMap<>();

                    map.put("username", username.getText().toString());
                    map.put("password", password.getText().toString());
                    map.put("name", name.getText().toString());
                    map.put("company_id", companyId.getText().toString());

                    Call<Void> call = retroInt.registerManager(map);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            if (response.code() == 200) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else if (response.code() == 400) {
                                Toast.makeText(RegisterActivity.this, "Most likely registered username already in use.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }
}