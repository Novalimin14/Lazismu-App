package com.example.lazismuapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LaporanModel implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("muzzaki_id")
    private String muzzakiId;
    @SerializedName("nama_muz")
    private String namaMuz;
    @SerializedName("kwitansi")
    private String kwitansi;
    @SerializedName("jml_dana")
    private int jumlahDana;
    @SerializedName("jml_beras")
    private double jumlahBeras;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("tanggal")
    private String tanggal;


    public void Laporan(int id, String muzzakiId, String namaMuz,String kwitansi,int jumlahDana,double jumlahBeras, String keterangan, String tanggal, String createdAt, String updatedAt) {
        this.id = id;
        this.muzzakiId = muzzakiId;
        this.namaMuz = namaMuz;
        this.kwitansi = kwitansi;
        this.jumlahDana = jumlahDana;
        this.jumlahBeras = jumlahBeras;
        this.keterangan = keterangan;
        this.tanggal = tanggal;

    }

    public double getJumlahBeras() {
        return jumlahBeras;
    }

    public void setJumlahBeras(double jumlahBeras) {
        this.jumlahBeras = jumlahBeras;
    }

    public int getId() {
        return id;
    }
    public String getKwitansi() {
        return kwitansi;
    }

    public void setKwitansi(String kwitansi) {
        this.kwitansi = kwitansi;
    }

    public String getMuzzakiId() {
        return muzzakiId;
    }

    public void setMuzzakiId(String muzzakiId) {
        this.muzzakiId = muzzakiId;
    }

    public String getNamaMuz() {
        return namaMuz;
    }

    public void setNamaMuz(String namaMuz) {
        this.namaMuz = namaMuz;
    }

    public int getJumlahDana() {
        return jumlahDana;
    }

    public void setJumlahDana(int jumlahDana) {
        this.jumlahDana = jumlahDana;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

}


