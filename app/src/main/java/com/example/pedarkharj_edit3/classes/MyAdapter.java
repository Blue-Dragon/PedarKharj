package com.example.pedarkharj_edit3.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {
    final static boolean AMOUNT_MODE = true;
    final static boolean DONG_MODE = false;


    private List<Participant> participants;
    private List<Event> events;
    private List<Expense> expenseList;
    private Context mContext;
    private Activity mActivity;
    private int mLayout, maxCheckImg;
    private boolean amountModeDong;
    private short selectMode = 3;
    private int defaultDong;
    private Drawable drawable; //EventMng
    private int widthSplit = 0;
    public boolean isAmountModeDong() {
        return amountModeDong;
    }
    public void setAmountModeDong(boolean amountModeDong) {
        this.amountModeDong = amountModeDong;
    }

    //------------------------------      Constructors       ---------------------------------/
    //EventMng Activity
    public MyAdapter(Context mContext) {
        this.mContext = mContext;
        this.mLayout = R.layout.sample_event;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
    public void setLayout(int layoutId) {
        this.mLayout = layoutId;
    }
    public void setForeground(Drawable drawable) {
        this.drawable = drawable;
    }
    public void setItemsInScreen(int itemsCount) {
        this.widthSplit = itemsCount ;
    }
    //EventDetailActivity
    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    //Typical Activities
    public MyAdapter(Context mContext, int mLayout, List<Participant> participants) {
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
    public void setSelectMode(short selectMode){
        this.selectMode = selectMode;
    }
    public void setDefaultDong(int defaultDong) {
        this.defaultDong = defaultDong;
    }

    public MyAdapter(Activity mActivity, int mLayout, List<Participant> participants, boolean amountModeDong) {
        this.mActivity = mActivity;
        this.participants = participants;
        this.mLayout = mLayout;
        this.amountModeDong = amountModeDong; //if true, mode 2- amount
    }


    //------------------------------      ViewHolder innerClass       ---------------------------------/
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profImv;
        AppCompatImageView checkedImg;
        ImageView imageView;
        TextView nameTv, resultTxt;
//        RelativeLayout baseLayout;
        CardView cardView; //EventMng
        //diff dong
        Button plusBtn, minusBtn;
        TextView dongEtxt;
            //mode_02 amount
        EditText dongEtxtAmount;
        // EventDetailed- ExpenseLists
        TextView dateTv, priceTitleTv;


        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_event);
            profImv = itemView.findViewById(R.id.prof_pic);
            nameTv = itemView.findViewById(R.id.partic_name);
            resultTxt = itemView.findViewById(R.id.result_txt);
//            baseLayout = itemView.findViewById(R.id.base_layout);
            //
            checkedImg = itemView.findViewById(R.id.sub_img);
            //dong
            plusBtn = itemView.findViewById(R.id.plus_btn);
            minusBtn = itemView.findViewById(R.id.minus_btn);
            dongEtxt = itemView.findViewById(R.id.dong_Etxt2);
            dongEtxtAmount =  itemView.findViewById(R.id.dong_Etxt_amount); // dong mode_02 only
            //
            cardView = itemView.findViewById(R.id.details_card_layout); //EventMng
            // EventDetailed- ExpenseLists
            dateTv = itemView.findViewById(R.id.tv_date);
            priceTitleTv = itemView.findViewById(R.id.tv_price_title);


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
        //screen_based width
        if (widthSplit > 0){
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            int split = (parent.getMeasuredWidth() / widthSplit);
            layoutParams.width = split - split/15 ;
            layoutParams.height = (layoutParams.width) + (layoutParams.width / 3);
//        int width = parent.getMeasuredWidth() / 3;
//        view.setMinimumWidth(width);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        /*
         * EventFragment
         */
        if (events != null){
            Log.i("positionCall", "Events recyclerView Call");
            Event event = events.get(position);
            DatabaseHelper db0 = new DatabaseHelper(mContext);
            int particNumber = db0.getAllParticeUnderEvent(event).size();

            if (event.getEventName() !=null && holder.nameTv != null)      holder.nameTv.setText(event.getEventName());
/* pic */ if ( event.getBitmapStr() != null  && holder.imageView != null)       holder.imageView.setImageBitmap(Routines.decodeBase64(event.getBitmapStr()));
            if (particNumber > 0 && holder.resultTxt != null)      holder.resultTxt.setText(particNumber + " عضو");
            else Log.e("E002",  particNumber + "" );

            if (drawable != null && holder.cardView != null) holder.cardView.setForeground(drawable); //onLongClick color changing

            db0.closeDB();
        }
        // </ EventFragment >

        /*
         * EventDetailed- ExpenseLists
         */
        if (expenseList != null){
            Log.i("positionCall", "ExpenseList recyclerView Call");
            Expense expense = expenseList.get(position);
            Participant buyer = expense.getBuyer();

            if (buyer.getName() != null && holder.nameTv != null)
                holder.nameTv.setText(buyer.getName());
            if (buyer.getBitmapStr() != null && holder.profImv != null)
                holder.profImv.setImageBitmap(Routines.decodeBase64(buyer.getBitmapStr()));
            if (buyer.getResult() != null && holder.resultTxt != null)
                holder.resultTxt.setText(buyer.getResult());

            if (holder.resultTxt != null)
                holder.resultTxt.setText(String.valueOf(expense.getExpensePrice() ));
            if (holder.dateTv != null)
                holder.dateTv.setText(expense.getCreated_at());
            if (holder.priceTitleTv != null)
                holder.priceTitleTv.setText(expense.getExpenseTitle());
        }
        // </ EventDetailed- ExpenseLists >


        if (participants != null ) {
            Participant participant;

                Log.i("positionCall", "Participants recyclerView Call");
                participant = participants.get(position);

            if (participant.getName() != null && holder.nameTv != null)
                holder.nameTv.setText(participant.getName());
            if (participant.getBitmapStr() != null && holder.profImv != null)
                holder.profImv.setImageBitmap(Routines.decodeBase64(participant.getBitmapStr()));
            if (participant.getResult() != null && holder.resultTxt != null)
                holder.resultTxt.setText(participant.getResult());



            //addExpenseActivity_ amountMode
            if (defaultDong > 0 && holder.dongEtxtAmount != null)
                holder.dongEtxtAmount.setText(String.valueOf(defaultDong));

            /*
             * AddExpenseActivity_ selecting users
             */
            if (selectMode == Routines.UNSELECT_ALL){
                //unselectAll
                holder.checkedImg.setVisibility(View.INVISIBLE);
            } else if (selectMode == Routines.SELECT_ALL){
                //SelectAll
                holder.checkedImg.setVisibility(View.VISIBLE);
            } //else, keep going dude!
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


    @Override
    public int getItemCount() {
        if (participants != null)
            return participants.size();

        else if (events != null)
            return events.size();

        else if (expenseList != null)
            return expenseList.size();

        else return 0;
    }
}
