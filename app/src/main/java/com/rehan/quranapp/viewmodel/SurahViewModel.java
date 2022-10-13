package com.rehan.quranapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.rehan.quranapp.model.SurahResponse;
import com.rehan.quranapp.repository.SurahRepo;

public class SurahViewModel extends ViewModel {

    private SurahRepo surahRepo;

    public SurahViewModel(){
        surahRepo = new SurahRepo();
    }

    public LiveData<SurahResponse> getSurah(){
        return surahRepo.getSurah();
    }
}
