package com.example.lazismuapp.Jadwal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.lazismuapp.Api.ApiClient;
import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Laporan.Laporan;
import com.example.lazismuapp.Laporan.tambahLaporan;
import com.example.lazismuapp.Model.JadwalAdapter;
import com.example.lazismuapp.Model.JadwalModel;
import com.example.lazismuapp.Model.LaporanModel;
import com.example.lazismuapp.Model.PengambilanModel;
import com.example.lazismuapp.Model.PengambilanAdapter;
import com.example.lazismuapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Jadwal extends AppCompatActivity{

    private RecyclerView recyclerView;
    private JadwalAdapter jadwalAdapter;
    ApiService apiService;
    ArrayList<JadwalModel> list;
    FloatingActionButton tambah;
    private BroadcastReceiver mReceiver;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        recyclerView = findViewById(R.id.recyclerViewJadwal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tambah = findViewById(R.id.addDataJadwal);
        list = new ArrayList<>();
        jadwalAdapter = new JadwalAdapter(Jadwal.this,list);


        getJadwalData();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Jadwal.this, TambahJadwalActivity.class);
                startActivity(intent);


            }

        });
//        refresh
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Aksi refresh akan dilakukan di sini (misalnya mengambil data baru dari server)

                // Contoh: Delay sederhana untuk simulasi pengambilan data
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hentikan indikator refresh setelah selesai
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000); // Waktu delay dalam milidetik (di sini 2000ms atau 2 detik)
            }
        });
        // Register the broadcast receiver
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Refresh the data when the broadcast is received
                getJadwalData();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("com.example.lazismuapp.JADWAL_ADDED"));

    }
    @Override
    protected void onDestroy() {
        // Unregister the broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    private void getJadwalData() {
        Retrofit retrofit = RetrofitClient.getClient(getString(R.string.BASE_URL));
        apiService = retrofit.create(ApiService.class);

        Call<List<JadwalModel>> call = apiService.getJadwals();
        call.enqueue(new Callback<List<JadwalModel>>() {
            @Override
            public void onResponse(Call<List<JadwalModel>> call, Response<List<JadwalModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<JadwalModel> jadwalList = response.body();
                    list.clear();
                    list.addAll(jadwalList);
                    recyclerView.setAdapter(jadwalAdapter);
                    jadwalAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(Jadwal.this, "Gagal memuat data. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<JadwalModel>> call, Throwable t) {
                // Handle failure
                Toast.makeText(Jadwal.this, "Gagal memuat data. Periksa koneksi internet Anda."+getString(R.string.BASE_URL), Toast.LENGTH_SHORT).show();
                Log.e("getDataFromApi", "Gagal mendapatkan data: " + t.getMessage());
            }
        });
    }
}
