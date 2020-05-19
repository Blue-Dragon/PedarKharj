package com.example.pedarkharj_edit3.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.RatingDialog;
import com.yugansh.tyagi.smileyrating.SmileyRatingView;

public class SupUsActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity = this;
    Context mContext = this;
    TextView tv, tv2;
    ImageView rateBtn, mailBtn, backBtn;
    ImageView donateBkBtn, donatePicBtn;
    String payUri;
    SmileyRatingView smileyRatingView;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_us);

        inits();
        onClicks();


    }



    private void inits() {
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        rateBtn = findViewById(R.id.rate_btn);
        mailBtn = findViewById(R.id.mail_btn);
        donatePicBtn = findViewById(R.id.donate_pic_btn);
        donateBkBtn = findViewById(R.id.donate_bk_btn);
        backBtn = findViewById(R.id.back_btn);
        smileyRatingView = findViewById(R.id.smiley_view);
        ratingBar = findViewById(R.id.rating_bar);

        payUri = "https://www.hamibash.com/hamedganjeali";
        if (!payUri.startsWith("http://") && !payUri.startsWith("https://"))
            payUri = "http://" + payUri;

        String supText =
                getString(R.string.sup_us_tv_2)+ "\n\n"+
                        getString(R.string.sup_us_tv_3)+ "\n\n"+
                        getString(R.string.sup_us_tv_4)+
                        getString(R.string.sup_us_tv_5)+
                        getString(R.string.sup_us_tv_6);
        //-----------------

        tv.setText(getString(R.string.sup_us_tv_1));
        tv2.setText(supText);
//        ratingBar.setRating(4f);
//        smileyRatingView.setSmiley(ratingBar.getRating());

    }

    private void onClicks() {
        donateBkBtn.setOnClickListener(this);
        donatePicBtn.setOnClickListener(this);
        rateBtn.setOnClickListener(this);
        mailBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                smileyRatingView.setSmiley(rating);
//                gotoBazzar();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rate_btn:
                gotoBazzar();


//                new RatingDialog(mActivity).show();
                break;

            case R.id.mail_btn:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getString(R.string.my_email)});
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                i.putExtra(Intent.EXTRA_TEXT   , getString(R.string.email_body));
                try {
                    startActivity(Intent.createChooser(i, "ارسال با ..."));
                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
                break;

            case R.id.donate_bk_btn:
            case R.id.donate_pic_btn:
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(payUri));
                    startActivity(browserIntent);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

            case R.id.back_btn:
                onBackPressed();
                break;


            default:
                    break;
        }

    }

    private void gotoBazzar() {
        //                Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
        Uri uri = Uri.parse("bazaar://details?id=" + mContext.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
        }
    }
}
