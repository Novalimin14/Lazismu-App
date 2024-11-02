package com.example.lazismuapp.profile.ui.theme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Model.MuzzakiAdapter;
import com.example.lazismuapp.Model.MuzzakiModel;
import com.example.lazismuapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Muzzaki extends AppCompatActivity {
    RecyclerView recyclerView;

    MuzzakiAdapter adapter;
    ApiService apiService;
    SearchView sv;
    ArrayList<MuzzakiModel> list,coba;
    FloatingActionButton tambah;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muzzaki);

        recyclerView = findViewById(R.id.tableTest);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tambah = findViewById(R.id.addData);
        list = new ArrayList<>();
        coba = new ArrayList<>();
        Set<MuzzakiModel> dataList = new HashSet<>();
        adapter = new MuzzakiAdapter(this,list);
//        adapter = new profileAdapter(dataList);
        Retrofit retrofit = RetrofitClient.getClient(getString(R.string.BASE_URL));
        apiService = retrofit.create(ApiService.class);

        // Memanggil method untuk mengambil data
        getDataFromApi();
        sv = findViewById(R.id.searchviewprofile);
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
                Intent intent = new Intent(Muzzaki.this, tambahMuzzaki.class);
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



    }
    private void getDataFromApi() {
        Call<List<MuzzakiModel>> call = apiService.getProfile();
        call.enqueue(new Callback<List<MuzzakiModel>>() {
            @Override
            public void onResponse(Call<List<MuzzakiModel>> call, Response<List<MuzzakiModel>> response) {
                if (response.isSuccessful()) {
                    List<MuzzakiModel> profile = response.body();
                    list.addAll(profile);
                    adapter.filterList(profile);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                    Toast.makeText(Muzzaki.this, "Berhasil memuat data. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Muzzaki.this, "Gagal memuat data. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MuzzakiModel>> call, Throwable t) {
                Toast.makeText(Muzzaki.this, "Gagal memuat data. Periksa koneksi internet Anda.", Toast.LENGTH_SHORT).show();
                Log.e("getDataFromApi", "Gagal mendapatkan data: " + t.getMessage());
            }
        });
    }

    // Method untuk melakukan filtering pada data laporan berdasarkan nama Muzaki
    private void filterData(String query) {
        ArrayList<MuzzakiModel> filteredList = new ArrayList<>();
        for (MuzzakiModel model : list) {
            if (model.getNama().toLowerCase().contains(query.toLowerCase()) ||  model.getAlamat().toLowerCase().contains(query.toLowerCase()) ) {
                filteredList.add(model);
            }
        }
        adapter.filterList(filteredList); // Memperbarui data yang ditampilkan di RecyclerView
    }
}