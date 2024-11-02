package com.example.lazismuapp.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lazismuapp.Date.CurrencyUtils;
import com.example.lazismuapp.R;

import java.util.List;

public class InfoTerkiniAdapter extends RecyclerView.Adapter<InfoTerkiniAdapter.ViewHolder> {

    private List<Infoterkini> infoList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pemasukanView;
        public TextView pengeluaranView;
        public TextView totalView;
        public TextView jumlahMuzzakiView;
        public TextView jumlahMustahikView,jumlahBerasView,jumlahBerasDisalurkanView;

        public ViewHolder(View v) {
            super(v);
            pemasukanView = v.findViewById(R.id.pemasukan);
            pengeluaranView = v.findViewById(R.id.pengeluaran);
            totalView = v.findViewById(R.id.total);
            jumlahMuzzakiView = v.findViewById(R.id.jumlahMuzzaki);
            jumlahMustahikView = v.findViewById(R.id.jumlahMustahik);
            jumlahBerasView = v.findViewById(R.id.jumlahberas);
            jumlahBerasDisalurkanView = v.findViewById(R.id.berasdisalurkan);
        }
    }

    public InfoTerkiniAdapter(List<Infoterkini> infoList) {
        this.infoList = infoList;
    }

    @Override
    public InfoTerkiniAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_info_terkini, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Infoterkini infoTerkini = infoList.get(position);
        String pemasukanFormatted = CurrencyUtils.formatRupiah(infoTerkini.getPemasukan());
        String pengeluaranFormatted = CurrencyUtils.formatRupiah(infoTerkini.getPengeluaran());
        String totalFormatted = CurrencyUtils.formatRupiah(infoTerkini.getTotal());

        holder.pemasukanView.setText(pemasukanFormatted);
        holder.pengeluaranView.setText(pengeluaranFormatted);
        holder.totalView.setText(totalFormatted);
        holder.jumlahMuzzakiView.setText(String.valueOf(infoTerkini.getJumlahMuzzaki()));
        holder.jumlahMustahikView.setText(String.valueOf(infoTerkini.getJumlahMustahik()));
        holder.jumlahBerasView.setText(String.valueOf(infoTerkini.getTotalBeras())+" Kg");
        holder.jumlahBerasDisalurkanView.setText(String.valueOf(infoTerkini.getPenyaluranBeras())+" Kg");
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }
}

