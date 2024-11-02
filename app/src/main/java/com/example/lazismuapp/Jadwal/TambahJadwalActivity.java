package com.example.lazismuapp.Jadwal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Laporan.tambahLaporan;
import com.example.lazismuapp.Model.JadwalRequest;
import com.example.lazismuapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahJadwalActivity extends Jadwal {
    EditText tanggalEditText;
    ImageButton pickerButton;
    Button simpan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_jadwal);
        tanggalEditText = findViewById(R.id.bulan);
        pickerButton = findViewById(R.id.picker_calender_jadwal);
        simpan = findViewById(R.id.simpan_jadwal);

        pickerButton.setOnClickListener(v -> showDatePickerDialog());
        simpan.setOnClickListener(v -> {
            inputData();
        });
    }

    private void inputData() {
        ApiService apiService = RetrofitClient.getClient(getString(R.string.BASE_URL)).create(ApiService.class);
        JadwalRequest jadwalRequest = new JadwalRequest(tanggalEditText.getText().toString());
        Call<Void> call = apiService.posttambahJadwal(jadwalRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TambahJadwalActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("com.example.lazismuapp.JADWAL_ADDED");
                    LocalBroadcastManager.getInstance(TambahJadwalActivity.this).sendBroadcast(intent);
                    finish();
                } else {
                    if (response.code() == 422) {
                        try {
                            JSONObject errorResponse = new JSONObject(response.errorBody().string());
                            StringBuilder errorMessage = new StringBuilder();
                            Iterator<String> keys = errorResponse.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                JSONArray errorArray = errorResponse.getJSONArray(key);
                                for (int i = 0; i < errorArray.length(); i++) {
                                    errorMessage.append(errorArray.getString(i)).append("\n");
                                }
                            }
                            Toast.makeText(TambahJadwalActivity.this, errorMessage.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Toast.makeText(TambahJadwalActivity.this, "Gagal mengirim data laporan", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TambahJadwalActivity.this, "Gagal mengirim data laporan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TambahJadwalActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> tanggalEditText.setText(String.format("%d-%02d", year, month + 1)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                0
        );

        datePickerDialog.show();
    }

}