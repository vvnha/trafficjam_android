package com.example.nhavo.services;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("jam/upimg")
    Call<ResponseBody> Upload(
            @Part MultipartBody.Part photo,
            @Part("description") RequestBody description,
            @Part("longtitude") RequestBody longtitude,
            @Part("latitude") RequestBody latitude,
            @Part("streetName") RequestBody streetName,
            @Part("cityName") RequestBody cityName
    );
}
