package com.example.lazismuapp.Model;

import com.google.gson.annotations.SerializedName;

public class Infoterkini {
    @SerializedName("Pemasukan")
    private int Pemasukan;
    @SerializedName("Pengeluaran")
    private int Pengeluaran;
    @SerializedName("Total")
    private int Total;
    @SerializedName("Jumlah-Muzzaki")
    private int JumlahMuzzaki;
    @SerializedName("Jumlah-Mustahik")
    private int JumlahMustahik;
    @SerializedName("Penyaluran-beras")
    private double PenyaluranBeras;
    @SerializedName("Total-beras")
    private double TotalBeras;

    public double getPenyaluranBeras() {
        return PenyaluranBeras;
    }

    public void setPenyaluranBeras(int penyaluranBeras) {
        PenyaluranBeras = penyaluranBeras;
    }

    public double getTotalBeras() {
        return TotalBeras;
    }

    public void setTotalBeras(float totalBeras) {
        TotalBeras = totalBeras;
    }

    // Getters and Setters
    public int getPemasukan() {
        return Pemasukan;
    }

    public void setPemasukan(int pemasukan) {
        Pemasukan = pemasukan;
    }

    public int getPengeluaran() {
        return Pengeluaran;
    }

    public void setPengeluaran(int pengeluaran) {
        Pengeluaran = pengeluaran;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public int getJumlahMuzzaki() {
        return JumlahMuzzaki;
    }

    public void setJumlahMuzzaki(int jumlahMuzzaki) {
        JumlahMuzzaki = jumlahMuzzaki;
    }

    public int getJumlahMustahik() {
        return JumlahMustahik;
    }

    public void setJumlahMustahik(int jumlahMustahik) {
        JumlahMustahik = jumlahMustahik;
    }
}

