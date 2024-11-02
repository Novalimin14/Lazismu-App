package com.example.lazismuapp.Jadwal;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Laporan.Laporan;
import com.example.lazismuapp.Model.MuzzakiModel;
import com.example.lazismuapp.Model.PengambilanModel;
import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateJadwalActivity extends AppCompatActivity {

    private LinearLayout muzzakiContainer;
    private Button updateButton;
    private int jadwalId;
    private ApiService apiService;
    private Map<Integer, CheckBox> muzzakiCheckBoxMap = new HashMap<>();
    private List<PengambilanModel> pengambilanList;
    private List<MuzzakiModel> muzzakiList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_jadwal);

        muzzakiContainer = findViewById(R.id.muzzakiContainer);
        updateButton = findViewById(R.id.updateButton);
        Retrofit retrofit = RetrofitClient.getClient(getString(R.string.BASE_URL));
        apiService = retrofit.create(ApiService.class);

        jadwalId = getIntent().getIntExtra("jadwal_id", -1);
        loadMuzzakiData();

        updateButton.setOnClickListener(v -> updateChecklist());
    }

    private void loadMuzzakiData() {
        // Load all Muzzaki profiles
        Call<List<MuzzakiModel>> callMuzzaki = apiService.getProfile();
        callMuzzaki.enqueue(new Callback<List<MuzzakiModel>>() {
            @Override
            public void onResponse(Call<List<MuzzakiModel>> call, Response<List<MuzzakiModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    muzzakiList = response.body();
                    loadMuzzakiChecklist();
                }
            }

            @Override
            public void onFailure(Call<List<MuzzakiModel>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void loadMuzzakiChecklist() {
        // Load Muzzaki checklist data
        Call<List<PengambilanModel>> callChecklist = apiService.getMuzzaki(jadwalId);
        callChecklist.enqueue(new Callback<List<PengambilanModel>>() {
            @Override
            public void onResponse(Call<List<PengambilanModel>> call, Response<List<PengambilanModel>> response) {
                List<PengambilanModel> body = response.body();
                if (response.isSuccessful() && response.body() != null && !body.isEmpty()) {
                    pengambilanList = response.body();
                    displayMuzzaki();
//                    Toast.makeText(UpdateJadwalActivity.this, "Ada Data."+pengambilanList, Toast.LENGTH_SHORT).show();
                }else {
                    addNewJadwal();
                    Toast.makeText(UpdateJadwalActivity.this, "Proses mendapatkan Data.", Toast.LENGTH_SHORT).show();
                    loadMuzzakiData();
                }
            }

            @Override
            public void onFailure(Call<List<PengambilanModel>> call, Throwable t) {
                // Handle failure
            }
        });
    }
    private void addNewJadwal() {
        Call<PengambilanModel> callAddJadwal = apiService.addJadwal(jadwalId);
        callAddJadwal.enqueue(new Callback<PengambilanModel>() {
            @Override
            public void onResponse(Call<PengambilanModel> call, Response<PengambilanModel> response) {
                if (response.isSuccessful()) {
                    PengambilanModel newJadwal = response.body();
                     // perbarui jadwalId dengan ID jadwal baru
                    loadMuzzakiChecklist(); // panggil kembali loadMuzzakiChecklist setelah jadwal baru dibuat
//                    Toast.makeText(UpdateJadwalActivity.this, "load Data.", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(UpdateJadwalActivity.this, "error Data.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PengambilanModel> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void displayMuzzaki() {
        muzzakiContainer.removeAllViews();
        for (MuzzakiModel muzzaki : muzzakiList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(muzzaki.getNama());
            PengambilanModel pengambilan = findPengambilanByMuzzakiId(muzzaki.getId());
            if (pengambilan != null) {
                checkBox.setChecked(pengambilan.getIsChecked() == 1);
                checkBox.setTag(pengambilan);
            } else {
                PengambilanModel newPengambilan = new PengambilanModel();
                newPengambilan.setJadwalId(jadwalId);
                newPengambilan.setMuzzakiId(muzzaki.getId());
                checkBox.setTag(newPengambilan);
            }
            muzzakiCheckBoxMap.put(muzzaki.getId(), checkBox);
            muzzakiContainer.addView(checkBox);
        }
    }

    private PengambilanModel findPengambilanByMuzzakiId(int muzzakiId) {
        for (PengambilanModel pengambilan : pengambilanList) {
            if (pengambilan.getMuzzakiId() == muzzakiId) {
                return pengambilan;
            }
        }
        return null;
    }

    private void updateChecklist() {
        for (Map.Entry<Integer, CheckBox> entry : muzzakiCheckBoxMap.entrySet()) {
            CheckBox checkBox = entry.getValue();
            PengambilanModel pengambilan = (PengambilanModel) checkBox.getTag();

//            pengambilan.setIsChecked(checkBox.isChecked() ? 1 : 0);
            int isChecked = checkBox.isChecked() ? 1 : 0;
            Log.e("getDataFromJAadwal", "Data pengambilan : "+pengambilan.getIsChecked() +"checkbox : "+isChecked+" id:  "+pengambilan.getMuzzakiId());
            if (pengambilan.getIsChecked() == isChecked) {
                continue;
            } else if (pengambilan.getIsChecked() != isChecked) {
                // Case 2: Update required
                pengambilan.setIsChecked(isChecked);
                Call<PengambilanModel> call = apiService.updateMustahikChecklist(pengambilan.getId());
                call.enqueue(new Callback<PengambilanModel>() {
                    @Override
                    public void onResponse(Call<PengambilanModel> call, Response<PengambilanModel> response) {
                        if (response.isSuccessful()) {
                            // Handle successful update
                        } else {
                            // Handle update failure
                            tambahDataBaru(pengambilan, isChecked);
                        }
                    }

                    @Override
                    public void onFailure(Call<PengambilanModel> call, Throwable t) {
                        // Handle failure
                    }
                });
            } else {
                // Case 3: New entry (pengambilan.getIsChecked() == null)

            }
        }
    }
    private void tambahDataBaru(PengambilanModel pengambilan, int isChecked){
        pengambilan.setIsChecked(isChecked);
        Call<PengambilanModel> call = apiService.postMuzzakiJadwal(jadwalId,pengambilan.getMuzzakiId());
        call.enqueue(new Callback<PengambilanModel>() {
            @Override
            public void onResponse(Call<PengambilanModel> call, Response<PengambilanModel> response) {
                if (response.isSuccessful()) {
                    // Handle successful post
                } else {
                    // Handle post failure
                }
            }

            @Override
            public void onFailure(Call<PengambilanModel> call, Throwable t) {
                // Handle failure
            }
        });

    }
}
