package com.example.waregarage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    RetrofitInterface retroInt;
    Context mContext;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        MainActivity.loggedEmployee = null;

        username = findViewById(R.id.editTextLoginUsername);
        password = findViewById(R.id.editTextLoginPassword);

        Button register = findViewById(R.id.buttonRegisterNow);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        Button login = findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();

                map.put("username", username.getText().toString());
                map.put("password", password.getText().toString());

                Call<Employee> call = retroInt.login(map);

                call.enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(Call<Employee> call, Response<Employee> response) {

                        System.out.println(map);

                        if (response.code() == 200) {
                            MainActivity.loggedEmployee = response.body();
                            if (MainActivity.loggedEmployee == null) {
                                Toast.makeText(LoginActivity.this, "Error unknown.", Toast.LENGTH_LONG).show();
                            } else {
                                System.out.println(MainActivity.loggedEmployee);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } else if (response.code() == 400) {
                            Toast.makeText(LoginActivity.this, "Login credentials incorrect.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error unknown.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Employee> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}