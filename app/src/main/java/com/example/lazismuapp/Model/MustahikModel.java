package com.example.lazismuapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class MustahikModel implements Serializable {
    @SerializedName("id")
    private int Id;
    @SerializedName("nama_mus")
    private String nama_mus;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("jkl")
    private String jkl;
    @SerializedName("ktp")
    private String ktp;
    @SerializedName("pekerjaan")
    private String pekerjaan;
    @SerializedName("jns_mus")
    private String jns_mus;
    @SerializedName("tipe_mus")
    private String tipe_mus;
    @SerializedName("KTM")
    private String ktm;
    @SerializedName("spres")
    private String spres;
    @SerializedName("Skel")
    private String skel;
    @SerializedName("Sktm")
    private String Sktm;
    @SerializedName("sprem")
    private String sprem;
    @SerializedName("gaji")
    private String gaji;
    @SerializedName("status_2")
    private String status;
    @SerializedName("keterangan")
    private String keterangan;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("link_maps")
    private String link_maps;



    public void Mustahik(int Id, String nama_mus,String tanggal,String status,String sprem,String skel,String ktm,String jns_mus, String alamat, String ktp, String pekerjaan, String tipe_mus, String spres, String sktm, String gaji, String keterangan, String link_maps) {
        this.Id = Id;
        this.nama_mus = nama_mus;
        this.alamat = alamat;
        this.status = status;
        this.tanggal = tanggal;
        this.sprem = sprem;
        this.skel = skel;
        this.ktm = ktm;
        this.jns_mus = jns_mus;
        this.ktp = ktp;
        this.pekerjaan = pekerjaan;
        this.tipe_mus = tipe_mus;
        this.spres = spres;
        this.Sktm = sktm;
        this.gaji = gaji;
        this.keterangan = keterangan;
        this.link_maps = link_maps;
    }
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
    public String getNama_mus() {
        return nama_mus;
    }

    public void setNama_mus(String nama_mus) {
        this.nama_mus = nama_mus;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getTipe_mus() {
        return tipe_mus;
    }

    public void setTipe_mus(String tipe_mus) {
        this.tipe_mus = tipe_mus;
    }

    public String getSpres() {
        return spres;
    }

    public void setSpres(String spres) {
        this.spres = spres;
    }

    public String getSktm() {
        return Sktm;
    }

    public void setSktm(String sktm) {
        this.Sktm = sktm;
    }

    public String getGaji() {
        return gaji;
    }

    public void setGaji(String gaji) {
        this.gaji = gaji;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getLink_maps() {
        return link_maps;
    }

    public void setLink_maps(String link_maps) {
        this.link_maps = link_maps;
    }
    public String getJkl() {
        return jkl;
    }

    public void setJkl(String jkl) {
        this.jkl = jkl;
    }

    public String getJns_mus() {
        return jns_mus;
    }

    public void setJns_mus(String jns_mus) {
        this.jns_mus = jns_mus;
    }

    public String getKtm() {
        return ktm;
    }

    public void setKtm(String ktm) {
        this.ktm = ktm;
    }

    public String getSkel() {
        return skel;
    }

    public void setSkel(String skel) {
        this.skel = skel;
    }

    public String getSprem() {
        return sprem;
    }

    public void setSprem(String sprem) {
        this.sprem = sprem;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}


