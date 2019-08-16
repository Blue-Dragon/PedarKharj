package com.example.pedarkharj_edit2.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantAdaptor extends RecyclerView.Adapter<ParticipantAdaptor.ViewHolder> {
    ArrayList<Participant> participants;
    Context mContext;
    Class mClass;

    public ParticipantAdaptor(Context mContext, ArrayList<Participant> participants) {
        this.mContext = mContext;
        this.participants = participants;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profImv;
        TextView nameTv, resultTv;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            profImv = itemView.findViewById(R.id.prof_pic);
            nameTv = itemView.findViewById(R.id.partic_name);
            resultTv = itemView.findViewById(R.id.partic_result_tv);
            cardView = itemView.findViewById(R.id.card_layout);
        }

        @Override
        public void onClick(View view) {
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Participant participant = participants.get(position);

        holder.resultTv.setText(String.valueOf(participant.getResult()));
        holder.nameTv.setText(participant.getName());
        if (participant.getProfBitmap() !=null)
            holder.profImv.setImageBitmap(participant.getProfBitmap());

        holder.cardView.setOnClickListener(item -> {
            Toast.makeText(mContext, (position +1) + " clicked", Toast.LENGTH_SHORT).show();

        });

        
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }






}
