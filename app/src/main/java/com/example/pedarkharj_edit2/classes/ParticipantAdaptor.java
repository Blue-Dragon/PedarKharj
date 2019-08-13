package com.example.pedarkharj_edit2.classes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ParticipantAdaptor extends RecyclerView.Adapter<ParticipantAdaptor.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profImv;
        TextView nameTv, resultTv;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }






}
