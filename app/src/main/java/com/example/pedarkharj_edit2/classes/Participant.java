package com.example.pedarkharj_edit2.classes;

import android.graphics.Bitmap;

public class Participant {
    private Bitmap bitmap;
    private String name, result;
    private int expense, dong;


    //with pic
    public Participant(Bitmap bitmap, String name, int expense, int dong) {
        this.bitmap = bitmap;
        this.name = name;
        this.expense = expense;
        this.dong = dong;
    }
    //NO pic
    public Participant(String name, int expense, int dong) {
        this.name = name;
        this.expense = expense;
        this.dong = dong;
    }


    public Bitmap getProfBitmap() {
        return bitmap;
    }
    public void setProfBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getExpense() {
        return expense;
    }
    public void setExpense(int expense) {
        this.expense = expense;
    }
    public int getDong() {
        return dong;
    }
    public void setDong(int dong) {
        this.dong = dong;
    }

    public String getResult() {
        if (expense-dong > 0)
            return "+ "+ (expense-dong);
        else  return String.valueOf(expense-dong);
    }

}
