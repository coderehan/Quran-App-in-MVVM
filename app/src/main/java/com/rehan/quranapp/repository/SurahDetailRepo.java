package com.rehan.quranapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rehan.quranapp.model.SurahDetail;
import com.rehan.quranapp.model.SurahDetailResponse;
import com.rehan.quranapp.model.SurahResponse;
import com.rehan.quranapp.network.Api;
import com.rehan.quranapp.network.JsonPlaceHolderApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurahDetailRepo {

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    public SurahDetailRepo(){
        jsonPlaceHolderApi = Api.getInstance().create(JsonPlaceHolderApi.class);
    }

    public LiveData<SurahDetailResponse> getSurahDetail(String language, int surahId){
        MutableLiveData<SurahDetailResponse> data = new MutableLiveData<>();
        jsonPlaceHolderApi.getSurahDetail(language, surahId).enqueue(new Callback<SurahDetailResponse>() {
            @Override
            public void onResponse(Call<SurahDetailResponse> call, Response<SurahDetailResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SurahDetailResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
