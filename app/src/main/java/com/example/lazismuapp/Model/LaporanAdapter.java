package com.example.lazismuapp.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Laporan.tambahLaporan;
import com.example.lazismuapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.MyViewHolder> {
    private List<LaporanModel> list;
    private List<LaporanModel> filteredList;
    private Context context;
    ApiService apiService;

    public LaporanAdapter(Context context,List<LaporanModel> list) {
        this.list = list;
        this.filteredList = new ArrayList<>(list);
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_laporan, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LaporanModel laporan = filteredList.get(position);
        holder.nama.setText(laporan.getNamaMuz());
        holder.jumlahDana.setText(String.valueOf(laporan.getJumlahDana()));
        holder.tanggal.setText(laporan.getTanggal());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, tambahLaporan.class);
            intent.putExtra("laporan", laporan);
            context.startActivity(intent);
        });
        holder.btndelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus laporan ini?")
                    .setPositiveButton("Ya", (dialog, which) -> deleteLaporan(laporan.getId(), position))
                    .setNegativeButton("Tidak", null)
                    .show();
        });
    }
    private void deleteLaporan(int id, int position) {
        String baseUrl = context.getString(R.string.BASE_URL);
        Retrofit retrofit = RetrofitClient.getClient(baseUrl);
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Void> call = apiService.deleteLaporan(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Laporan berhasil dihapus", Toast.LENGTH_SHORT).show();
                    filteredList.remove(position);
                    list.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(context, "Gagal menghapus Laporan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterList(List<LaporanModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    // Method untuk mengatur data yang akan ditampilkan secara langsung
    public void setData(List<LaporanModel> data) {
        this.list.clear();
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, jumlahDana,tanggal;
        ImageButton btnEdit,btndelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.textView4);
            jumlahDana = itemView.findViewById(R.id.textView7);
            tanggal = itemView.findViewById(R.id.textView90);
            btnEdit = itemView.findViewById(R.id.btn_edit_laporan);
            btndelete = itemView.findViewById(R.id.deletelaporan);
        }
    }
}
