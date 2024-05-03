package com.example.garbagedisposal;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST()
    Call<AddGarbageRes> addGarbage(@Part MultipartBody.Part image);
}
