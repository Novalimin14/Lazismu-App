package com.example.lazismuapp.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazismuapp.Api.ApiService;
import com.example.lazismuapp.Api.RetrofitClient;
import com.example.lazismuapp.Date.DateUtils;
import com.example.lazismuapp.Jadwal.UpdateJadwalActivity;
import com.example.lazismuapp.Model.JadwalModel;
import com.example.lazismuapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder> {

    private List<JadwalModel> jadwalList;
    private Context context;

    public JadwalAdapter(Context context, List<JadwalModel> jadwalList) {
        this.context = context;
        this.jadwalList = jadwalList;
    }

    @NonNull
    @Override
    public JadwalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jadwal, parent, false);
        return new JadwalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalViewHolder holder, int position) {
        JadwalModel jadwal = jadwalList.get(position);
        String bulanDanTahun = DateUtils.getMonthAndYear(jadwal.getBulan());
        String deadlineBulanDanTahun = DateUtils.getMonthAndYear(jadwal.getDeadline_bulan());

        holder.tvBulan.setText(bulanDanTahun);
        holder.tvDeadline.setText(deadlineBulanDanTahun);
        holder.updateJad.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateJadwalActivity.class);
            intent.putExtra("jadwal_id", jadwal.getId());
            intent.putExtra("pengambilan_id", jadwal.getId());
            context.startActivity(intent);
        });
        holder.btndelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus Jadwal ini?")
                    .setPositiveButton("Ya", (dialog, which) -> deleteJadwal(jadwal.getId(), position))
                    .setNegativeButton("Tidak", null)
                    .show();
        });
    }
    private void deleteJadwal(int id, int position) {
        String baseUrl = context.getString(R.string.BASE_URL);
        Retrofit retrofit = RetrofitClient.getClient(baseUrl);
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Void> call = apiService.deleteJadwal(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Jadwal berhasil dihapus", Toast.LENGTH_SHORT).show();
//                    filteredList.remove(position);
                    jadwalList.remove(position);
                    notifyItemRemoved(position);

                } else {
                    Toast.makeText(context, "Gagal menghapus Jadwal"+id+ response.message(), Toast.LENGTH_SHORT).show();
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
        return jadwalList.size();
    }

    public static class JadwalViewHolder extends RecyclerView.ViewHolder {

        TextView tvBulan, tvDeadline;
        ImageButton updateJad,btndelete;

        public JadwalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBulan = itemView.findViewById(R.id.tvBulan);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            updateJad = itemView.findViewById(R.id.button_updatejadwal);
            btndelete = itemView.findViewById(R.id.button_deletejadwal);
        }
    }
}

