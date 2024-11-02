// JadwalModel.java
package com.example.lazismuapp.Model;

import java.util.List;

public class JadwalModel {
    private int id;
    private String bulan;
    private String deadline_bulan;
    private List<PengambilanModel> jadwalpengambilans;  // Ensure this matches your JSON structure

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }

    public String getDeadline_bulan() {
        return deadline_bulan;
    }

    public void setDeadline_bulan(String deadline_bulan) {
        this.deadline_bulan = deadline_bulan;
    }

    public List<PengambilanModel> getJadwalPengambilans() {
        return jadwalpengambilans;
    }

    public void setJadwalPengambilans(List<PengambilanModel> jadwalpengambilans) {
        this.jadwalpengambilans = jadwalpengambilans;
    }
}

