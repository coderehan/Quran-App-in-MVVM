package com.rehan.quranapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Retrofit retrofit;
    private static final String BASE_URL_1 = "http://api.alquran.cloud/v1/";
    private static final String BASE_URL_2 = "https://quranenc.com/api/translation/";

    public static Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_1)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //here we are using different base urls from internet to display data, so we have to set retrofit instance to null
    public static Retrofit getInstance(){
        if(retrofit != null){
            retrofit = null;
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
