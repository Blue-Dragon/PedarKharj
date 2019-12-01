package com.example.pedarkharj_edit2.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> implements View.OnClickListener {
    final static boolean AMOUNT_MODE = true;
    final static boolean DONG_MODE = false;

    private List<Participant> participants;
    private List<Event> events;
    private Context mContext;
    private Activity mActivity;
    private int mLayout, maxCheckImg;
    private boolean amountModeDong;

    public boolean isAmountModeDong() {
        return amountModeDong;
    }
    public void setAmountModeDong(boolean amountModeDong) {
        this.amountModeDong = amountModeDong;
    }

    //------------------------------      Constructors       ---------------------------------/
    //main page
//    public ParticipantAdapter(Context mContext , List<Participant> participants) {
//        this.mContext = mContext;
//        this.participants = participants;
//        this.mLayout = R.layout.sample_participant;
//    }

    //EventMng Activity
    public ParticipantAdapter(Context mContext) {
        this.mContext = mContext;
        this.mLayout = R.layout.sample_event;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
    public void setLayout(int layoutId) {
        this.mLayout = layoutId;
    }

    //Typical Activities
    public ParticipantAdapter(Context mContext, int mLayout, List<Participant> participants) {
        this.mContext = mContext;
        this.participants = participants;
        this.mLayout = mLayout;
    }
    public void setMaxCheckImg(int maxCheckImg) {
        this.maxCheckImg = maxCheckImg;
    }
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public ParticipantAdapter(Activity mActivity, int mLayout, List<Participant> participants, boolean amountModeDong) {
        this.mActivity = mActivity;
        this.participants = participants;
        this.mLayout = mLayout;
        this.amountModeDong = amountModeDong; //if true, mode 2- amount
    }


    //------------------------------      ViewHolder innerClass       ---------------------------------/
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profImv, checkImg;
        ImageView imageView;
        TextView nameTv, resultTxt;
        RelativeLayout baseLayout;
        //diff dong
        Button plusBtn, minusBtn;
        EditText dongEtxt;
            //mode_02 amount
        EditText dongEtxtAmount;


        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_event);
            profImv = itemView.findViewById(R.id.prof_pic);
            nameTv = itemView.findViewById(R.id.partic_name);
            resultTxt = itemView.findViewById(R.id.result_txt);
            baseLayout = itemView.findViewById(R.id.base_layout);
            //
            checkImg = itemView.findViewById(R.id.sub_img);
            //dong
            plusBtn = itemView.findViewById(R.id.plus_btn);
            minusBtn = itemView.findViewById(R.id.minus_btn);
            dongEtxt = itemView.findViewById(R.id.dong_Etxt2);
            // dong mode_02 only
            dongEtxtAmount =  itemView.findViewById(R.id.dong_Etxt_amount);

        }

        @Override
        public void onClick(View view) {
        }
    }


    //------------------------------      Interface Methods       ---------------------------------/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        /**
         * EventMngActivity
         */
//        Log.e("E002",  String.valueOf(events) );
        if (events != null){
            Event event = events.get(position);
            DatabaseHelper db0 = new DatabaseHelper(mContext);
            int particNumber = db0.getAllParticeUnderEvent(event).size();

            if (event.getEventName() !=null && holder.nameTv != null)      holder.nameTv.setText(event.getEventName());
            /* pic */ if ( holder.imageView != null && event.getBitmapStr() != null )       holder.profImv.setImageBitmap(Routines.decodeBase64(event.getBitmapStr()));
            if (particNumber > -1 && holder.resultTxt != null)      holder.resultTxt.setText(particNumber + " عضو");
            else Log.e("E002",  particNumber + "" );
            db0.closeDB();
        }
        // </ EventMngActivity >

        if (participants != null) {
            Participant participant = participants.get(position);

            if (participant.getName() != null && holder.nameTv != null)
                holder.nameTv.setText(participant.getName());
            if (participant.getBitmapStr() != null && holder.profImv != null)
                holder.profImv.setImageBitmap(Routines.decodeBase64(participant.getBitmapStr()));
            if (participant.getResult() != null && holder.resultTxt != null)
                holder.resultTxt.setText(String.valueOf(participant.getResult()));


            holder.baseLayout.setOnClickListener(item -> {
                DatabaseHelper db = new DatabaseHelper(mActivity);
                Intent intent = new Intent();
                db.closeDB();
            });


            holder.baseLayout.setOnClickListener(this);
            checkAsRadioBtn(holder);

            //DiffDong (mActivity)
            //mode_01 numeric (if false)
            if (!amountModeDong) {
//            int curDongNum = participant.getDongNumber(); todo: ckeck if I haven't screwed this code
                int curDongNum = 1;
                if (curDongNum >= 0 && holder.dongEtxt != null)
                    holder.dongEtxt.setText(String.valueOf(curDongNum));

                if (holder.plusBtn != null) {
                    holder.plusBtn.setOnClickListener(view -> {
                        int mCurNumber = Integer.valueOf(holder.dongEtxt.getText().toString());
                        holder.dongEtxt.setText(String.valueOf(++mCurNumber));
                    });
                }

                if (holder.minusBtn != null) {
                    holder.minusBtn.setOnClickListener(item -> {
                        int mCurNumber = Integer.valueOf(holder.dongEtxt.getText().toString());
                        holder.dongEtxt.setText(String.valueOf(--mCurNumber));
                    });
                }
            }
            //mode_02 amount (if true)
            else {
                float curDongAmount = participant.getDebt();
                if (curDongAmount >= 0 && holder.dongEtxtAmount != null)
                    holder.dongEtxtAmount.setText(String.valueOf(curDongAmount));
            }

        }



    }



    /**************************************      other Methods       **************************************/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_layout:
//                if (mContext != null)
//                    Toast.makeText(mContext, " max CheckImg: " + maxCheckImg, Toast.LENGTH_SHORT).show();
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

//            holder.baseLayout.setOnClickListener(item -> {
//                DatabaseHelper db = new DatabaseHelper(mActivity);
//                Participant participant = db.getParticeById(holder.nameTv)
//
//                        db.closeDB();
//            });

        }
    }

    @Override
    public int getItemCount() {
        if (participants != null)
            return participants.size();
        else if (events != null)
            return events.size();
        else return 0;
    }
}
