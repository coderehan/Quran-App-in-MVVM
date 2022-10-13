package com.rehan.quranapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rehan.quranapp.R;
import com.rehan.quranapp.adapters.SurahDetailAdapter;
import com.rehan.quranapp.common.Common;
import com.rehan.quranapp.databinding.ActivitySurahDetailBinding;
import com.rehan.quranapp.model.SurahDetail;
import com.rehan.quranapp.viewmodel.SurahDetailViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SurahDetailActivity extends AppCompatActivity {

    private ActivitySurahDetailBinding binding;
    private List<SurahDetail> list;
    private SurahDetailAdapter surahDetailAdapter;
    private SurahDetailViewModel surahDetailViewModel;

    private final String urdu = "urdu_junagarhi";
    private final String hindi = "hindi_omari";
    private final String english = "english_hilali_khan";

    private RadioGroup rgTranslation, rgQari;
    private RadioButton rbTranslation, rbQari;
    private String language;
    private int no;

    private final String qari_abdul_basit = "abdul_basit_murattal";
    private final String qari_abdul_wadood = "abdul_wadood_haneef_rare";
    private String qari;
    Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_surah_detail);

        //getting data from source activity and setting in destination activity
        no = getIntent().getIntExtra(Common.SURAH_NO, 0);
        binding.tvSurahName.setText(getIntent().getStringExtra(Common.SURAH_NAME));
        binding.tvSurahTranslation.setText(getIntent().getStringExtra(Common.SURAH_TRANSLATION));
        binding.tvRevelationType.setText(getIntent().getStringExtra(Common.SURAH_TYPE) + " " +
                getIntent().getIntExtra(Common.SURAH_TOTAL_AYAH, 0) + "Ayahs");

        binding.rvSurahDetail.setHasFixedSize(true);
        list = new ArrayList<>();
        surahTranslation(urdu, no);

        //showing suggestions while typing in edittext
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        binding.ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SurahDetailActivity.this, R.style.BottomSheetDialogTheme);
                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                View view = layoutInflater.inflate(R.layout.bottom_sheet_layout, findViewById(R.id.sheetContainer));

                view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rgTranslation = view.findViewById(R.id.rgTranslation);
                        rgQari = view.findViewById(R.id.rgQari);
                        int selectedId = rgTranslation.getCheckedRadioButtonId();
                        rbTranslation = view.findViewById(selectedId);

                        if(selectedId==-1){
                            Toast.makeText(SurahDetailActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SurahDetailActivity.this, "Selected", Toast.LENGTH_SHORT).show();
                        }

                        if(rbTranslation.getText().toString().toLowerCase().trim().equals("urdu")){
                            language = urdu;
                        }else if(rbTranslation.getText().toString().toLowerCase().trim().equals("hindi")){
                            language = hindi;
                        }else if(rbTranslation.getText().toString().toLowerCase().trim().equals("english")) {
                            language = english;
                        }

                        surahTranslation(language, no);

                        int id = rgQari.getCheckedRadioButtonId();
                        rbQari = view.findViewById(id);
                        if(rbQari.getText().toString().toLowerCase().trim().equals("abdul basit murattal")){
                            qari = qari_abdul_basit;
                        }else if(rbTranslation.getText().toString().toLowerCase().trim().equals("abdul wadood haneef rare")){
                            qari = qari_abdul_wadood;
                        }
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        listenAudio(qari);

                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
            }
        });

        listenAudio(qari_abdul_basit);

    }

    private void listenAudio(String qari) {
        mediaPlayer = new MediaPlayer();
        binding.sbAudio.setMax(100);
        binding.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    binding.ivPlay.setImageResource(R.drawable.icon_play);
                }else{
                    mediaPlayer.start();
                    binding.ivPlay.setImageResource(R.drawable.icon_pause);
                    updateSeekBar();
                }
            }
        });

        preparedMediaPlayer(qari);

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                binding.sbAudio.setSecondaryProgress(i);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                binding.sbAudio.setProgress(0);
                binding.ivPlay.setImageResource(R.drawable.icon_play);
                binding.tvStartTime.setText("0:00");
                binding.tvTotalTime.setText("0:00");
                mediaPlayer.reset();
                preparedMediaPlayer(qari);
            }
        });

        binding.sbAudio.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                SeekBar seekBar = (SeekBar) v;
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                binding.tvStartTime.setText(timeToMilliSecond(mediaPlayer.getCurrentPosition()));
                return false;

            }
        });
    }

    private void preparedMediaPlayer(String qari) {
        if(no<10){
            str = "00" + no;
        }else if(no<100){
            str = "0" + no;
        }else if(no>=100){
            str = String.valueOf(no);
        }

        try {
            mediaPlayer.setDataSource("https://download.quranicaudio.com/quran/" + qari + "/" + str.trim() + ".mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding.tvTotalTime.setText(timeToMilliSecond(mediaPlayer.getDuration()));

    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            binding.tvStartTime.setText(timeToMilliSecond(currentDuration));
        }
    };

    private void updateSeekBar(){
        if(mediaPlayer.isPlaying()){
            binding.sbAudio.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }

    private String timeToMilliSecond(long milliSecond){
        String timerString = "";
        String secondString = "";

        int hours = (int) (milliSecond / (1000*60*60));
        int minutes = (int) (milliSecond % (1000*60*60)) / (1000*60);
        int seconds = (int) (milliSecond % (1000*60*60)) % (1000*60) / 1000;

        if(hours>0){
            timerString = hours + ":";
        }
        if(seconds<10){
            secondString = "0" + seconds;
        }else {
            secondString = "" + seconds;
        }
        timerString = timerString + minutes + ":" + secondString;
        return timerString;



    }

    private void filter(String id) {
        ArrayList<SurahDetail> arrayList = new ArrayList<>();
        for (SurahDetail detail : list){
            if(String.valueOf(detail.getId()).contains(id)){
                arrayList.add(detail);
            }
        }
        surahDetailAdapter.filter(arrayList);
    }

    private void surahTranslation(String language, int surahId){
        if(list.size()>0){
            list.clear();
        }

        surahDetailViewModel = new ViewModelProvider(this).get(SurahDetailViewModel.class);

        surahDetailViewModel.getSurahDetail(language, surahId)
                .observe(this, surahDetailResponse -> {

                    for (int i=0; i<surahDetailResponse.getList().size(); i++){
                        list.add(new SurahDetail(surahDetailResponse.getList().get(i).getId(),
                                surahDetailResponse.getList().get(i).getSura(),
                                surahDetailResponse.getList().get(i).getAya(),
                                surahDetailResponse.getList().get(i).getArabic_text(),
                                surahDetailResponse.getList().get(i).getTranslation(),
                                surahDetailResponse.getList().get(i).getFootnotes()
                        ));
                    }

                    if(list.size()!=0){
                        surahDetailAdapter = new SurahDetailAdapter(this, list);
                        binding.rvSurahDetail.setAdapter(surahDetailAdapter);
                    }
        });
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer.isPlaying()) {
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            binding.ivPlay.setImageResource(R.drawable.icon_play);
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if(mediaPlayer.isPlaying()) {
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            binding.ivPlay.setImageResource(R.drawable.icon_play);
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if(mediaPlayer.isPlaying()) {
            handler.removeCallbacks(updater);
            mediaPlayer.pause();
            binding.ivPlay.setImageResource(R.drawable.icon_play);
        }
        super.onPause();
    }
}