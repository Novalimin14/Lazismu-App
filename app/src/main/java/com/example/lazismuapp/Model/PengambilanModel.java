package com.example.lazismuapp.Model;

import com.google.gson.annotations.SerializedName;

public class PengambilanModel {
    @SerializedName("id")
    private int id;
    @SerializedName("jadwal_id")
    private int jadwalId;
    @SerializedName("muzzaki_id")
    private int muzzakiId;
    @SerializedName("is_checked")
    private int isChecked;
    @SerializedName("muzzaki")
    private MuzzakiModel muzzaki;  // Ini adalah objek tunggal, bukan daftar

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJadwalId() {
        return jadwalId;
    }

    public void setJadwalId(int jadwalId) {
        this.jadwalId = jadwalId;
    }

    public int getMuzzakiId() {
        return muzzakiId;
    }

    public void setMuzzakiId(int muzzakiId) {
        this.muzzakiId = muzzakiId;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public MuzzakiModel getMuzzaki() {
        return muzzaki;
    }

    public void setMuzzaki(MuzzakiModel muzzaki) {
        this.muzzaki = muzzaki;
    }
}
