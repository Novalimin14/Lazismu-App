package com.example.lazismuapp.Laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Model.LaporanAdapter;
import com.example.lazismuapp.Model.LaporanModel;
import com.example.lazismuapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;
public class Laporan extends AppCompatActivity {

    RecyclerView recyclerView;
    LaporanAdapter adapter;
    ApiService apiService;
    ArrayList<LaporanModel> list;
    FloatingActionButton tambahData;
    SearchView sv;
    FloatingActionButton tambah;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);


        recyclerView = findViewById(R.id.laporanllist);
        tambah = findViewById(R.id.addDataLaporan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new LaporanAdapter(this,list);

        // Inisialisasi Retrofit menggunakan RetrofitClient
        Retrofit retrofit = RetrofitClient.getClient(getString(R.string.BASE_URL));
        apiService = retrofit.create(ApiService.class);

        // Memanggil method untuk mengambil data
        getDataFromApi();

        // Inisialisasi SearchView
        sv = findViewById(R.id.searchviewlaporan);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText); // Panggil method filterData() saat isi SearchView berubah
                return true;
            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Laporan.this, tambahLaporan.class);
                startActivity(intent);
            }
        });
//        Refresh
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
    }

    // Method untuk mengambil data dari API menggunakan Retrofit
    private void getDataFromApi() {
        Call<List<LaporanModel>> call = apiService.getLaporan();
        call.enqueue(new Callback<List<LaporanModel>>() {
            @Override
            public void onResponse(Call<List<LaporanModel>> call, Response<List<LaporanModel>> response) {
                if (response.isSuccessful()) {
                    List<LaporanModel> laporanList = response.body();
                    list.addAll(laporanList);
                    adapter.filterList(laporanList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(Laporan.this, "Gagal memuat data. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LaporanModel>> call, Throwable t) {
                Toast.makeText(Laporan.this, "Gagal memuat data. Periksa koneksi internet Anda."+getString(R.string.BASE_URL), Toast.LENGTH_SHORT).show();
                Log.e("getDataFromApi", "Gagal mendapatkan data: " + t.getMessage());
            }
        });
    }

    // Method untuk melakukan filtering pada data laporan berdasarkan nama Muzaki
    private void filterData(String query) {
        ArrayList<LaporanModel> filteredList = new ArrayList<>();
        for (LaporanModel model : list) {
            if (model.getNamaMuz().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(model);
            }
        }
        adapter.filterList(filteredList); // Memperbarui data yang ditampilkan di RecyclerView
    }
}
