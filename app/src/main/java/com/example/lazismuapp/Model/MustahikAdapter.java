package com.example.lazismuapp.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
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
import com.example.lazismuapp.Mustahik.tambahMustahik;
import com.example.lazismuapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MustahikAdapter extends RecyclerView.Adapter<MustahikAdapter.MyViewHolder>{
    private List<MustahikModel> list;
    private List<MustahikModel> filteredList;
    private Context context;
    ApiService apiService;

    public MustahikAdapter(Context context, List<MustahikModel> list) {
        this.list = list;
        this.filteredList = new ArrayList<>(list);
        this.context = context;
    }

    @NonNull
    @Override
    public MustahikAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mustahik, parent, false);
        return new MustahikAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MustahikAdapter.MyViewHolder holder, int position) {
        MustahikModel mustahik = filteredList.get(position);
        holder.nama.setText(mustahik.getNama_mus());
        String alamat = mustahik.getAlamat();
        if (alamat.length() > 20) {
            alamat = alamat.substring(0, 20) + "..."; // Memotong dan menambahkan "..." di akhir
        }
        holder.alamat.setText(alamat);


        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, tambahMustahik.class);
            intent.putExtra("mustahik", mustahik);
            context.startActivity(intent);
        });
        holder.ceklokasi.setOnClickListener(v -> {
            String linkmaps = mustahik.getLink_maps();
            if (linkmaps != null && !linkmaps.isEmpty()) {
                try {
                    Uri gmmIntentUri = Uri.parse(linkmaps);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                        v.getContext().startActivity(mapIntent);
                    } else {
                        showAlternativeMapOption(v.getContext(), gmmIntentUri);
                    }
                } catch (Exception e) {
                    // Tangani kesalahan yang mungkin terjadi
                    Toast.makeText(v.getContext(), "Terjadi kesalahan saat mencoba membuka peta.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(v.getContext(), "URL tidak valid", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btndelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus mustahik ini?")
                    .setPositiveButton("Ya", (dialog, which) -> deleteMustahik(mustahik.getId(), position))
                    .setNegativeButton("Tidak", null)
                    .show();
        });


    }
    private void deleteMustahik(int id, int position) {
        String baseUrl = context.getString(R.string.BASE_URL);
        Retrofit retrofit = RetrofitClient.getClient(baseUrl);
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Void> call = apiService.deleteMustahik(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Mustahik berhasil dihapus", Toast.LENGTH_SHORT).show();
                    filteredList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(context, "Gagal menghapus mustahik", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAlternativeMapOption(Context context, Uri mapUri) {
        Toast.makeText(context, "Aplikasi Google Maps tidak ditemukan, coba gunakan aplikasi lain untuk membuka lokasi.", Toast.LENGTH_SHORT).show();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        context.startActivity(browserIntent);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterList(List<MustahikModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    // Method untuk mengatur data yang akan ditampilkan secara langsung
    public void setData(List<MustahikModel> data) {
        this.list.clear();
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat;
        Button ceklokasi;
        ImageButton btnEdit,btndelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.menu_nama_mustahik);
            alamat = itemView.findViewById(R.id.menu_alamat_mustahik);
            btnEdit = itemView.findViewById(R.id.edit_mustahik);
            ceklokasi = itemView.findViewById(R.id.menu_ceklokasi_mustahik);
            btndelete = itemView.findViewById(R.id.button_delete_mustahik);
        }
    }
}
