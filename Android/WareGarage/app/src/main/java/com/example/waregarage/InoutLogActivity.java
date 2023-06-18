package com.example.waregarage;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waregarage.model.InoutRecords;
import com.example.waregarage.model.Warehouse;
import com.example.waregarage.request.RetrofitInterface;
import com.example.waregarage.request.UtilsApi;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InoutLogActivity extends AppCompatActivity {

    RetrofitInterface retroInt;
    Context mContext;
    protected static Class previousAct;
    protected static List<InoutRecords> inoutRecords;

    String in_time_start = null, in_time_end = null, out_time_start = null, out_time_end = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inout_log);

        retroInt = UtilsApi.getApiService();
        mContext = this;

        Button buttonSetDateFrom = findViewById(R.id.buttonSetDateFrom);
        Button buttonSetDateTo = findViewById(R.id.buttonSetDateTo);

        TextView valueDateFrom = findViewById(R.id.valueDateFrom);
        TextView valueDateTo = findViewById(R.id.valueDateTo);

        buttonSetDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year1 = c.get(Calendar.YEAR);
                int month1 = c.get(Calendar.MONTH);
                int day1 = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog1 = new DatePickerDialog(
                        InoutLogActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year1, int month1, int day1) {
                                in_time_start = year1 + "-" + month1 + "-" + day1;
                            }
                        }, year1, month1, day1);
                datePickerDialog1.setMessage("From");
                datePickerDialog1.show();

                int year2 = c.get(Calendar.YEAR);
                int month2 = c.get(Calendar.MONTH);
                int day2 = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog2 = new DatePickerDialog(
                        InoutLogActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year2, int month2, int day2) {
                                in_time_end = year2 + "-" + month2 + "-" + day2;
                            }
                        }, year2, month2, day2);
                datePickerDialog2.setMessage("To");
                datePickerDialog2.show();

                String fullIn = in_time_start + " - " + in_time_end;
                valueDateFrom.setText(fullIn);
            }
        });

        buttonSetDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year1 = c.get(Calendar.YEAR);
                int month1 = c.get(Calendar.MONTH);
                int day1 = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog1 = new DatePickerDialog(
                        InoutLogActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year1, int month1, int day1) {
                                out_time_start = year1 + "-" + month1 + "-" + day1;
                            }
                        }, year1, month1, day1);
                datePickerDialog1.setMessage("From");
                datePickerDialog1.show();

                int year2 = c.get(Calendar.YEAR);
                int month2 = c.get(Calendar.MONTH);
                int day2 = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog2 = new DatePickerDialog(
                        InoutLogActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year2, int month2, int day2) {
                                out_time_end = year2 + "-" + month2 + "-" + day2;
                            }
                        }, year2, month2, day2);
                datePickerDialog2.setMessage("To");
                datePickerDialog2.show();

                String fullOut = out_time_start + " - " + out_time_end;
                valueDateTo.setText(fullOut);
            }
        });

        Button confirm = findViewById(R.id.buttonConfirm);
        Button print = findViewById(R.id.buttonPrint);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("company_id", valueOf(MainActivity.loggedEmployee.company_id));
                map.put("in_time_start", in_time_start);
                map.put("in_time_end", in_time_end);
                map.put("out_time_start", out_time_start);
                map.put("out_time_end", out_time_end);
                Call<List<InoutRecords>> call1 = retroInt.inOut(map);
                call1.enqueue(new Callback<List<InoutRecords>>() {
                    @Override
                    public void onResponse(Call<List<InoutRecords>> call1, Response<List<InoutRecords>> response) {

                        if (response.code() == 200) {
                            InoutLogActivity.inoutRecords = response.body();
                            print.setVisibility(View.VISIBLE);
                        } else if (response.code() == 400) {
                            Toast.makeText(InoutLogActivity.this, "Internal Error.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<InoutRecords>> call1, Throwable t) {
                        Toast.makeText(InoutLogActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    String filename = "Output" + System.currentTimeMillis() + ".pdf";
                    File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
                    FileOutputStream outputStream = new FileOutputStream(pdfFile);
                    Document document = new Document();
                    PdfWriter.getInstance(document, outputStream);
                    document.open();

                    document.add(new Paragraph("File is created with the following format: id name in_date out_date"));

                    Iterable<InoutRecords> iterable = inoutRecords;

                    for (InoutRecords inoutRecord : iterable) {
                        document.add(new Paragraph(inoutRecord.commodity_id + " " + inoutRecord.name + " " + inoutRecord.in_time + " " + inoutRecord.out_time));
                    }

                    document.close();

                } catch(Exception e) {
                    e.printStackTrace();
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
                intent = new Intent(this, previousAct);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}