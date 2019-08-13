package com.example.pedarkharj_edit2.classes;

import android.graphics.Bitmap;

public class Participant {
    private Bitmap profPic;
    private String name;
    private int expense, dong, result= expense-dong;


    //with pic
    public Participant(Bitmap profPic, String name, int expense, int dong) {
        this.profPic = profPic;
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


    public Bitmap getProfPic() {
        return profPic;
    }
    public void setProfPic(Bitmap profPic) {
        this.profPic = profPic;
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
    public int getResult() {
        return result;
    }

}
