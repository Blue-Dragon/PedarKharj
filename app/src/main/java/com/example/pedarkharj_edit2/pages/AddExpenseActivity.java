package com.example.pedarkharj_edit2.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.example.pedarkharj_edit2.MainActivity;
import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Event;
import com.example.pedarkharj_edit2.classes.Expense;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.PersianDate;
import com.example.pedarkharj_edit2.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    List<Participant> mParticipants;
    List<Participant> usersListPartices;
    List<Integer> expenseDebtsList;
    ParticipantAdapter adapter;
    LinearLayout calculator;
    Event curEvent;
    RecyclerTouchListener listener;
    int recyclerChildCount, curChildCount;

    RecyclerView recyclerView;
    Context mContext;
    Activity mActivity;
    Button removeBtn, dateBtn, particBtn, doneBtn,
    bp, b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;
    CircleImageView circleImageView;
    boolean pmCanUse;
    EditText dongEText;
    RelativeLayout buyerBtn;
    PersianDate mDate;
    PersianDate todayDate;
    TextView priceTv, BuyerBtnTxt;
    Switch aSwitch;

    int particId;
    Participant buyer;
    List<Participant> users;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        //back imageView btn
        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(item -> finish());

        mContext = this;
        mActivity = this;
        pmCanUse = true;
        mParticipants = new ArrayList<>();
        usersListPartices = new ArrayList<>();
        expenseDebtsList = new ArrayList<>();
        db = new DatabaseHelper(mContext);

        particId = getIntent().getIntExtra(Routines.PARTICIPANT_INFO, 0);
        buyer = db.getParticeById(particId);
        curEvent = buyer.getEvent();
//        expenseDebtsList = new  int[]{}; //by deff


        BuyerBtnTxt = findViewById(R.id.buyer_btn_txt);
//        BuyerBtnTxt.setText(buyer.getName());
        particBtn = findViewById(R.id.custom_dong_btn);              particBtn.setOnClickListener(this);
        buyerBtn = findViewById(R.id.plus_btn);                         buyerBtn.setOnClickListener(this);
        doneBtn = findViewById(R.id.done_btn);                             buyerBtn.setOnClickListener(this);
        dateBtn = findViewById(R.id.date_btn);                             dateBtn.setOnClickListener(this);
        dongEText = findViewById(R.id.dong_Etxt);                       //dongEText.setSelection(dongEText.getText().length());
        priceTv = findViewById(R.id.price_txt);
        recyclerView = findViewById(R.id.participants_RecView);
        circleImageView = findViewById(R.id.selected_contact);     circleImageView.setOnClickListener(this);
        aSwitch = findViewById(R.id.switch1);

        //
        calculator = findViewById(R.id.calculator);                          calculator.setOnClickListener(this);
//        bp = findViewById(R.id.bp);
//        b0 = findViewById(R.id.b0);
//        b1 = findViewById(R.id.b1);
//        b2 = findViewById(R.id.b2);
//        b3 = findViewById(R.id.b3);
//        b4 = findViewById(R.id.b4);
//        b5 = findViewById(R.id.b5);
//        b6 = findViewById(R.id.b6);
//        b7 = findViewById(R.id.b7);
//        b8 = findViewById(R.id.b8);
//        b9 = findViewById(R.id.b9);
        todayDate = new PersianDate();
        dateBtn.setText(dateString(todayDate) );

        //
        if (particId != 0)  {
            dongEText.setHint("خب... " +buyer.getName() + " چی خریده؟");

            //if buyer has no pic, put a default pic
            Bitmap defPic = Routines.resizeBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.profile) ) ;
            Bitmap bitmap = buyer.getBitmapStr() != null ? Routines.decodeBase64(buyer.getBitmapStr() ) : defPic;
            circleImageView.setImageBitmap( bitmap );
        }
        //
        doRecyclerView(Routines.NOT_SELECT_ALL);

        // -----------------------------        on participant click        --------------------------------//
        /**
         * recView onClick
         */
        Log.e("recOnClick", "onClick");
        listener = new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = mParticipants.get(position);
                AppCompatImageView subImg = view.findViewById(R.id.sub_img);

                // gather expense partices when clicked (check img code is in `aSwitch.setOnClickListener` Call)
                if (subImg.getVisibility() != View.VISIBLE ) {
                    subImg.setVisibility(View.VISIBLE);
                    curChildCount++;
                    usersListPartices.add(participant);
                } else{
                    subImg.setVisibility(View.INVISIBLE);
                    curChildCount--;
                    if (usersListPartices.contains(participant)) usersListPartices.remove(participant);
                }


                aSwitch.setChecked(false);  //set switch off, when unSelect one among all
                if (curChildCount == recyclerChildCount)    aSwitch.setChecked(true); //set switch on, when all are selected one by one
            }
            @Override
            public void onLongClick(View view, int position) {}
        });


        recyclerView.addOnItemTouchListener(listener);
        /*
         * CheckAll
         */
        aSwitch.setOnClickListener(view -> {
            if (aSwitch.isChecked()) {
                doRecyclerView(Routines.SELECT_ALL);
                curChildCount = recyclerChildCount;
                //add all as users
                usersListPartices.clear();
                for (Participant participant : mParticipants){
                    usersListPartices.add(participant);
                }
            }
            else {
                doRecyclerView(Routines.UNSELECT_ALL);
                usersListPartices.clear(); //remove all as users
                curChildCount = 0;
            }
        });



        aSwitch.performClick();     //check all be def
        // db
        db.closeDB();
    }




    /********************************************       Methods     ****************************************************/

//    private void LOGPartices(List<Participant> participants) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Partices' names:\n");
//        for (Participant participant1: participants){
//            builder.append(participant1.getName()+ "\n");
//        }
//        Log.d("Fuck010", builder.toString());
//    }

    /*
     * setting diff dong, if set
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Routines.RESULT_OK) {
            if(resultCode == Activity.RESULT_OK){
                int[] intArrayExtra = data.getIntArrayExtra(Routines.RESULT);
                for (int i : intArrayExtra){
                    expenseDebtsList.add(i);
                }
            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.custom_dong_btn:
                int price = Integer.valueOf( priceTv.getText().toString());
                // expense users' ids
                int[] usersIds = new int[usersListPartices.size()];
                int j = 0;
                for (Participant participant: usersListPartices){
                    usersIds[j++] = participant.getId();
                }

                if (price > 0){
                    if (usersIds.length > 0)  {
                        Intent intent = new Intent(mContext,  DiffDongActivity.class);
                        intent.putExtra(Routines.SEND_EVENT_ID_INTENT, curEvent.getId());
                        intent.putExtra(Routines.SEND_EXPENSE_INT_INTENT, price);
                        intent.putExtra(Routines.SEND_USERS_INTENT, usersIds);
                        startActivityForResult(intent, Routines.RESULT_OK);
                    }
                    else   Toast.makeText(mContext, "لطفا افراد شرکت کننده را انتخاب کنید.", Toast.LENGTH_SHORT).show();

                } else Toast.makeText(mContext, "لطفا هزینه خرج را وارد کنید", Toast.LENGTH_SHORT).show();

                break;

            case R.id.plus_btn:
            case R.id.selected_contact:
//                new BuyerDialog(mActivity,curEvent)
                break;

            case R.id.date_btn:
                DatePicker.Builder builder = new DatePicker.Builder().theme(R.style.DialogTheme);
                mDate = new PersianDate();
                builder.date(mDate.getDay(), mDate.getMonth(), mDate.getYear());
                builder.build((id1, calendar, day, month, year) -> {
                    mDate.setDate(day, month, year);
                    //dateBtn
//                    String dateString = "";
//                    if ()
                    dateBtn.setText(dateString(mDate) );
                }).show(getSupportFragmentManager(), "");
                break;

            case R.id.done_btn:
                // expense users
//                users = new Participant[usersListPartices.size()];
//                for (int i = 0; i< usersListPartices.size(); i++){
//                    users[i] = usersListPartices.get(i);
//                }

                if (usersListPartices.size() > 0)     saveExpense();
                else                            Toast.makeText(mContext, "لطفا افراد شرکت کننده را انتخاب کنید.", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void saveExpense() {
        int price = Integer.valueOf( priceTv.getText().toString());


        if (price > 0){
            String priceTitle = dongEText.getText().toString().trim();

//            if (expenseDebtsList == null || expenseDebtsList.length < 1){
//                //same debts
//               expenseDebtsList = new int[usersListPartices.size()];
//                for (int debt: expenseDebtsList){
//                    debt = price/usersListPartices.size();
//                    Log.i("Fuck012", debt+ "");
//                 }
//
//            }

            Expense expense = new Expense();
            expense.setExpenseIdByOrder(db);
            expense.setEvent(curEvent);
            expense.setBuyer(buyer);
            expense.setUserPartics(usersListPartices);
            expense.setExpenseTitle(priceTitle);
            expense.setExpensePrice(price);
            if (expenseDebtsList == null || expenseDebtsList.size() < 1) expense.setExpenseDebts(price/usersListPartices.size());
            else expense.setExpenseDebts(expenseDebtsList);

            db.addExpense(expense);
            startActivity(new Intent(mContext,  MainActivity.class));
            finish();
        } else Toast.makeText(mContext, "لطفا هزینه خرج را وارد کنید", Toast.LENGTH_SHORT).show();


    }

    // Writes numbers when calculator layout buttons clicked (method got used in XML layout)
    public void onCalcClick(View view) {
        Button b = mActivity.findViewById(view.getId() ) ;

        StringBuilder builder = new StringBuilder();
        if (Integer.valueOf(priceTv.getText().toString()) > 0 ) builder.append(priceTv.getText());

        // Not letting user to use '.' twice
        if (b.getId() == R.id.minus_btn || b.getId() == R.id.plus_btn ){
            if (pmCanUse) {
                pmCanUse = false;
                builder.append(b.getText());
            }
        }
            //delete
        else if (b.getId() == R.id.bkSpace){
            char[] chars = priceTv.getText().toString().toCharArray();
            int i = chars.length;

            if (chars.length > 0 ){
                builder.delete(i-1, i);

                chars = builder.toString().toCharArray();
                if ( chars.length < 1 ) builder.append(0);
            }
        }
        // add number to priceTv
        else {
            builder.append(b.getText());
        }


        priceTv.setText(builder);
    }

    private void doRecyclerView(short selectMode) {
        //show partices of the Event
        mParticipants = db.getAllParticeUnderEvent(curEvent);
        recyclerChildCount = mParticipants.size();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.HORIZONTAL, false);
//        gridLayoutManager.setOrientation(gridLayoutManager.scrollHorizontallyBy(3));
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adapter = new ParticipantAdapter(mContext, R.layout.sample_contact, mParticipants);
        adapter.setSelectMode(selectMode);
        recyclerView.setAdapter(adapter);
    }

    private String dateString(PersianDate mDate) {
        String dateStringType;
        PersianDate todayDate = new PersianDate();

        if (mDate.getFullDate().equals(todayDate.getFullDate()) ){
            dateStringType =  String.format(Locale.US, "%s\n%s", "امروز", mDate.getShortDate() );
        }else {
            dateStringType =  String.format(Locale.US, "%s\n%s", mDate.getShortDate(), mDate.getmYear() );
        }
        return dateStringType;
    }




}
