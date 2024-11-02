package com.example.lazismuapp.Mustahik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Laporan.tambahLaporan;
import com.example.lazismuapp.Model.LaporanModel;
import com.example.lazismuapp.Model.MustahikModel;
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

public class tambahMustahik extends AppCompatActivity {
    EditText nama, alamat, ktp, pekerjaan, gaji, status, keterangan, tanggal, linkmaps;
    Spinner jenis_mus, tipe_mus, KTM, spres, skel, sktm, sprem;
    Button simpan;
    ImageButton pickerButton;
    MustahikModel mustahikModel;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale;

    ArrayAdapter<CharSequence> jenisMusAdapter, tipeMusAdapter, adaTidakAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_mustahik);

        nama = findViewById(R.id.namamus1);
        alamat = findViewById(R.id.alamat);
        ktp = findViewById(R.id.ktp);
        pekerjaan = findViewById(R.id.pekerjaan);

        jenis_mus = findViewById(R.id.jenismustahik);
        tipe_mus = findViewById(R.id.tipe_mus);
        KTM = findViewById(R.id.ktm);
        spres = findViewById(R.id.spres);
        skel = findViewById(R.id.skel);
        sktm = findViewById(R.id.sktm);
        sprem = findViewById(R.id.sprem);
        gaji = findViewById(R.id.gaji);
        status = findViewById(R.id.status);
        keterangan = findViewById(R.id.keterangan);
        tanggal = findViewById(R.id.tanggal);
        linkmaps = findViewById(R.id.linkmaps);
        pickerButton = findViewById(R.id.picker_calender);
        simpan = findViewById(R.id.simpan1);
        radioGroup = findViewById(R.id.radioGroup);

        radioFemale = findViewById(R.id.radio_female);
        radioMale = findViewById(R.id.radio_male);

        jenisMusAdapter = ArrayAdapter.createFromResource(this, R.array.jenis_mustahik_array, android.R.layout.simple_spinner_item);
        jenisMusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenis_mus.setAdapter(jenisMusAdapter);

        tipeMusAdapter = ArrayAdapter.createFromResource(this, R.array.tipe_mus_array, android.R.layout.simple_spinner_item);
        tipeMusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipe_mus.setAdapter(tipeMusAdapter);

        adaTidakAdapter = ArrayAdapter.createFromResource(this, R.array.ada_tidak_array, android.R.layout.simple_spinner_item);
        adaTidakAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        KTM.setAdapter(adaTidakAdapter);
        spres.setAdapter(adaTidakAdapter);
        skel.setAdapter(adaTidakAdapter);
        sktm.setAdapter(adaTidakAdapter);
        sprem.setAdapter(adaTidakAdapter);

        pickerButton.setOnClickListener(v -> showDatePickerDialog());

        simpan.setOnClickListener(v -> {
            if (mustahikModel == null) {
                inputData();
            } else {
                updateData(mustahikModel.getId());
            }
        });

        if (getIntent().hasExtra("mustahik")) {
            mustahikModel = (MustahikModel) getIntent().getSerializableExtra("mustahik");
            setEditMode();
        }
    }

    private void setEditMode() {
        try {
            if (mustahikModel != null) {
                setSpinnerSelection(jenis_mus, jenisMusAdapter, mustahikModel.getJns_mus());
                setSpinnerSelection(tipe_mus, tipeMusAdapter, mustahikModel.getTipe_mus());
                setSpinnerSelection(KTM, adaTidakAdapter, mustahikModel.getKtm());
                setSpinnerSelection(spres, adaTidakAdapter, mustahikModel.getSpres());
                setSpinnerSelection(skel, adaTidakAdapter, mustahikModel.getSkel());
                setSpinnerSelection(sktm, adaTidakAdapter, mustahikModel.getSktm());
                setSpinnerSelection(sprem, adaTidakAdapter, mustahikModel.getSprem());
            }
            gaji.setText(mustahikModel.getGaji());
            nama.setText(mustahikModel.getNama_mus());
            alamat.setText(mustahikModel.getAlamat());
            ktp.setText(mustahikModel.getKtp());
            status.setText(mustahikModel.getStatus());
            pekerjaan.setText(mustahikModel.getPekerjaan());
            tanggal.setText(mustahikModel.getTanggal());
            keterangan.setText(mustahikModel.getKeterangan());
            linkmaps.setText(mustahikModel.getLink_maps());
            simpan.setText("Update Data");

            // Mengatur nilai RadioButton berdasarkan jenis kelamin
            String jenisKelamin = mustahikModel.getJkl();
            if (jenisKelamin != null) {
                if (jenisKelamin.equals("Laki-laki")) {
                    radioGroup.check(R.id.radio_male);
                } else if (jenisKelamin.equals("Perempuan")) {
                    radioGroup.check(R.id.radio_female);
                }
            } else {
                Log.e("setEditMode", "Jenis kelamin null");
            }
        } catch (Exception e) {
            Log.e("setEditMode", "Error setting edit mode: " + e.getMessage(), e);
        }
    }

    private void setSpinnerSelection(Spinner spinner, ArrayAdapter<CharSequence> adapter, String value) {
        if (value != null) {
            int position = adapter.getPosition(value);
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }
    }

    private void inputData() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String jenisKelamin = "";

        if (selectedId == R.id.radio_male) {
            jenisKelamin = "Laki-laki";
        } else if (selectedId == R.id.radio_female) {
            jenisKelamin = "Perempuan";
        }

        if (nama.getText().toString().isEmpty() ||
                alamat.getText().toString().isEmpty() ||
                ktp.getText().toString().isEmpty() ||
                jenisKelamin.isEmpty() ||
                status.getText().toString().isEmpty() ||
                jenis_mus.getSelectedItem().toString().isEmpty() ||
                skel.getSelectedItem().toString().isEmpty() ||
                sprem.getSelectedItem().toString().isEmpty() ||
                KTM.getSelectedItem().toString().isEmpty() ||
                pekerjaan.getText().toString().isEmpty() ||
                tipe_mus.getSelectedItem().toString().isEmpty() ||
                spres.getSelectedItem().toString().isEmpty() ||
                sktm.getSelectedItem().toString().isEmpty() ||
                gaji.getText().toString().isEmpty() ||
                keterangan.getText().toString().isEmpty() ||
                linkmaps.getText().toString().isEmpty() ||
                tanggal.getText().toString().isEmpty()) {

            Toast.makeText(tambahMustahik.this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        MustahikModel mustahikModel = new MustahikModel();
        mustahikModel.setNama_mus(nama.getText().toString());
        mustahikModel.setAlamat(alamat.getText().toString());
        mustahikModel.setKtp(ktp.getText().toString());
        mustahikModel.setJkl(jenisKelamin);
        mustahikModel.setStatus(status.getText().toString());
        mustahikModel.setJns_mus(jenis_mus.getSelectedItem().toString());
        mustahikModel.setSkel(skel.getSelectedItem().toString());
        mustahikModel.setSprem(sprem.getSelectedItem().toString());
        mustahikModel.setKtm(KTM.getSelectedItem().toString());
        mustahikModel.setPekerjaan(pekerjaan.getText().toString());
        mustahikModel.setTipe_mus(tipe_mus.getSelectedItem().toString());
        mustahikModel.setSpres(spres.getSelectedItem().toString());
        mustahikModel.setSktm(sktm.getSelectedItem().toString());
        mustahikModel.setGaji(gaji.getText().toString());
        mustahikModel.setKeterangan(keterangan.getText().toString());
        mustahikModel.setLink_maps(linkmaps.getText().toString());
        mustahikModel.setTanggal(tanggal.getText().toString());
        ApiService apiService = RetrofitClient.getClient(getString(R.string.BASE_URL)).create(ApiService.class);

        Call<Void> call = apiService.postMustahik(mustahikModel);
//        Call<MustahikModel> call = RetrofitClient.getInstance().getMyApi().addMustahik(mustahikModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(tambahMustahik.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            if (errorJson.has("message")) {
                                String errorMessage = errorJson.getString("message");
                                Toast.makeText(tambahMustahik.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(tambahMustahik.this, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(tambahMustahik.this, "Gagal menghubungi server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateData(int id) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String jenisKelamin = "";

        if (selectedId == R.id.radio_male) {
            jenisKelamin = "Laki-laki";
        } else if (selectedId == R.id.radio_female) {
            jenisKelamin = "Perempuan";
        }

        MustahikModel mustahikModel = new MustahikModel();
        mustahikModel.setNama_mus(nama.getText().toString());
        mustahikModel.setAlamat(alamat.getText().toString());
        mustahikModel.setKtp(ktp.getText().toString());
        mustahikModel.setJkl(jenisKelamin);
        mustahikModel.setStatus(status.getText().toString());
        mustahikModel.setJns_mus(jenis_mus.getSelectedItem().toString());
        mustahikModel.setSkel(skel.getSelectedItem().toString());
        mustahikModel.setSprem(sprem.getSelectedItem().toString());
        mustahikModel.setKtm(KTM.getSelectedItem().toString());
        mustahikModel.setPekerjaan(pekerjaan.getText().toString());
        mustahikModel.setTipe_mus(tipe_mus.getSelectedItem().toString());
        mustahikModel.setSpres(spres.getSelectedItem().toString());
        mustahikModel.setSktm(sktm.getSelectedItem().toString());
        mustahikModel.setGaji(gaji.getText().toString());
        mustahikModel.setKeterangan(keterangan.getText().toString());
        mustahikModel.setLink_maps(linkmaps.getText().toString());
        mustahikModel.setTanggal(tanggal.getText().toString());

        ApiService apiService = RetrofitClient.getClient(getString(R.string.BASE_URL)).create(ApiService.class);
        Call<Void> call = apiService.postMustahik(mustahikModel);
//        Call<MustahikModel> call = RetrofitClient.getInstance().getMyApi().updateMustahik(id, mustahikModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(tambahMustahik.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            JSONObject errorJson = new JSONObject(errorBody);
                            if (errorJson.has("message")) {
                                String errorMessage = errorJson.getString("message");
                                Toast.makeText(tambahMustahik.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(tambahMustahik.this, "Gagal mengupdate data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(tambahMustahik.this, "Gagal menghubungi server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    tanggal.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}