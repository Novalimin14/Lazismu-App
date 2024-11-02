package com.example.lazismuapp.Api;
import com.example.lazismuapp.Model.Infoterkini;
import com.example.lazismuapp.Model.JadwalModel;
import com.example.lazismuapp.Model.JadwalRequest;
import com.example.lazismuapp.Model.LaporanModel;
import com.example.lazismuapp.Model.LoginRequest;
import com.example.lazismuapp.Model.LoginResponse;
import com.example.lazismuapp.Model.MustahikModel;
import com.example.lazismuapp.Model.MuzzakiModel;
import com.example.lazismuapp.Model.PengambilanModel;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @Headers("Accept: application/json")

    @POST("login-karyawan")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("laporan")
    Call<List<LaporanModel>> getLaporan();
    @GET("data_muzzaki")
    Call<List<MuzzakiModel>> getProfile();
    @GET("table_mustahik")
    Call<List<MustahikModel>> getMustahik();
    @GET("infoterkini")
    Call<Infoterkini> getInfoTerkini();
    @GET("jadwals")
    Call<List<JadwalModel>> getJadwals();
    
//    BAGIAN POST
    @POST("postlaporan")
    Call<Void> postLaporan(@Body LaporanModel laporanModel);
    @POST("postmustahik")
    Call<Void> postMustahik(@Body MustahikModel mustahikModel);
    @POST("postmuzzaki")
    Call<Void> postMuzzaki(@Body MuzzakiModel muzzakiModel);
    @POST("jadwals")
    Call<Void> posttambahJadwal(@Body JadwalRequest jadwalRequest);

    // UPDATE
    @PUT("laporan/{id}")
    Call<Void> updateLaporan(@Path("id") int id, @Body LaporanModel laporanModel);

    @PUT("mustahik/{id}")
    Call<Void> updateMustahik(@Path("id") int id, @Body MustahikModel mustahikModel);
    @PUT("muzzaki/{id}")
    Call<Void> updateMuzzaki(@Path("id") int id, @Body MuzzakiModel muzzakiModel);

    //JADWAL Route::post('jadwals/{jadwal_id}/pengambilan', [JadwalController::class, 'addJadwalPengambilan']);

    @GET("jadwals/{jadwal_id}/mustahiks")
    Call<List<PengambilanModel>> getMuzzaki(@Path("jadwal_id") int jadwalId);
    @POST("jadwals/{jadwal_id}/pengambilan/{muzzaki_id}")
    Call<PengambilanModel> postMuzzakiJadwal(@Path("jadwal_id") int jadwalId,@Path("muzzaki_id") int muzzaki_id);
    @POST("jadwals/tambahdata/{jadwal_id}")
    Call<PengambilanModel> addJadwal(@Path("jadwal_id") int jadwalId);

    @PATCH("jadwals/mustahiks/{pengambilan_id}")
    Call<PengambilanModel> updateMustahikChecklist(@Path("pengambilan_id") int pengambilanId);

    //DELETE
    @DELETE("delete-mustahik")
    Call<Void> deleteMustahik(@Query("id") int id);
    @DELETE("delete-laporan")
    Call<Void> deleteLaporan(@Query("id") int id);
    @DELETE("delete-muzzaki")
    Call<Void> deleteMuzzaki(@Query("id") int id);
    @DELETE("delete-jadwal")
    Call<Void> deleteJadwal(@Query("id") int id);
}
