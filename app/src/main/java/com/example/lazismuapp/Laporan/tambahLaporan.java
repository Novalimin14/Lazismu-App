package com.example.lazismuapp.Laporan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lazismuapp.Model.LaporanModel;
import com.example.lazismuapp.Model.MuzzakiModel;
import com.example.lazismuapp.R;
import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tambahLaporan extends AppCompatActivity {
    EditText muzzakiid, nama, alamat, kwitansi, jumlah, tanggal, kwitansi1, tanggalEditText,beras;
    Button simpan;
    ImageButton pickerButton;
    LaporanModel laporanModel;
    Spinner spinnerNamaMuz;
    List<MuzzakiModel> muzzakiList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String selectedMuzzakiId,selectedNamaMuz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laporan);

        muzzakiid = findViewById(R.id.muzzakiid);
        nama = findViewById(R.id.nama1);
        simpan = findViewById(R.id.simpan1);
        kwitansi1 = findViewById(R.id.kwitansitambah2);
        kwitansi = findViewById(R.id.no_kwitansi);
        jumlah = findViewById(R.id.jml_dana);
        beras = findViewById(R.id.jml_beras);
        tanggal = findViewById(R.id.tanggal);
        tanggalEditText = findViewById(R.id.tanggal);
        pickerButton = findViewById(R.id.picker_calender);
        spinnerNamaMuz = findViewById(R.id.spinner_nama_muz);



        pickerButton.setOnClickListener(v -> showDatePickerDialog());
        fetchNamaMuzzaki();
//        selectedNamaMuz = spinnerNamaMuz.getSelectedItem().toString();
//        nama.setText(selectedNamaMuz);

        simpan.setOnClickListener(v -> {
            if (laporanModel == null) {
                inputData();
            } else {
                updateData(laporanModel.getId());
            }
        });

        if (getIntent().hasExtra("laporan")) {
            laporanModel = (LaporanModel) getIntent().getSerializableExtra("laporan");
            setEditMode();
        }
    }

    private void setEditMode() {
        muzzakiid.setText(laporanModel.getMuzzakiId());
        nama.setText(laporanModel.getNamaMuz());
        kwitansi1.setText(laporanModel.getKwitansi());
        kwitansi.setText(laporanModel.getKeterangan());
        jumlah.setText(String.valueOf(laporanModel.getJumlahDana()));
        beras.setText(String.valueOf(laporanModel.getJumlahBeras()));
        tanggal.setText(laporanModel.getTanggal());
        simpan.setText("Update Data");
    }

    private void inputData() {
        if ( nama.getText().toString().isEmpty() ||
                kwitansi1.getText().toString().isEmpty() ||
                kwitansi.getText().toString().isEmpty() ||
                tanggal.getText().toString().isEmpty()) {

            Toast.makeText(tambahLaporan.this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        LaporanModel laporanModel = new LaporanModel();
        String jmldana = jumlah.getText().toString();
        if (jmldana != null && !jmldana.isEmpty()) {
            laporanModel.setJumlahDana(Integer.parseInt(jmldana));
            // Lanjutkan dengan pengolahan data
        }
        String jmlberas = beras.getText().toString();
        if (jmlberas != null && !jmlberas.isEmpty()) {
            laporanModel.setJumlahBeras(Float.parseFloat(jmlberas));
            // Lanjutkan dengan pengolahan data
        }
        String muzzid = muzzakiid.getText().toString();
        if (muzzid != null && !muzzid.isEmpty()) {
            laporanModel.setMuzzakiId(String.valueOf(Integer.parseInt(muzzakiid.getText().toString())));
            // Lanjutkan dengan pengolahan data
        }

        laporanModel.setNamaMuz(nama.getText().toString());
        laporanModel.setKwitansi(kwitansi1.getText().toString());
//        laporanModel.setJumlahDana(Integer.parseInt(jmldana));
//        laporanModel.setJumlahBeras(Float.parseFloat(jmlberas));
        laporanModel.setKeterangan(kwitansi.getText().toString());
        laporanModel.setTanggal(tanggal.getText().toString());

        Log.d("Data Laporan", "Muzzaki ID: " + laporanModel.getMuzzakiId());
        Log.d("Data Laporan", "Muzzaki ID: " + selectedNamaMuz);
        Log.d("Data Laporan", "Nama: " + laporanModel.getNamaMuz());
        Log.d("Data Laporan", "Jumlah Dana: " + laporanModel.getJumlahDana());
        Log.d("Data Laporan", "Keterangan: " + laporanModel.getKeterangan());
        Log.d("Data Laporan", "Tanggal: " + laporanModel.getTanggal());

        ApiService apiService = RetrofitClient.getClient(getString(R.string.BASE_URL)).create(ApiService.class);

        Call<Void> call = apiService.postLaporan(laporanModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(tambahLaporan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(tambahLaporan.this, errorMessage.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Toast.makeText(tambahLaporan.this, "Gagal mengirim data laporan", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(tambahLaporan.this, "Gagal mengirim data laporan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(tambahLaporan.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateData(int laporanId) {
        if ( nama.getText().toString().isEmpty() ||
                kwitansi1.getText().toString().isEmpty() ||
                kwitansi.getText().toString().isEmpty() ||
                tanggal.getText().toString().isEmpty()) {

            Toast.makeText(tambahLaporan.this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        LaporanModel laporanModel = new LaporanModel();
        String jmldana = jumlah.getText().toString();
        if (jmldana != null && !jmldana.isEmpty()) {
            laporanModel.setJumlahDana(Integer.parseInt(jmldana));
            // Lanjutkan dengan pengolahan data
        }
        String jmlberas = beras.getText().toString();
        if (jmlberas != null && !jmlberas.isEmpty()) {
            laporanModel.setJumlahBeras(Float.parseFloat(jmlberas));
            // Lanjutkan dengan pengolahan data
        }
//        LaporanModel laporanModel = new LaporanModel();
        String muzzid = muzzakiid.getText().toString();
        if (muzzid != null && !muzzid.isEmpty()) {
            laporanModel.setMuzzakiId(String.valueOf(Integer.parseInt(muzzakiid.getText().toString())));
            // Lanjutkan dengan pengolahan data
        }
        laporanModel.setNamaMuz(nama.getText().toString());
        laporanModel.setKwitansi(kwitansi1.getText().toString());
//        laporanModel.setJumlahDana(Integer.parseInt(jumlah.getText().toString()));
//        laporanModel.setJumlahBeras(Integer.parseInt(beras.getText().toString()));
        laporanModel.setKeterangan(kwitansi.getText().toString());
        laporanModel.setTanggal(tanggal.getText().toString());
        ApiService apiService = RetrofitClient.getClient(getString(R.string.BASE_URL)).create(ApiService.class);

        Call<Void> call = apiService.updateLaporan(laporanId, laporanModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(tambahLaporan.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(tambahLaporan.this, errorMessage.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Toast.makeText(tambahLaporan.this, "Gagal mengirim data laporan", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(tambahLaporan.this, "Gagal mengirim data laporan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(tambahLaporan.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> tanggalEditText.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }
    private void fetchNamaMuzzaki() {
        // Mendapatkan data dari server
        ApiService apiService = RetrofitClient.getClient(getString(R.string.BASE_URL)).create(ApiService.class);
        Call<List<MuzzakiModel>> call = apiService.getProfile();
        call.enqueue(new Callback<List<MuzzakiModel>>() {
            @Override
            public void onResponse(Call<List<MuzzakiModel>> call, Response<List<MuzzakiModel>> response) {
                if (response.isSuccessful()) {
                    // Data dari server berhasil diterima
                    List<MuzzakiModel> muzzakiList = response.body();
                    List<String> namaMuzzaki = new ArrayList<>();

                    // Menambahkan data default "Hamba Allah"
                    namaMuzzaki.add("Umum");

                    // Menambahkan data dari server ke dalam daftar nama muzzaki
                    for (MuzzakiModel muzzaki : muzzakiList) {
                        namaMuzzaki.add(muzzaki.getNama());
                    }

                    // Membuat adapter untuk spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(tambahLaporan.this, android.R.layout.simple_spinner_item, namaMuzzaki);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerNamaMuz.setAdapter(adapter);

                    // Menetapkan listener untuk spinner
                    spinnerNamaMuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Mendapatkan ID muzzaki yang dipilih
                            selectedNamaMuz = parent.getItemAtPosition(position).toString();
                            if (position == 0) {
                                // Jika posisi yang dipilih adalah "Hamba Allah", atur ID menjadi null
                                selectedMuzzakiId = null;
                            } else {
                                // Jika posisi yang dipilih adalah nama muzzaki dari server
                                selectedMuzzakiId = String.valueOf(muzzakiList.get(position - 1).getId());
                                nama.setText(selectedNamaMuz);
                                muzzakiid.setText(String.valueOf(Integer.parseInt(selectedMuzzakiId)));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Tidak melakukan apa-apa jika tidak ada yang dipilih
                            selectedMuzzakiId = null;
                        }
                    });
                } else {
                    // Gagal mendapatkan data dari server
                    Toast.makeText(tambahLaporan.this, "Gagal mendapatkan data muzzaki", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MuzzakiModel>> call, Throwable t) {
                // Terjadi kesalahan saat melakukan permintaan ke server
                Toast.makeText(tambahLaporan.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void clearFields() {
        muzzakiid.setText("");
        nama.setText("");
        kwitansi1.setText("");
        alamat.setText("");
        kwitansi.setText("");
        jumlah.setText("");
        beras.setText("");
        tanggal.setText("");
    }
}
