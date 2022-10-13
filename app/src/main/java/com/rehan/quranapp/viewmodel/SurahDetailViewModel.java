package com.rehan.quranapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rehan.quranapp.model.SurahDetailResponse;
import com.rehan.quranapp.repository.SurahDetailRepo;

public class SurahDetailViewModel extends ViewModel {

    private SurahDetailRepo surahDetailRepo;

    public SurahDetailViewModel(){
        surahDetailRepo = new SurahDetailRepo();
    }

    public LiveData<SurahDetailResponse> getSurahDetail(String language, int surahId){
        return surahDetailRepo.getSurahDetail(language, surahId);
    }
}
