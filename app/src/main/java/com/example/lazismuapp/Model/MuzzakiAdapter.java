package com.example.lazismuapp.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.lazismuapp.Mustahik.tambahMustahik;
import com.example.lazismuapp.R;
import com.example.lazismuapp.profile.ui.theme.tambahMuzzaki;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MuzzakiAdapter extends RecyclerView.Adapter<MuzzakiAdapter.MyViewHolder> {
    private List<MuzzakiModel> list;
    private List<MuzzakiModel> filteredList;
    private OnItemClickListener listener;
    private Context context;


    public MuzzakiAdapter(Context context,List<MuzzakiModel> list) {
        this.list = list;
        this.filteredList = new ArrayList<>(list);
        this.context = context;
    }

    @NonNull
    @Override
    public MuzzakiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_muzzaki, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MuzzakiAdapter.MyViewHolder holder, int position) {
        MuzzakiModel muzzaki = filteredList.get(position);
        holder.nama.setText(muzzaki.getNama());
        String alamat = muzzaki.getAlamat();
        if (alamat.length() > 20) {
            alamat = alamat.substring(0, 20) + "..."; // Memotong dan menambahkan "..." di akhir
        }
        holder.alamat.setText(alamat);

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, tambahMuzzaki.class);
            intent.putExtra("muzzaki", muzzaki);
            context.startActivity(intent);
        });

        holder.ceklokasi.setOnClickListener(v -> {
            String linkmaps = muzzaki.getLinkmaps();
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
                    .setMessage("Apakah Anda yakin ingin menghapus muzzaki ini?")
                    .setPositiveButton("Ya", (dialog, which) -> deleteMuzzaki(muzzaki.getId(), position))
                    .setNegativeButton("Tidak", null)
                    .show();
        });
    }
    private void deleteMuzzaki(int id, int position) {
        String baseUrl = context.getString(R.string.BASE_URL);
        Retrofit retrofit = RetrofitClient.getClient(baseUrl);
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Void> call = apiService.deleteMuzzaki(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Muzzaki berhasil dihapus", Toast.LENGTH_SHORT).show();
                    filteredList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(context, "Gagal menghapus Muzzaki"+ response.message(), Toast.LENGTH_SHORT).show();
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

    public void filterList(List<MuzzakiModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(MuzzakiModel profile);
    }

    public void setData(List<MuzzakiModel> data) {
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
            nama = itemView.findViewById(R.id.listnama);
            alamat = itemView.findViewById(R.id.listalamat);
            btnEdit = itemView.findViewById(R.id.edit_muzzaki);
            ceklokasi = itemView.findViewById(R.id.button4);
            btndelete = itemView.findViewById(R.id.delete_muzzaki);
        }
    }
}
