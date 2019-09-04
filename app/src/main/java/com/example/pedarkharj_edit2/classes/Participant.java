package com.example.pedarkharj_edit2.classes;

import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.EditText;

public class Participant {
    private Bitmap bitmap, chkImg;
    private String name, result;
    private int expense, dong, dongNumber;


/********      Main Page     ********/
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

/********      Expenses/Contacts Page   ********/
    //NO pic
    public Participant(String name) {
        this.name = name;
    }
    //with pic
    public Participant(Bitmap bitmap, String name) {
        this.bitmap = bitmap;
        this.name = name;
    }

    /********      DiffDong Page   ********/
    public Participant(String name, int dongNumber) {
        this.name = name;
        this.dongNumber = dongNumber;
    }
    //with pic
    public Participant(Bitmap bitmap, String name, int dongNumber) {
        this.bitmap = bitmap;
        this.name = name;
        this.dongNumber = dongNumber;
    }


    /**********************************************************************************************/
    public Bitmap getProfBitmap() {
        return bitmap;
    }
    public void setProfBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public Bitmap getChkImg() {
        return chkImg;
    }
    public void setChkImg(Bitmap chkImg) {
        this.chkImg = chkImg;
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
    public int getDongNumber() {
        return dongNumber;
    }
    public void setDongNumber(int dongNumber) {
        this.dongNumber = dongNumber;
    }

    public String getResult() {
        if (expense-dong > 0)
            return "+ "+ (expense-dong);
        else  return String.valueOf(expense-dong);
    }

}
