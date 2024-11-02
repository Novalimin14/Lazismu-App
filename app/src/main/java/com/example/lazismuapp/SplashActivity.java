package com.example.lazismuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lazismuapp.Login.ui.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Periksa status login
        SharedPreferences preferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
        String token = preferences.getString("TOKEN", null);

        if (token != null) {
            // Jika sudah login, lanjutkan ke MainActivity
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // Jika belum login, lanjutkan ke LoginActivity
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        finish(); // Hentikan aktivitas ini
    }
}

