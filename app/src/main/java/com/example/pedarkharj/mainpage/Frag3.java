package com.example.pedarkharj.mainpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj.R;

public class Frag3 extends Fragment {

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            textView = (TextView) container.findViewById(R.id.textView3);
            textView.setOnClickListener(view -> Toast.makeText(getContext(), "response", Toast.LENGTH_SHORT).show());
        }

        return inflater.inflate(R.layout.fragment_3, container, false);
    }
}
