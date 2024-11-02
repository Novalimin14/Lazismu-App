package com.example.lazismuapp.Mustahik;

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
import com.example.lazismuapp.Laporan.Laporan;
import com.example.lazismuapp.Laporan.tambahLaporan;
import com.example.lazismuapp.Model.LaporanAdapter;
import com.example.lazismuapp.Model.LaporanModel;
import com.example.lazismuapp.Model.MustahikAdapter;
import com.example.lazismuapp.Model.MustahikModel;
import com.example.lazismuapp.R;
import com.example.lazismuapp.profile.ui.theme.tambahMuzzaki;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Mustahik extends AppCompatActivity {
    RecyclerView recyclerView;
    MustahikAdapter adapter;
    ApiService apiService;
    ArrayList<MustahikModel> list;
    FloatingActionButton tambahData;
    SearchView sv;
    FloatingActionButton tambah;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mustahik);

        recyclerView = findViewById(R.id.mustahiklist);
        tambah = findViewById(R.id.adddatamustahik);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new MustahikAdapter(this,list);

        // Inisialisasi Retrofit menggunakan RetrofitClient
        Retrofit retrofit = RetrofitClient.getClient(getString(R.string.BASE_URL));
        apiService = retrofit.create(ApiService.class);

        // Memanggil method untuk mengambil data
        getDataFromApi();

        // Inisialisasi SearchView
        sv = findViewById(R.id.searchviewmustahik);
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
                Intent intent = new Intent(Mustahik.this, tambahMustahik.class);
                startActivity(intent);
            }
        });
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
        Call<List<MustahikModel>> call = apiService.getMustahik();
        call.enqueue(new Callback<List<MustahikModel>>() {
            @Override
            public void onResponse(Call<List<MustahikModel>> call, Response<List<MustahikModel>> response) {
                if (response.isSuccessful()) {
                    List<MustahikModel> laporanList = response.body();
                    list.addAll(laporanList);
                    adapter.filterList(laporanList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                    Toast.makeText(Mustahik.this, "Berhasil memuat data. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Mustahik.this, "Gagal memuat data. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MustahikModel>> call, Throwable t) {
                Toast.makeText(Mustahik.this, "Gagal memuat data. Periksa koneksi internet Anda.", Toast.LENGTH_SHORT).show();
                Log.e("getDataFromApi", "Gagal mendapatkan data: " + t.getMessage());
            }
        });
    }

    // Method untuk melakukan filtering pada data laporan berdasarkan nama Muzaki
    private void filterData(String query) {
        ArrayList<MustahikModel> filteredList = new ArrayList<>();
        for (MustahikModel model : list) {
            if (model.getNama_mus().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(model);
            }
        }
        adapter.filterList(filteredList); // Memperbarui data yang ditampilkan di RecyclerView
    }
}
