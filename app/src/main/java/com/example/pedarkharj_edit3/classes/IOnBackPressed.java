package com.example.pedarkharj_edit3.classes;

import android.content.Context;

public interface IOnBackPressed {
    /**
     * If you return true the back press will not be taken into account, otherwise the activity will act naturally
     * @return true if your processing has priority if not false
     */
    boolean onMyBackPressed(Context context);
}