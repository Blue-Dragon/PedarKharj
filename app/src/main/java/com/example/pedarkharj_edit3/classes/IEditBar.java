package com.example.pedarkharj_edit3.classes;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.R;

public interface IEditBar {

    /**
     * Should be added in init part of OnCreate
     */
    default void initEditBar (TextView counter_text_view, TextView title){
        counter_text_view.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        Routines.counter = 0;
    }

    //Edit & Delete
   default void setActionModeOn(Toolbar toolbar, TextView counter_text_view, TextView title) {
        toolbar.getMenu().clear();//clear activity menu
        toolbar.inflateMenu(R.menu.menu_action_mode);//inflate action mode menu
        counter_text_view.setVisibility(View.VISIBLE); //make textView visible on it
//        updateCounter(counter); //show textView with nothing selected by def
        title.setVisibility(View.GONE);
        Routines.is_in_action_mode = true;
//        adaptor.notifyDataSetChanged();//notify adapter about this  change
    }

    //Delete only
   default void setActionMode2On(Toolbar toolbar, TextView counter_text_view, TextView title) {
        toolbar.getMenu().clear();//clear activity menu
        toolbar.inflateMenu(R.menu.menu_action_mode_2);//inflate action mode menu
        counter_text_view.setVisibility(View.VISIBLE); //make textView visible on it
        title.setVisibility(View.GONE);
        Routines.is_in_action_mode = true;
        Routines.selectedItemId = 0;
//        is_select_one = false;
    }

    /**
     *  You should clear List after this method like : selectionList.clear();
     */
    default void setActionModeOff(Toolbar toolbar, TextView counter_text_view, TextView title, MyAdapter adaptor) {
        toolbar.getMenu().clear();//clear activity menu
//        toolbar.inflateMenu(R.menu.menu_action_mode);
        counter_text_view.setVisibility(View.GONE);
        title.setVisibility(View.VISIBLE);
        Routines.is_in_action_mode = false;//make checkbox visible
        adaptor.notifyDataSetChanged();//notify adapter about this  change
        Routines.counter = 0;
        Routines.selectedItemId = 0;
    }

    /**
     * textview above
     */
    default void updateCounter(int counter, TextView counter_text_view) {
        counter_text_view.setText(counter + " رویداد انتخاب شده");
    }

    default void selectionChangeColor(Context mContext, int id, MyAdapter adaptor) {
        adaptor.setForeground( new ColorDrawable(ContextCompat.getColor(mContext, id)));
    }
}
