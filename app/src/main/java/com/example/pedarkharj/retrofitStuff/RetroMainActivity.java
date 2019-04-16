package com.example.pedarkharj.retrofitStuff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj.R;
import com.example.pedarkharj.profile.URLs;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetroMainActivity extends AppCompatActivity {

    TextView tvResult;
    //Synchronous
    //???
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retro_main);

        tvResult =findViewById(R.id.tv_retro_result);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TaskService taskService = retrofit.create(TaskService.class);

        Call<List<Post>> call = taskService.getPoosts();
        //Asyncronous
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    tvResult.setText("CODE: "+ response.code());
                    return;
                }
                Toast.makeText(RetroMainActivity.this, "OK", Toast.LENGTH_SHORT).show();
                List<Post> posts = response.body();

                for (Post post: posts){
                    String content = "";
                    content += "ID: "+ post.getId()+ "\n";
                    content += "User id: : "+ post.getUserId()+ "\n";
                    content += "Title: "+ post.getTitle()+ "\n";
                    content += "Text: "+ post.getText()+ "\n\n";

                    tvResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                tvResult.setText("ERROR: "+t.getMessage());
                t.printStackTrace();
            }

        });
    }
}
