package com.example.lazismuapp.Model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String message;
    private String access_token;
    private String token_type;

    private String nama;

    // Getter methods
    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }
    public String getUserName() {
        return nama;
    }
}

