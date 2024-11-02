package com.example.lazismuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lazismuapp.Jadwal.Jadwal;
import com.example.lazismuapp.R;

import androidx.appcompat.widget.Toolbar;


import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Login.ui.LoginActivity;
import com.example.lazismuapp.Model.InfoTerkiniAdapter;
import com.example.lazismuapp.Model.Infoterkini;
import com.example.lazismuapp.calculator.Calculator;

import com.example.lazismuapp.profile.ui.theme.Muzzaki;
import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textViewName,textViewusername,textViewemail;
    CardView Jadwal,Laporan,Kalkulator,Profil,Mustahik;
    ImageView imageView;
    private Toolbar toolbar;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnLogout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMenu();
        // Mengambil nama dari SharedPreferences
        textViewName = findViewById(R.id.textViewName);
        textViewusername = findViewById(R.id.textView_username);
        textViewemail = findViewById(R.id.textView_email);
        SharedPreferences preferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
        String userName = preferences.getString("USER_NAME", "paijo");
        textViewName.setText(userName);
//        textViewusername.setText(userName);
//        textViewemail.setText(userName);


        // Mengatur nama pada TextView
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        imageView = findViewById(R.id.nav_logout);

        // Setup ActionBarDrawerToggle untuk mengaktifkan hamburger icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //set warna
        DrawerArrowDrawable drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setColor(Color.GRAY); // Ganti dengan warna yang diinginkan
        actionBarDrawerToggle.setDrawerArrowDrawable(drawerArrowDrawable);

        // Tambahkan listener untuk menangani klik pada item menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item menu click disini
                if (item.getItemId() == R.id.nav_logout) {
                    logout();
                    return true;
                }
                return false;
            }
        });


        recyclerView = findViewById(R.id.recyclerviewdata);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize Retrofit
        Retrofit retrofit = RetrofitClient.getClient(getString(R.string.BASE_URL));

        ApiService apiService = retrofit.create(ApiService.class);

        // Fetch data from API
        apiService.getInfoTerkini().enqueue(new Callback<Infoterkini>() {
            @Override
            public void onResponse(Call<Infoterkini> call, Response<Infoterkini> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Infoterkini infoTerkini = response.body();
                    // Update RecyclerView with the fetched data
                    List<Infoterkini> infoList = Collections.singletonList(infoTerkini);
                    InfoTerkiniAdapter mAdapter = new InfoTerkiniAdapter(infoList);
                    recyclerView.setAdapter(mAdapter);
                    Log.d("MainActivity", "Pemasukan: " + infoTerkini.getPemasukan());
                    Log.d("MainActivity", "Pengeluaran: " + infoTerkini.getPengeluaran());
                    Log.d("MainActivity", "Total: " + infoTerkini.getTotal());
                    Log.d("MainActivity", "Jumlah Muzzaki: " + infoTerkini.getJumlahMuzzaki());
                    Log.d("MainActivity", "Jumlah Mustahik: " + infoTerkini.getJumlahMustahik());
                }
            }

            @Override
            public void onFailure(Call<Infoterkini> call, Throwable t) {
                // Handle error
                Log.e("MainActivity", "Error fetching data", t);
            }
        });
    }
    private void logout() {
        SharedPreferences preferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("TOKEN");
        editor.apply();

        // Kembali ke LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setMenu() {
        Jadwal = findViewById(R.id.jadwal);
        Laporan = findViewById(R.id.laporan);
        Kalkulator = findViewById(R.id.calculator);
        Profil = findViewById(R.id.profile);
        Mustahik = findViewById(R.id.mustahik);
        Kalkulator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Calculator.class);
                startActivity(intent);
            }
        });
        Profil.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Muzzaki.class);
                startActivity(intent);
            }
        });
        Laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.lazismuapp.Laporan.Laporan.class);
                startActivity(intent);
            }
        });
        Mustahik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.lazismuapp.Mustahik.Mustahik.class);
                startActivity(intent);
            }
        });
        Jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.lazismuapp.Jadwal.Jadwal.class);
                startActivity(intent);
            }
        });


    }
}