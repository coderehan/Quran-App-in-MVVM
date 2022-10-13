package com.rehan.quranapp.network;

import com.rehan.quranapp.model.SurahDetailResponse;
import com.rehan.quranapp.model.SurahResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("surah")
    Call<SurahResponse> getSurah();

    @GET("sura/{language}/{id}")
    Call<SurahDetailResponse> getSurahDetail(@Path("language") String language,
                                             @Path("id") int surahId);
}
