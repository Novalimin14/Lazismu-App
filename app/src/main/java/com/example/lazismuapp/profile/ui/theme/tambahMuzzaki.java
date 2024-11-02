package com.example.lazismuapp.profile.ui.theme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Model.MustahikModel;
import com.example.lazismuapp.Model.MuzzakiModel;
import com.example.lazismuapp.Mustahik.Mustahik;
import com.example.lazismuapp.Mustahik.tambahMustahik;
import com.example.lazismuapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tambahMuzzaki extends AppCompatActivity {
    EditText nama, alamat, ktp, pekerjaan, linkmaps;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale;
    Button simpan;
    MuzzakiModel muzzakiModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_muzzaki);

        nama = findViewById(R.id.nama_muzzaki);
        alamat = findViewById(R.id.alamat_muzzaki);
        ktp = findViewById(R.id.ktp_muzzaki);
        pekerjaan = findViewById(R.id.pekerjaan_muzzaki);
        linkmaps = findViewById(R.id.linkmaps_muzzaki);
        radioGroup = findViewById(R.id.radioGroup_muzzaki);
        simpan = findViewById(R.id.simpan_muzzaki);
        radioMale = findViewById(R.id.radio_male1);
        radioFemale = findViewById(R.id.radio_female1);

        simpan.setOnClickListener(v -> {
            if (muzzakiModel == null) {
                inputData();
            } else {
                updateData(muzzakiModel.getId());

            }
        });

        if (getIntent().hasExtra("muzzaki")) {
            muzzakiModel = (MuzzakiModel) getIntent().getSerializableExtra("muzzaki");
            setEditMode();
        }
    }

    private void setEditMode() {
        try {
            nama.setText(muzzakiModel.getNama());
            alamat.setText(muzzakiModel.getAlamat());
            ktp.setText(muzzakiModel.getKtp());
            pekerjaan.setText(muzzakiModel.getPekerjaan());
            linkmaps.setText(muzzakiModel.getLinkmaps());
            simpan.setText("Update Data");
            Log.d("Data Muzzaki", "Nama: " );

            // Mengatur nilai RadioButton berdasarkan jenis kelamin
            String jenisKelamin = muzzakiModel.getJkl();
            if (jenisKelamin != null) {
                if (jenisKelamin.equals("Laki-laki")) {
                    radioGroup.check(R.id.radio_male1);
                } else if (jenisKelamin.equals("Perempuan")) {
                    radioGroup.check(R.id.radio_female1);
                }
            } else {
                Log.e("setEditMode", "Jenis kelamin null");
            }
        } catch (Exception e) {
            Log.e("setEditMode", "Error setting edit mode: " + e.getMessage(), e);
        }
    }
    private void inputData() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String jenisKelamin = "";

        if (selectedId == R.id.radio_male1) {
            jenisKelamin = "Laki-laki";
        } else if (selectedId == R.id.radio_female1) {
            jenisKelamin = "Perempuan";
        }
        if (nama.getText().toString().isEmpty() ||
                alamat.getText().toString().isEmpty() ||
                jenisKelamin == null || jenisKelamin.isEmpty() || // Asumsikan jenisKelamin dipilih dari dropdown atau radio button
                ktp.getText().toString().isEmpty() ||
                pekerjaan.getText().toString().isEmpty() ||
                linkmaps.getText().toString().isEmpty()) {

            Toast.makeText(tambahMuzzaki.this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        MuzzakiModel muzzakiModel = new MuzzakiModel();
        muzzakiModel.setNama(nama.getText().toString());
        muzzakiModel.setAlamat(alamat.getText().toString());
        muzzakiModel.setJkl(jenisKelamin);
        muzzakiModel.setKtp(ktp.getText().toString());
        muzzakiModel.setPekerjaan(pekerjaan.getText().toString());
        muzzakiModel.setLinkmaps(linkmaps.getText().toString());
        Log.d("Data Muzzaki", "Nama: " + muzzakiModel.getNama());
        Log.d("Data Muzzaki", "Alamat: " + muzzakiModel.getAlamat());
        Log.d("Data Muzzaki", "JKL: " + muzzakiModel.getJkl());
        Log.d("Data Muzzaki", "KTP: " + muzzakiModel.getKtp());
        Log.d("Data Muzzaki", "Peke: " + muzzakiModel.getPekerjaan());

        Log.d("Data Muzzaki", "Link: " + muzzakiModel.getLinkmaps());


        ApiService apiService = RetrofitClient.getClient(getString(R.string.BASE_URL)).create(ApiService.class);

        Call<Void> call = apiService.postMuzzaki(muzzakiModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(tambahMuzzaki.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    clearFields();
                    Intent intent = new Intent(tambahMuzzaki.this, Muzzaki.class);
                    startActivity(intent);
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
                            Toast.makeText(tambahMuzzaki.this, errorMessage.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Toast.makeText(tambahMuzzaki.this, "Gagal mengirim data muzzaki", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(tambahMuzzaki.this, "Gagal mengirim data muzzaki", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(tambahMuzzaki.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateData(int muzzakiId) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String jenisKelamin = "";

        if (selectedId == R.id.radio_male1) {
            jenisKelamin = "Laki-laki";
        } else if (selectedId == R.id.radio_female1) {
            jenisKelamin = "Perempuan";
        }
        if (nama.getText().toString().isEmpty() ||
                alamat.getText().toString().isEmpty() ||
                jenisKelamin == null || jenisKelamin.isEmpty() || // Asumsikan jenisKelamin dipilih dari dropdown atau radio button
                ktp.getText().toString().isEmpty() ||
                pekerjaan.getText().toString().isEmpty() ||
                linkmaps.getText().toString().isEmpty()) {

            Toast.makeText(tambahMuzzaki.this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        MuzzakiModel muzzakiModel = new MuzzakiModel();
        muzzakiModel.setNama(nama.getText().toString());
        muzzakiModel.setAlamat(alamat.getText().toString());
        muzzakiModel.setJkl(jenisKelamin);
        muzzakiModel.setKtp(ktp.getText().toString());
        muzzakiModel.setPekerjaan(pekerjaan.getText().toString());
        muzzakiModel.setLinkmaps(linkmaps.getText().toString());


        ApiService apiService = RetrofitClient.getClient(getString(R.string.BASE_URL)).create(ApiService.class);

        Call<Void> call = apiService.updateMuzzaki(muzzakiId, muzzakiModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(tambahMuzzaki.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    clearFields();
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
                            Toast.makeText(tambahMuzzaki.this, errorMessage.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Toast.makeText(tambahMuzzaki.this, "Gagal mengirimkan data mustahik", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(tambahMuzzaki.this, "Gagal mengirim data mustahik", Toast.LENGTH_SHORT).show();
                        try {
                            Log.e("API_ERROR", response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(tambahMuzzaki.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clearFields() {
        nama.setText("");
        alamat.setText("");
        ktp.setText("");
        pekerjaan.setText("");
        linkmaps.setText("");
        radioGroup.clearCheck();
    }
}