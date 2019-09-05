package com.example.pedarkharj_edit2.classes;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<Participant> participants;
    private Context mContext;
    private Activity mActivity;
    private int mLayout, maxCheckImg;


    /*      Constructors       */
    //main page
    public ParticipantAdapter(Context mContext, ArrayList<Participant> participants) {
        this.mContext = mContext;
        this.participants = participants;
        this.mLayout = R.layout.sample_participant;
    }
    //Expense/ Contacts Activity
    public ParticipantAdapter(Context mContext, int mLayout, ArrayList<Participant> participants) {
        this.mContext = mContext;
        this.participants = participants;
        this.mLayout = mLayout;
        this.maxCheckImg = participants.size();
    }
    //BuyerDialog Activity
    public ParticipantAdapter(Context mContext, int mLayout, ArrayList<Participant> participants, int maxCheckImg) {
        this.mContext = mContext;
        this.participants = participants;
        this.mLayout = mLayout;
        this.maxCheckImg = maxCheckImg;
    }
    //DiffDong Activity
    public ParticipantAdapter(Activity mActivity, int mLayout, ArrayList<Participant> participants) {
        this.mActivity = mActivity;
        this.participants = participants;
        this.mLayout = mLayout;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profImv, checkImg;
        TextView nameTv, resultTxt;
        RelativeLayout baseLayout;
        //diff dong
        Button plusBtn, minusBtn;
        EditText dongEtxt;

        ViewHolder(View itemView) {
            super(itemView);
            profImv = itemView.findViewById(R.id.prof_pic);
            nameTv = itemView.findViewById(R.id.partic_name);
            resultTxt = itemView.findViewById(R.id.result_txt);
            baseLayout = itemView.findViewById(R.id.base_layout);
            //
            checkImg = itemView.findViewById(R.id.sub_img);
            //
            plusBtn = itemView.findViewById(R.id.plus_btn);
            minusBtn = itemView.findViewById(R.id.minus_btn);
            dongEtxt = itemView.findViewById(R.id.dong_Etxt2);

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
        int curDongNum = 0;

        if (participant.getName() !=null && holder.nameTv != null)      holder.nameTv.setText(participant.getName());
        if (participant.getProfBitmap() !=null && holder.profImv != null)    holder.profImv.setImageBitmap(participant.getProfBitmap());
        if (participant.getResult() !=null && holder.resultTxt != null)    holder.resultTxt.setText(String.valueOf(participant.getResult()));

        holder.baseLayout.setOnClickListener(this);
        checkAsRadioBtn(holder);

        //DiffDong (mActivity)
        curDongNum = participant.getDongNumber();
        if (curDongNum >= 0 && holder.dongEtxt != null)     holder.dongEtxt.setText(String.valueOf(curDongNum));


        if (holder.plusBtn != null) {
            holder.plusBtn.setOnClickListener(view -> {
                int mCurNumber = Integer.valueOf(holder.dongEtxt.getText().toString());
                holder.dongEtxt.setText(String.valueOf(++mCurNumber));
                Toast.makeText(mActivity, "CurrDong: "+ holder.dongEtxt.getText().toString(), Toast.LENGTH_SHORT).show();
            });
        }


        if (holder.minusBtn != null){
            holder.minusBtn.setOnClickListener(item-> {
                int mCurNumber = Integer.valueOf(holder.dongEtxt.getText().toString());
                holder.dongEtxt.setText(String.valueOf(--mCurNumber));
                Toast.makeText(mActivity, "CurrDong: "+ holder.dongEtxt.getText().toString(), Toast.LENGTH_SHORT).show();
            });
        }



    }



    /**************************************       Methods       **************************************/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_layout:
                if (mContext != null)
                    Toast.makeText(mContext, " max CheckImg: " + maxCheckImg, Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void checkAsRadioBtn(@NonNull ViewHolder holder) {
        if (holder.checkImg != null){
            //TODO: change it to radio button action
            if (holder.checkImg.getVisibility() != View.VISIBLE){
                holder.baseLayout.setOnClickListener(item -> holder.checkImg.setVisibility(View.VISIBLE));
                holder.profImv.setOnClickListener(item -> holder.checkImg.setVisibility(View.VISIBLE));
            }
            else if (holder.checkImg.getVisibility() == View.VISIBLE) {
                holder.baseLayout.setOnClickListener(item -> holder.checkImg.setVisibility(View.INVISIBLE));
                holder.profImv.setOnClickListener(item -> holder.checkImg.setVisibility(View.INVISIBLE));
            }
        }
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }
}