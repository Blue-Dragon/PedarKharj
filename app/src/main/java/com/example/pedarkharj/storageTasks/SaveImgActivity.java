package com.example.pedarkharj.storageTasks;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageRequest;
import com.example.pedarkharj.R;
import com.example.pedarkharj.profile.URLs;
import com.example.pedarkharj.profile.VolleySingleton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SaveImgActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_CODE = 101;
    Button saveBtn, dlBtn, sizeBtn;
    ImageView iv1;
    Activity activity;
    Drawable drawable;
    Bitmap bitmap;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_img);

        activity = this;
        drawable = getResources().getDrawable(R.drawable.bk1);
//        bitmap = ((BitmapDrawable) drawable).getBitmap();
        tv1 = findViewById(R.id.tv1);
        iv1 = findViewById(R.id.iv_1);
        saveBtn = findViewById(R.id.save_to_storage_btn);
        dlBtn = findViewById(R.id.dl_btn);
        sizeBtn = findViewById(R.id.show_size_btn);

        dlBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        sizeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int size;
        int rounds = 0;
        int height=0;
        int width=0;

        switch (v.getId()){
            case R.id.save_to_storage_btn:
                if (Build.VERSION.SDK_INT >= 23 && !checkPermission()) requestPermissions();
                else saveToInternalStorage(bitmap);
            break;

            case R.id.show_size_btn:
                if (bitmap != null){

                    height = bitmap.getHeight();
                    width = bitmap.getWidth();
                    tv1.append("\nheight: "+ height+ "\nwidth: "+ width);

                    tv1.append("\n.......................................................................\ngetByteCount: "+bitmap.getByteCount()/1024+" KB\ngetAllocationByteCount: "+bitmap.getRowBytes()+"\n"+bitmap.getRowBytes());
//                    setStandardSize(bitmap),;
                    rounds = 0;
                    while (bitmap.getHeight() * bitmap.getWidth() > 90000){
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*0.8), (int) (bitmap.getHeight()*0.8), true);
                        rounds++;
                    }

                    height = bitmap.getHeight();
                    width = bitmap.getWidth();
                    tv1.append("\n\nheight: "+ height+ "\nwidth: "+ width+"\nrounds: "+rounds);

                    tv1.append("\n.......................................................................\ngetByteCount: "+bitmap.getByteCount()/1024+" KB\ngetAllocationByteCount: "+bitmap.getRowBytes()+"\n"+bitmap.getRowBytes());

                }else {
                    tv1.setText("0");
                }
                break;

            case R.id.dl_btn:
                ImageRequest imageRequest = new ImageRequest(URLs.URL_IMAGE_DIR+ "one.jpg",
                        response -> {
                            bitmap = response;
                            iv1.setImageBitmap(response);
                        },
                        0,
                        0,
                        ImageView.ScaleType.CENTER_CROP,
                        Bitmap.Config.ARGB_8888,
                        Throwable::printStackTrace //method reference instead of lambda
                );

                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
            break;
        }
    }


    private boolean checkPermission() {
        int chkW = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        int chkR = ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE);
        return chkW == PackageManager.PERMISSION_GRANTED && chkR == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(activity, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                boolean permissionR = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissionW = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (permissionR && permissionW) {
                    Toast.makeText(activity, "Granted", Toast.LENGTH_SHORT).show();
                    saveToInternalStorage(bitmap);
                } else {
                    Toast.makeText(activity, "Not Granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    private void saveToInternalStorage(Bitmap bmp) {
//        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//        File directory = wrapper.getDir("imageDir", Context.MODE_APPEND);
//        // Create imageDir
//        File myPath = new File(directory, "profile.jpg");

        Context context = getApplicationContext();
        File myPath = new File(context.getFilesDir(), "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e){
            e.printStackTrace();
            tv1.setText(e+"");
        //close fos
        } finally { try { if (fos != null) {fos.close();}} catch (IOException e) {e.printStackTrace(); tv1.setText(e+"");} }
        tv1.setText(myPath.getAbsolutePath());
    }

    public void setStandardSize (Bitmap bitmap, int reqHeight, int reqWidth){
        int reqSize = reqHeight*reqWidth;
        if (bitmap != null){
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            tv1.append("\nbefore:\nwidth: "+ width+"\nheight: "+ height+"\n"+height*width);
            while (height*width > reqSize){
                height /= 2;
                width /= 2;
            }

            tv1.append("\n\nDone\nwidth: "+ width+"\nheight: "+ height+"\n"+height*width);
        }
    }
}
