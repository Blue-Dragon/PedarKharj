package com.example.pedarkharj_edit3.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiffDongActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final static boolean CASH_MODE = true;
    final static boolean DONG_MODE = false;

    RecyclerView recyclerView;
    List<Participant> usersList;
    Map<Integer, Float> usersDongMap;
    DatabaseHelper db;
    MyAdapter adaptor;
    Context mContext = this;
    Activity mActivity = this;
    Event curEvent;

    int[] usersIds;
    float userDong;
    boolean layoutMode;
    float defDongAmount;
    int defDongNumber;
    int    dongsNumber;
    float eachDongAmount,
            expense,
            countedExpenses;

    FloatingActionButton fab;
    Spinner spinner;
    // the rectangle above
    TextView tvR1, tvR2, tvC1, tvC2, tvL1, tvL2;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_dong);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        inits();
        onClicks();


        //Tutorial - TabTargetView
        if ( SharedPrefManager.getInstance(mActivity).getRunTurn(Routines.KEY_TURN_TIME_EXPENSE_DIFF) == Routines.FIRST_RUN ){
            new Handler().postDelayed(this::showTabTargetsSequences1, 500);   // Delay 0.5 sec
        }

        // recView onClick
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant user = usersList.get(position);
                Button plus = view.findViewById(R.id.plus_btn);
                Button minus = view.findViewById(R.id.minus_btn);
                TextView tv = view.findViewById(R.id.dong_Etxt2);
                EditText editText = view.findViewById(R.id.dong_Etxt_amount);

                if (layoutMode == DONG_MODE){
                    userDong = Integer.valueOf(tv.getText().toString());

                    plus.setOnClickListener(item -> {
                        tv.setText(Routines.getRoundFloatString(++userDong));
                        tvC2.setText(Routines.getRoundFloatString(++dongsNumber));
                        doDongStuff(user, userDong, dongsNumber);
//                    Log.i("fuck013", "id " +user.getId()+ ": " +userDong + "");
                    });
                    minus.setOnClickListener(item -> {
                        if (userDong > 1){
                            tv.setText(Routines.getRoundFloatString(--userDong));
                            tvC2.setText(Routines.getRoundFloatString(--dongsNumber));
                            doDongStuff(user, userDong, dongsNumber);
//                        Log.i("fuck013", "id " +user.getId()+ ": " +userDong + "");

                        } else
                            Toast.makeText(mContext, "دونگ نمی تواند کمتر از یک سهم باشد.", Toast.LENGTH_SHORT).show();
                    });
                    //

//                editText.addTextChangedListener(new TextWatcher() {
//                    String before ;
//                    int beforeInt;
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        before = charSequence.toString().trim();
//                        beforeInt = Integer.valueOf(before);
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                        if (Integer.valueOf(charSequence.toString()) < 1 ){
////                            Toast.makeText(mContext, "دونگ نمی تواند کمتر از یک سهم باشد.", Toast.LENGTH_SHORT).show();
////                        } editText.setText(before);
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//                        int i = Integer.valueOf(editable.toString()) ;
//                        userDong = i;
//                        dongsNumber += (i - beforeInt);
////                        editText.setText(String.valueOf(i));
//                        doDongStuff(user, userDong, dongsNumber);
//                    }
//                });
                }
                else {
                    //in Amount Mode
                    editText.addTextChangedListener(new TextWatcher() {
                        float cur;

                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
//                            int countedExpenses = 0;
                            cur = !editable.toString().equals("") ? Float.valueOf(editable.toString()) : 0 ;

                            Log.i("fuck016",  "expense: " + expense);

                            doDongStuff(user, cur, dongsNumber);
                            countedExpenses = reGetCountedExpenses();
                            tvR2.setText(String.valueOf(expense - countedExpenses));
                            Log.i("fuck016",  "remaining: " + (expense - countedExpenses) );

                        }
                    });
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // the rectangle above
        initRectangleAbove();

        // Floating Btn
        fab.setOnClickListener(view -> {
            countedExpenses = reGetCountedExpenses();
            if (layoutMode == CASH_MODE && (expense - countedExpenses) != 0){
                Toast.makeText(mContext, "مبلغ ها صحیح نیستند! باقی مانده باید صفر باشد.", Toast.LENGTH_SHORT).show();
                return;
            }

            float dongAmountUnit = Float.valueOf(tvR2.getText().toString().trim());
            dongAmountUnit = Routines.getRoundFloat(dongAmountUnit);
            float[] expenseDongs = new float[usersList.size()];

            int i = 0;
            float userDong;
            Log.i("fuck014", ". \n\n" );

            for (Participant user : usersList){
                userDong = usersDongMap.get(user.getId());
                if (layoutMode == DONG_MODE)
                    expenseDongs[i++] = userDong * dongAmountUnit;
                else
                    expenseDongs[i++] =userDong;
            }

//            Toast.makeText(mContext, ""+( expense - countedExpenses), Toast.LENGTH_SHORT).show();



            Intent intent = new Intent(mContext, AddExpenseActivity.class);
            intent.putExtra(Routines.RESULT, expenseDongs);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }





    /********************************************       Methods     ****************************************************/
    private void inits() {
        usersList = new ArrayList<Participant>();
        usersDongMap = new HashMap<Integer, Float>();
        db = new DatabaseHelper(mContext);
        curEvent = db.getEventById( getIntent().getIntExtra(Routines.SEND_EVENT_ID_INTENT, 1) );
        layoutMode = DONG_MODE; //by default
//        defDongAmount = 1;//by default
        defDongNumber = 1;//by default

        expense = getIntent().getFloatExtra(Routines.SEND_EXPENSE_FLOAT_INTENT, 0);
        countedExpenses = 0; //By def; we'll set it after Spinner to count the exact thing
        usersIds = getIntent().getIntArrayExtra(Routines.SEND_USERS_INTENT);
        dongsNumber = usersIds.length;
        eachDongAmount = Routines.getRoundFloat(expense/dongsNumber);

        //the rectangle above
        tvR1 = findViewById(R.id. tv_title_my_expense);
        tvR2 = findViewById(R.id.tv_my_expense );
        tvC1 = findViewById(R.id. tv_title_my_dong);
        tvC2 = findViewById(R.id. tv_my_dong);
        tvL1 = findViewById(R.id. tv_title_my_result);
        tvL2 = findViewById(R.id. tv_my_result);
        tvL1.setTextColor(getResources().getColor(R.color.grayTextColor));
        tvL2.setTextColor(getResources().getColor(R.color.grayTextColor));
        tvR1.setTextColor(getResources().getColor(R.color.primaryTextColor));
        tvR2.setTextColor(getResources().getColor(R.color.primaryTextColor));

        //back imageView btn
        backBtn = findViewById(R.id.back_btn);
        fab = this.findViewById(R.id.fab);

        //recyclerView_horizental
        recyclerView = findViewById(R.id.diff_dong_recView);
        /*
         * Spinner
         */
        spinner = findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("تعداد دنگ");
        list.add("مقداد دنگ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void onClicks() {
        backBtn.setOnClickListener(item -> onBackPressed());
        spinner.setOnItemSelectedListener(this);
    }

    private float reGetCountedExpenses() {
        float countedExpenses = 0;
        for (Participant user : usersList) {
            countedExpenses += usersDongMap.get(user.getId());
        }
        return countedExpenses;
    }

    void  doDongStuff(Participant user, float userDong, float allDongsNum){
        /*
         * Map: to save each dongNumber
         * todo: update -> replace Map with SparseIntArray (for less memory)
         */
        usersDongMap.put(user.getId(), userDong);
        Log.i("fuck015", "usersDongMap id: "+ user.getId()+ " : "+ userDong);
        //
        if (layoutMode == DONG_MODE)    {
            eachDongAmount = Routines.getRoundFloat(expense/allDongsNum);
            tvR2.setText(Routines.getRoundFloatString(eachDongAmount));
        }
    }

    private void doRecyclerView(boolean mode) {
        usersList.clear();
        for (int i : usersIds){
            usersList.add(db.getParticeById(i));
            usersDongMap.put(i, defDongAmount);
        }
        //
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //dongNumber mode
        if (mode == DONG_MODE) {
            adaptor = new MyAdapter(mActivity, R.layout.sample_diff_dong__dong_mode, usersList);
        }
        else {
            adaptor = new MyAdapter(mActivity, R.layout.sample_diff_dong__cash_mode, usersList);
            adaptor.setDefaultDongAmount(Routines.getRoundFloat(defDongAmount));
        }
        recyclerView.setAdapter(adaptor);
    }

    //--------------------------    spinner    --------------------------//
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedIten = parent.getItemAtPosition(position).toString();
        dongsNumber = usersIds.length;
        eachDongAmount = expense/dongsNumber;

        if (selectedIten.equals("تعداد دنگ")){
            tvC2.setText(String.valueOf(dongsNumber));
            defDongAmount = 1;
            layoutMode = DONG_MODE;
            doRecyclerView(DONG_MODE);
        }else{
            defDongAmount = eachDongAmount; //change diffDong to cash
            layoutMode = CASH_MODE;
            doRecyclerView(CASH_MODE);

            countedExpenses = reGetCountedExpenses();
//            Log.i("fuck018", "1- expense: " + expense + " \n"+ " countedExpenses: "+ countedExpenses);
        }

        initRectangleAbove();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void initRectangleAbove() {
        tvL1.setText("مبلغ خرج");
        tvL2.setText(Routines.getRoundFloatString(expense));

        if (layoutMode == DONG_MODE){
            tvC1.setText("تعداد دنگ ها");
            tvC2.setText(String.valueOf( dongsNumber));

            tvR1.setText("قیمت هر دنگ");
            tvR2.setText(Routines.getRoundFloatString(eachDongAmount));

        }else {
            tvC1.setText("قیمت هر دنگ");
            tvC2.setText(Routines.getRoundFloatString(eachDongAmount));

            tvR1.setText("هزینه باقی مانده");
            float theRest = Routines.getRoundFloat(expense - countedExpenses);
            tvR2.setText(Routines.getRoundFloatString(theRest));
        }
    }


    /**
     * first time tutorial (tapTarget)
     */
    public void showTabTargetsSequences1() {
// 1

        new TapTargetSequence(mActivity)
                // 2
                .targets(

                        TapTarget.forView(spinner, getString(R.string.diffDongSpinner_title), getString(R.string.diffDongSpinner_description))
                                .outerCircleColor(R.color.colorPrimaryDark)
                                .outerCircleAlpha(Routines.tapAlpha)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(Routines.tapTitleSize)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(Routines.tapDescSize)
                                .descriptionTextColor(R.color.bk1)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(false)
                                .transparentTarget(true)
                                .targetRadius(60)

                )

                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        SharedPrefManager.getInstance(mActivity).setNextRunTurn(Routines.KEY_TURN_TIME_EXPENSE_DIFF, Routines.SECOND_RUN);
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                    }

                })

                // 6
                .start();

    }

}
