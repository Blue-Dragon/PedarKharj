package com.example.pedarkharj_edit2.classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.pages.AddExpenseActivity;
import com.example.pedarkharj_edit2.pages.ContactsActivity;

import java.util.ArrayList;
import java.util.List;

public class BuyerDialog extends Dialog implements View.OnClickListener {
    private Activity mActivity;
    public Dialog d;
    private Button yes, no;
    RecyclerView recyclerView;
    ArrayList<Participant> mParticipants;
    ParticipantAdapter adapter;
    DatabaseHelper db;


    public BuyerDialog(Activity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_buyer_dialog);

        db = new DatabaseHelper(mActivity);
        yes = (Button) findViewById(R.id.btn_yes);     yes.setOnClickListener(this);
        no = (Button) findViewById(R.id.btn_no);        no.setOnClickListener(this);
        recyclerView = findViewById(R.id.chooseBuyer_RecView);
        doRecyclerView();


        db.closeDB();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_yes:
                mActivity.startActivity(new Intent(mActivity, AddExpenseActivity.class));
//                mActivity.finish();
                break;

            case R.id.btn_no:
                mActivity.startActivity(new Intent(mActivity, ContactsActivity.class));
//                mActivity.finish();
                break;

            default:
                break;
        }
        dismiss();
    }

    private Bitmap drawableToBitmap(int drawable){
        return BitmapFactory.decodeResource(mActivity.getResources(), drawable);
    }
//        participants.add(new Participant(drawableToBitmap(R.drawable.r),"غلوم"));
//        participants.add(new Participant("حسین عباس پور"));



    private void doRecyclerView() {
        mParticipants = new ArrayList<Participant>();
        db.closeDB();
        //show partices of the Event todo: update -> get event
        Event event = db.getEventById(1);
        List<Participant> participants0 = db.getAllParticeUnderEvent(1);
        Log.d("Event", event.getEventName());
        for (Participant participant : participants0){
            mParticipants.add(new Participant( participant.getName() ));
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 4, GridLayoutManager.VERTICAL, false);
//        gridLayoutManager.setOrientation(gridLayoutManager.scrollHorizontallyBy(3));
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adapter = new ParticipantAdapter(mActivity, R.layout.sample_contact, mParticipants, 3);
        recyclerView.setAdapter(adapter);

    }

}
