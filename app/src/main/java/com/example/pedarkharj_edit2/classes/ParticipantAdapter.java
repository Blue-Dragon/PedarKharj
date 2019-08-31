package com.example.pedarkharj_edit2.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {
    private ArrayList<Participant> participants;
    private Context mContext;
    private int mLayout;

    /*      Constructors       */
    //main page
    public ParticipantAdapter(Context mContext, ArrayList<Participant> participants) {
        this.mContext = mContext;
        this.participants = participants;
        this.mLayout = R.layout.sample_participant;
    }
    //Expense Activity
    public ParticipantAdapter(Context mContext, int mLayout, ArrayList<Participant> participants) {
        this.mContext = mContext;
        this.participants = participants;
        this.mLayout = mLayout;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profImv, subImg;
        TextView nameTv, subTxt;
        RelativeLayout baseLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            profImv = itemView.findViewById(R.id.prof_pic);
            nameTv = itemView.findViewById(R.id.partic_name);
            subTxt = itemView.findViewById(R.id.sub_txt);
            baseLayout = itemView.findViewById(R.id.base_layout);
            subImg = itemView.findViewById(R.id.sub_img);
        }

        @Override
        public void onClick(View view) {
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Participant participant = participants.get(position);

        if (participant.getName() !=null)     holder.nameTv.setText(participant.getName());
        if (participant.getProfBitmap() !=null)     holder.profImv.setImageBitmap(participant.getProfBitmap());
        if (participant.getResult() !=null && holder.subTxt != null)     holder.subTxt.setText(String.valueOf(participant.getResult()));

        holder.baseLayout.setOnClickListener(item -> {
            Toast.makeText(mContext, (position +1) + " clicked", Toast.LENGTH_SHORT).show();
        });

        if (holder.subImg != null){
            holder.baseLayout.setOnClickListener(item -> holder.subImg.setVisibility(View.VISIBLE));
            holder.profImv.setOnClickListener(item -> holder.subImg.setVisibility(View.VISIBLE));
        }

        
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }
}
