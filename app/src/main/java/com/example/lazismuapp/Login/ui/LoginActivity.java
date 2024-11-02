package com.example.lazismuapp.Login.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.lazismuapp.Api.ApiClient;
import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.MainActivity;
import com.example.lazismuapp.Model.LoginRequest;
import com.example.lazismuapp.Model.LoginResponse;
import com.example.lazismuapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    ApiService apiService;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        Retrofit retrofit = RetrofitClient.getClient(getString(R.string.BASE_URL));
        apiService = retrofit.create(ApiService.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                login(email, password);
            }
        });
    }

    private void login(String email, String password) {
//        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    // Simpan token ke SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("MY_APP", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("TOKEN", loginResponse.getAccessToken());
                    editor.putString("USER_NAME", loginResponse.getUserName());
                    editor.apply();

                    // Lanjutkan ke MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (response.errorBody() != null) {
                        try {
                            JSONObject errorResponse = new JSONObject(response.errorBody().string());
                            if (errorResponse.has("errors")) {
                                JSONObject errors = errorResponse.getJSONObject("errors");
                                if (errors.has("email")) {
                                    JSONArray emailErrors = errors.getJSONArray("email");
                                    String emailErrorMessage = emailErrors.getString(0); // Ambil pesan kesalahan pertama
                                    Toast.makeText(LoginActivity.this, emailErrorMessage, Toast.LENGTH_SHORT).show();
                                }
                                if (errors.has("password")) {
                                    JSONArray passwordErrors = errors.getJSONArray("password");
                                    String passwordErrorMessage = passwordErrors.getString(0); // Ambil pesan kesalahan pertama
                                    Toast.makeText(LoginActivity.this, passwordErrorMessage, Toast.LENGTH_SHORT).show();
                                }
                            } else if (errorResponse.has("message")) {
                                String errorMessage = errorResponse.getString("message");
                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
