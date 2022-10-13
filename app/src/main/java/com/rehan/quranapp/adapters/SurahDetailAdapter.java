package com.rehan.quranapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rehan.quranapp.R;
import com.rehan.quranapp.activities.SurahDetailActivity;
import com.rehan.quranapp.model.SurahDetail;

import java.util.ArrayList;
import java.util.List;

public class SurahDetailAdapter extends RecyclerView.Adapter<SurahDetailAdapter.ViewHolder> {

    private Context context;
    private List<SurahDetail> list;

    public SurahDetailAdapter(Context context, List<SurahDetail> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.surah_detail_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvAyahNumber.setText(String.valueOf(list.get(position).getAya()));
        holder.tvArabic.setText(list.get(position).getArabic_text());
        holder.tvTranslation.setText(list.get(position).getTranslation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter(ArrayList<SurahDetail> details){
        list = details;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAyahNumber, tvArabic, tvTranslation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAyahNumber = itemView.findViewById(R.id.tvAyahNumber);
            tvArabic = itemView.findViewById(R.id.tvArabic);
            tvTranslation = itemView.findViewById(R.id.tvTranslation);
        }
    }
}
