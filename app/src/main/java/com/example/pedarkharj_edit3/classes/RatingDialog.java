package com.example.pedarkharj_edit3.classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;
import com.example.pedarkharj_edit3.pages.AddExpenseActivity;
import com.yugansh.tyagi.smileyrating.SmileyRatingView;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RatingDialog extends Dialog {

    private static Activity activity;
    Activity mActivity;
    Context mContext;
    TextView tv;
    ImageView donateBkBtn, donatePicBtn;
    String comment;
    SmileyRatingView smileyRatingView;
    RatingBar ratingBar;




    public RatingDialog(Activity mActivity) {
        super(mActivity);
        setContext(mActivity);
    }

    //Default Constructor (obligatory)
    public RatingDialog() {
        super(activity);
    }
    public static void setContext(Activity activity1) {
        activity = activity1;
    }

    /******************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rating);

    }

}
