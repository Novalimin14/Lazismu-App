package com.example.lazismuapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MuzzakiModel implements Serializable {

    @SerializedName("id")
    private int Id;
    @SerializedName("nama")
    private String Nama;
    @SerializedName("alamat")
    private String Alamat;

    @SerializedName("ktp")
    private String ktp;

    @SerializedName("jkl")
    private String jkl;

    @SerializedName("pekerjaan")
    private String pekerjaan;

    @SerializedName("linkmaps")
    private String linkmaps;

    public void Muzzaki(int Id,String nama, String alamat, String ktp, String jkl, String pekerjaan, String linkmaps) {
        this.Id = Id;
        this.Nama = nama;
        this.Alamat = alamat;
        this.ktp = ktp;
        this.jkl = jkl;
        this.pekerjaan = pekerjaan;
        this.linkmaps = linkmaps;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        Id = Id;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }
    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getJkl() {
        return jkl;
    }

    public void setJkl(String jkl) {
        this.jkl = jkl;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getLinkmaps() {
        return linkmaps;
    }

    public void setLinkmaps(String linkmaps) {
        this.linkmaps = linkmaps;
    }
}
