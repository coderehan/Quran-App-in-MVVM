package com.rehan.quranapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rehan.quranapp.R;
import com.rehan.quranapp.listener.SurahListener;
import com.rehan.quranapp.model.Surah;

import java.util.List;

public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder> {

    private Context context;
    private List<Surah> list;
    private SurahListener surahListener;

    public SurahAdapter(Context context, List<Surah> list, SurahListener surahListener) {
        this.context = context;
        this.list = list;
        this.surahListener = surahListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.surah_layout, parent, false);
        return new ViewHolder(view, surahListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSurahNumber.setText(String.valueOf(list.get(position).getNumber()));
        holder.tvSurahName.setText(list.get(position).getEnglishName());
        holder.tvAyahs.setText("Total Ayahs : " + String.valueOf(list.get(position).getNumberOfAyahs()));
        holder.tvArabicName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSurahNumber, tvSurahName, tvAyahs, tvArabicName;
        private SurahListener surahListener;

        public ViewHolder(@NonNull View itemView, SurahListener surahListener) {
            super(itemView);

            tvSurahNumber = itemView.findViewById(R.id.tvSurahNumber);
            tvSurahName = itemView.findViewById(R.id.tvSurahName);
            tvAyahs = itemView.findViewById(R.id.tvAyahs);
            tvArabicName = itemView.findViewById(R.id.tvArabicName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    surahListener.onSurahListener(getAdapterPosition());
                }
            });
        }
    }
}
