package com.example.pedarkharj_edit3.pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.PersianDate;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddExpenseActivity extends AppCompatActivity  implements View.OnClickListener, View.OnTouchListener{
    /*
     * Calculator
     */
    private boolean dotUsed = false;
    private boolean equalClicked = false;
    private String lastExpression = "";

    private final static int EXCEPTION = -1;
    private final static int IS_NUMBER = 0;
    private final static int IS_OPERAND = 1;
    private final static int IS_OPEN_PARENTHESIS = 2;
    private final static int IS_CLOSE_PARENTHESIS = 3;
    private final static int IS_DOT = 4;

    Button buttonNumber0;
    Button buttonNumber1;
    Button buttonNumber2;
    Button buttonNumber3;
    Button buttonNumber4;
    Button buttonNumber5;
    Button buttonNumber6;
    Button buttonNumber7;
    Button buttonNumber8;
    Button buttonNumber9;

    Button buttonBkSpace;
    Button buttonSubtraction;
    Button buttonAddition;
    Button buttonDone;
    Button buttonDot;

    TextView textViewInputNumbers;
    ScriptEngine scriptEngine;

    /*
     * others
     */
    List<Participant> allParticipants;
    List<Participant> selectedListPartices;
    List<Integer> expenseDebtsList;
//    List<Participant> users;
    MyAdapter adapter;
    LinearLayout calculatorAboveBox;
    Event curEvent;
    RecyclerTouchListener listener;
    int recyclerChildCount, curChildCount;

    RecyclerView recyclerView;
    Context mContext;
    Activity mActivity;
    Button dateBtn, diffDong, doneBtn;

    CircleImageView circleImageView;
    EditText dongEText;
    PersianDate mDate;
    PersianDate todayDate;
    Switch aSwitch;

    int particId;
    Participant buyer;
    DatabaseHelper db;


    // -----------------------------       OnCreate        --------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        scriptEngine = new ScriptEngineManager().getEngineByName("rhino");
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);


        initializeViewVariables();
        setOnClickListeners();
        setOnTouchListener();
        //
        doRecyclerView(Routines.NOT_SELECT_ALL);

        // -----------   on participant click  -------------//
        /*
         * recView onClick
         */
        Log.e("recOnClick", "onClick");
        listener = new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = allParticipants.get(position);
                AppCompatImageView subImg = view.findViewById(R.id.sub_img);

                // gather expense partices when clicked (check img code is in `aSwitch.setOnClickListener` Call)
                if (subImg.getVisibility() != View.VISIBLE ) {
                    subImg.setVisibility(View.VISIBLE);
                    curChildCount++;
                    selectedListPartices.add(participant);
                } else{
                    subImg.setVisibility(View.INVISIBLE);
                    curChildCount--;
                    if (selectedListPartices.contains(participant)) selectedListPartices.remove(participant);
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
                selectedListPartices.clear();
                for (Participant participant : allParticipants){
                    selectedListPartices.add(participant);
                }
            }
            else {
                doRecyclerView(Routines.UNSELECT_ALL);
                selectedListPartices.clear(); //remove all as users
                curChildCount = 0;
            }
        });



        aSwitch.performClick();     //check all be def
        // db
        db.closeDB();
    }




    /********************************************       Methods     ****************************************************/

//    private void LOGPartices(List<Participant> allContactsTo_participants) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Partices' names:\n");
//        for (Participant participant1: allContactsTo_participants){
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



    private void saveExpense() {
        float f =  Float.valueOf( textViewInputNumbers.getText().toString());
        int price = Math.round(f);


        if (price > 0){
            String priceTitle = dongEText.getText().toString().trim();

            //we will need all users later, so we set debt = 0 fot non users here:

            Expense expense = new Expense();
            expense.setExpenseIdByOrder(db);
            expense.setEvent(curEvent);
            expense.setBuyer(buyer);
            expense.setUserPartics(selectedListPartices);
            expense.setExpenseTitle(priceTitle);
            expense.setExpensePrice(price);

            if (expenseDebtsList == null || expenseDebtsList.size() < 1)
                expense.setExpenseDebts(price/ selectedListPartices.size());
            else expense.setExpenseDebts(expenseDebtsList);

            db.addExpense(expense);
            startActivity(new Intent(mContext,  MainActivity.class));
            finish();
        } else Toast.makeText(mContext, "لطفا هزینه خرج را وارد کنید", Toast.LENGTH_SHORT).show();


    }

    //CALCULATOR
    // Writes numbers when calculatorAboveBox layout buttons clicked (method got used in XML layout)
    public void onCalcClick(View view) {
//        Button b = mActivity.findViewById(view.getId() ) ;
//
//        StringBuilder builder = new StringBuilder();
//        if (Integer.valueOf(textViewInputNumbers.getText().toString()) > 0 ) builder.append(textViewInputNumbers.getText());
//
//         Not letting user to use '.' twice
//        if (b.getId() == R.id.minus_btn || b.getId() == R.id.plus_btn ){
//            if (pmCanUse) {
//                pmCanUse = false;
//                builder.append(b.getText());
//            }
//        }
//            delete
//        else if (b.getId() == R.id.bkSpace){
//            char[] chars = textViewInputNumbers.getText().toString().toCharArray();
//            int i = chars.length;
//
//            if (chars.length > 0 ){
//                builder.delete(i-1, i);
//
//                chars = builder.toString().toCharArray();
//                if ( chars.length < 1 ) builder.append(0);
//            }
//        }
//         add number to textViewInputNumbers
//        else {
//            builder.append(b.getText());
//        }
//
//
//        textViewInputNumbers.setText(builder);
    }


    /*
     * RecyclerView
     */
    private void doRecyclerView(short selectMode) {
        //show partices of the Event
        allParticipants = db.getAllParticeUnderEvent(curEvent);
        recyclerChildCount = allParticipants.size();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4, GridLayoutManager.HORIZONTAL, false);
//        gridLayoutManager.setOrientation(gridLayoutManager.scrollHorizontallyBy(3));
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adapter = new MyAdapter(mContext, R.layout.sample_contact, allParticipants);
        adapter.setSelectMode(selectMode);
        recyclerView.setAdapter(adapter);
    }

    /*
     * Date / Calender
     */
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


    //-------------------------------------------------------  CALCULATOR  ----------------------------------------------------------//

    private void initializeViewVariables(){
        mContext = this;
        mActivity = this;
        allParticipants = new ArrayList<>();
        selectedListPartices = new ArrayList<>();
        expenseDebtsList = new ArrayList<>();
        db = new DatabaseHelper(mContext);
        todayDate = new PersianDate();

        dongEText = findViewById(R.id.dong_Etxt);
        recyclerView = findViewById(R.id.participants_RecView);
        circleImageView = findViewById(R.id.selected_contact);
        aSwitch = findViewById(R.id.switch1);
        dateBtn = findViewById(R.id.date_btn);
        diffDong = findViewById(R.id.custom_dong_btn);
        calculatorAboveBox = findViewById(R.id.calculator);
        textViewInputNumbers = findViewById(R.id.price_txt);

        buttonBkSpace = findViewById(R.id.bkSpace);
        buttonSubtraction = findViewById(R.id.minus_btn);
        buttonAddition = (Button) findViewById(R.id.plus_btn);
        buttonDone = (Button) findViewById(R.id.done_btn);
        buttonDot = (Button) findViewById(R.id.button_dot);

        buttonNumber0 = (Button) findViewById(R.id.b0);
        buttonNumber1 = (Button) findViewById(R.id.b1);
        buttonNumber2 = (Button) findViewById(R.id.b2);
        buttonNumber3 = (Button) findViewById(R.id.b3);
        buttonNumber4 = (Button) findViewById(R.id.b4);
        buttonNumber5 = (Button) findViewById(R.id.b5);
        buttonNumber6 = (Button) findViewById(R.id.b6);
        buttonNumber7 = (Button) findViewById(R.id.b7);
        buttonNumber8 = (Button) findViewById(R.id.b8);
        buttonNumber9 = (Button) findViewById(R.id.b9);
        //
        dateBtn.setText(dateString(todayDate));
        particId = getIntent().getIntExtra(Routines.PARTICIPANT_INFO, 0);
        buyer = db.getParticeById(particId);
        curEvent = buyer.getEvent();
//        BuyerBtnTxt.setText(buyer.getName());
        //
        if (particId != 0)  {
            dongEText.setHint("خب... " +buyer.getName() + " چی خریده؟");
            //if buyer has no pic, put a default pic
//            Bitmap defPic = Routines.resizeBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.profile) ) ;
            Bitmap defPic = Routines.convertBitmapThumbnail(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.profile) ) ;
            Bitmap bitmap = buyer.getBitmapStr() != null ? Routines.stringToBitmap(buyer.getBitmapStr() ) : defPic;
            circleImageView.setImageBitmap( bitmap );
        }
    }

    private void setOnClickListeners(){
        calculatorAboveBox.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        diffDong.setOnClickListener(this);

        buttonNumber0.setOnClickListener(this);
        buttonNumber1.setOnClickListener(this);
        buttonNumber2.setOnClickListener(this);
        buttonNumber3.setOnClickListener(this);
        buttonNumber4.setOnClickListener(this);
        buttonNumber5.setOnClickListener(this);
        buttonNumber6.setOnClickListener(this);
        buttonNumber7.setOnClickListener(this);
        buttonNumber8.setOnClickListener(this);
        buttonNumber9.setOnClickListener(this);

        buttonBkSpace.setOnClickListener(this);
        buttonSubtraction.setOnClickListener(this);
        buttonAddition.setOnClickListener(this);
        buttonDone.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouchListener()
    {
        buttonNumber0.setOnTouchListener(this);
        buttonNumber1.setOnTouchListener(this);
        buttonNumber2.setOnTouchListener(this);
        buttonNumber3.setOnTouchListener(this);
        buttonNumber4.setOnTouchListener(this);
        buttonNumber5.setOnTouchListener(this);
        buttonNumber6.setOnTouchListener(this);
        buttonNumber7.setOnTouchListener(this);
        buttonNumber8.setOnTouchListener(this);
        buttonNumber9.setOnTouchListener(this);

        buttonBkSpace.setOnTouchListener(this);
        buttonSubtraction.setOnTouchListener(this);
        buttonAddition.setOnTouchListener(this);
        buttonDot.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.custom_dong_btn:
                int price = Integer.valueOf( textViewInputNumbers.getText().toString());
                // expense users' ids
                int[] usersIds = new int[selectedListPartices.size()];
                int j = 0;
                for (Participant participant: selectedListPartices){
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


            case R.id.b0:
                if (addNumber("0")) equalClicked = false;
                break;
            case R.id.b1:
                if (addNumber("1")) equalClicked = false;
                break;
            case R.id.b2:
                if (addNumber("2")) equalClicked = false;
                break;
            case R.id.b3:
                if (addNumber("3")) equalClicked = false;
                break;
            case R.id.b4:
                if (addNumber("4")) equalClicked = false;
                break;
            case R.id.b5:
                if (addNumber("5")) equalClicked = false;
                break;
            case R.id.b6:
                if (addNumber("6")) equalClicked = false;
                break;
            case R.id.b7:
                if (addNumber("7")) equalClicked = false;
                break;
            case R.id.b8:
                if (addNumber("8")) equalClicked = false;
                break;
            case R.id.b9:
                if (addNumber("9")) equalClicked = false;
                break;
            case R.id.plus_btn:
                if (addOperand("+")) equalClicked = false;
                break;
            case R.id.minus_btn:
                if (addOperand("-")) equalClicked = false;
                break;
            case R.id.button_dot:
                if (addDot()) equalClicked = false;
                break;
            case R.id.bkSpace:
                StringBuilder builder0 = new StringBuilder();
                builder0.append(textViewInputNumbers.getText().toString());
                if (builder0.length() > 1){
                    builder0.deleteCharAt(builder0.length()-1);
                }
                else builder0.replace(0, 1, "0");
                textViewInputNumbers.setText(builder0.toString());
                dotUsed = false;
                equalClicked = false;
                break;

            case R.id.done_btn:

                //Equal Action
                String s = textViewInputNumbers.getText().toString();
                if (! isReadyToUse(s))
                    calculate(s);

                //Done Action
                    //now we have a straig
                else  {
                    //check price
                    if (Float.valueOf(s) <= 0){
                        Toast.makeText(mContext, "قیمت اشتباه!", Toast.LENGTH_SHORT).show();
                    }else {
                        //check participants
                        if (selectedListPartices.size() > 0)
                            saveExpense();
                        else
                            Toast.makeText(mContext, "لطفا افراد شرکت کننده را انتخاب کنید.", Toast.LENGTH_SHORT).show();
                    }


                }

                break;
        }
    }

    private boolean isReadyToUse(String string) {
        if (string.equals(""))
            return false;

        String s = string.substring(1);
//        Toast.makeText(mContext, ""+ s, Toast.LENGTH_SHORT).show();
        return  !string.contains("+") && !s.contains("-") && defineLastCharacter(string) != IS_DOT;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
//        switch (motionEvent.getAction()){
//
//            case MotionEvent.ACTION_DOWN:
//                view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
//                view.invalidate();
//                break;
//
//            case MotionEvent.ACTION_UP:
//                view.getBackground().clearColorFilter();
//                view.invalidate();
//                break;
//
//        }
        return false;
    }

    private boolean addDot(){
        boolean done = false;

        if (textViewInputNumbers.getText().length() == 0){
            textViewInputNumbers.setText("0.");
            dotUsed = true;
            done = true;
        }
        else if (dotUsed == true){
            // do nothing
        }
        else if (defineLastCharacter(textViewInputNumbers.getText().charAt(textViewInputNumbers.getText().length() - 1) + "") == IS_OPERAND){
            textViewInputNumbers.setText(textViewInputNumbers.getText() + "0.");
            done = true;
            dotUsed = true;
        }
        else if (defineLastCharacter(textViewInputNumbers.getText().charAt(textViewInputNumbers.getText().length() - 1) + "") == IS_NUMBER){
            textViewInputNumbers.setText(textViewInputNumbers.getText() + ".");
            done = true;
            dotUsed = true;
        }
        return done;
    }

    private boolean addOperand(String operand){
        boolean done = false;
        int operationLength = textViewInputNumbers.getText().length();
        if (operationLength > 0)
        {
            String lastInput = textViewInputNumbers.getText().charAt(operationLength - 1) + "";

            if ((lastInput.equals("+") || lastInput.equals("-") || lastInput.equals("*") || lastInput.equals("\u00F7") || lastInput.equals("%")))
            {
//                Toast.makeText(getApplicationContext(), "فرمت اشتباه", Toast.LENGTH_LONG).show();
            } else if (operand.equals("%") && defineLastCharacter(lastInput) == IS_NUMBER)
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + operand);
                dotUsed = false;
                equalClicked = false;
                lastExpression = "";
                done = true;
            } else if (!operand.equals("%"))
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + operand);
                dotUsed = false;
                equalClicked = false;
                lastExpression = "";
                done = true;
            }
        } else
        {
            Toast.makeText(getApplicationContext(), "Wrong Format. Operand Without any numbers?", Toast.LENGTH_LONG).show();
        }
        return done;
    }

    private boolean addNumber(String number)
    {
        boolean done = false;
        int operationLength = textViewInputNumbers.getText().length();
        if (operationLength > 0)
        {
            String lastCharacter = textViewInputNumbers.getText().charAt(operationLength - 1) + "";
            int lastCharacterState = defineLastCharacter(lastCharacter);

            if (operationLength == 1 && lastCharacterState == IS_NUMBER && lastCharacter.equals("0"))
            {
                textViewInputNumbers.setText(number);
                done = true;
            } else if (lastCharacterState == IS_OPEN_PARENTHESIS)
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + number);
                done = true;
            } else if (lastCharacterState == IS_CLOSE_PARENTHESIS || lastCharacter.equals("%"))
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + "x" + number);
                done = true;
            } else if (lastCharacterState == IS_NUMBER || lastCharacterState == IS_OPERAND || lastCharacterState == IS_DOT)
            {
                textViewInputNumbers.setText(textViewInputNumbers.getText() + number);
                done = true;
            }
        } else
        {
            textViewInputNumbers.setText(textViewInputNumbers.getText() + number);
            done = true;
        }
        return done;
    }


    private void calculate(String input){
        String result = input;
        String lastChar = result.substring(result.length()-1);

         if (lastChar.equals("+") || lastChar.equals("-") ){
            result = result.substring(0, result.length()-1);
            textViewInputNumbers.setText(result);
        }

        try{
            result = "";
            String temp = input;
            if (equalClicked){
                temp = input + lastExpression;
            } else{
                saveLastExpression(input);
            }

            result = scriptEngine.eval(temp.replaceAll("%", "/100").replaceAll("x", "*").replaceAll("[^\\x00-\\x7F]", "/")).toString();
            BigDecimal decimal = new BigDecimal(result);
            result = decimal.setScale(8, BigDecimal.ROUND_HALF_UP).toPlainString();
            equalClicked = true;

        } catch (Exception e){
//            Toast.makeText(getApplicationContext(), "Wrong Format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (result.equals("Infinity")){
            Toast.makeText(getApplicationContext(), "Division by zero is not allowed", Toast.LENGTH_SHORT).show();
            textViewInputNumbers.setText(input);

        } else if (result.contains(".")){
            result = result.replaceAll("\\.?0*$", "");
            dotUsed = false;
            textViewInputNumbers.setText(result);

        }
    }

    private void saveLastExpression(String input)
    {
        String lastOfExpression = input.charAt(input.length() - 1) + "";
        if (input.length() > 1)
        {
            if (lastOfExpression.equals(")"))
            {
                lastExpression = ")";
                int numberOfCloseParenthesis = 1;

                for (int i = input.length() - 2; i >= 0; i--)
                {
                    if (numberOfCloseParenthesis > 0)
                    {
                        String last = input.charAt(i) + "";
                        if (last.equals(")"))
                        {
                            numberOfCloseParenthesis++;
                        } else if (last.equals("("))
                        {
                            numberOfCloseParenthesis--;
                        }
                        lastExpression = last + lastExpression;
                    } else if (defineLastCharacter(input.charAt(i) + "") == IS_OPERAND)
                    {
                        lastExpression = input.charAt(i) + lastExpression;
                        break;
                    } else
                    {
                        lastExpression = "";
                    }
                }
            } else if (defineLastCharacter(lastOfExpression + "") == IS_NUMBER)
            {
                lastExpression = lastOfExpression;
                for (int i = input.length() - 2; i >= 0; i--)
                {
                    String last = input.charAt(i) + "";
                    if (defineLastCharacter(last) == IS_NUMBER || defineLastCharacter(last) == IS_DOT)
                    {
                        lastExpression = last + lastExpression;
                    } else if (defineLastCharacter(last) == IS_OPERAND)
                    {
                        lastExpression = last + lastExpression;
                        break;
                    }
                    if (i == 0)
                    {
                        lastExpression = "";
                    }
                }
            }
        }
    }

    private int defineLastCharacter(String lastCharacter){
        try{
            Integer.parseInt(lastCharacter);
            return IS_NUMBER;
        } catch (NumberFormatException e){
        }

        if ((lastCharacter.equals("+") || lastCharacter.equals("-") || lastCharacter.equals("x") || lastCharacter.equals("\u00F7") || lastCharacter.equals("%")))
            return IS_OPERAND;

        if (lastCharacter.equals("("))
            return IS_OPEN_PARENTHESIS;

        if (lastCharacter.equals(")"))
            return IS_CLOSE_PARENTHESIS;

        if (lastCharacter.equals("."))
            return IS_DOT;

        return -1;
    }




}
