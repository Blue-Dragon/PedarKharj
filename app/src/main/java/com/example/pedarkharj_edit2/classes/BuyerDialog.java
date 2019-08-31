package com.example.pedarkharj_edit2.classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.pages.AddExpenseActivity;

public class BuyerDialog extends Dialog implements View.OnClickListener {
    private Activity mActivity;
    public Dialog d;
    private Button yes, no;
    RecyclerView recyclerView;


    public BuyerDialog(Activity mActivity) {
        super(mActivity);
        // TODO Auto-generated constructor stub
        this.mActivity = mActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_buyer_dialog);
        yes = (Button) findViewById(R.id.btn_yes);     yes.setOnClickListener(this);
        no = (Button) findViewById(R.id.btn_no);        no.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_yes:
                mActivity.startActivity(new Intent(mActivity, AddExpenseActivity.class));
                mActivity.finish();
                break;

            case R.id.btn_no:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }

}
