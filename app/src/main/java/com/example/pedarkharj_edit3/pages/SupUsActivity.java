package com.example.pedarkharj_edit3.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pedarkharj_edit3.R;

public class SupUsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv;
    ImageView rateBtn, payBtn;
    String payUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_us);

        inits();
        onClicks();


    }



    private void inits() {
        tv = findViewById(R.id.tv);
        rateBtn = findViewById(R.id.rate_btn);
        payBtn = findViewById(R.id.pay_btn);
        payUri = "https://www.hamibash.com/hamedganjeali";
        if (!payUri.startsWith("http://") && !payUri.startsWith("https://"))
            payUri = "http://" + payUri;

        //-----------------
        String supText =
                getString(R.string.sup_us_tv_1)+ "\n\n\n"+
                        getString(R.string.sup_us_tv_2)+ "\n\n"+
                        getString(R.string.sup_us_tv_3)+ "\n\n"+
                        getString(R.string.sup_us_tv_4)+ "\n\n"+
                        getString(R.string.sup_us_tv_5);

        tv.setText(supText);
    }

    private void onClicks() {
        payBtn.setOnClickListener(this);
        rateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rate_btn:
                break;

            case R.id.pay_btn:
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(payUri));
                    startActivity(browserIntent);
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;

                default:
                    break;
        }

    }
}
