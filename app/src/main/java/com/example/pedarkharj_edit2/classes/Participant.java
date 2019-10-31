package com.example.pedarkharj_edit2.classes;

import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.EditText;

public class Participant {
    //vital
    private int id;
    private String name;
    //others
    private Bitmap bitmap;
    private boolean chkImg;
    private float expense;
    private float debt;
    private  int dongNumber;
    private int eventId;
    private int contactId;



        // constructors
    public Participant() {
            this.dongNumber = 1;
            this.chkImg = false;
        }

        //main
    public Participant(String name) {
            this.name = name;
            this.dongNumber = 1;
            this.chkImg = false;
        }

    public Participant(String name, float expense, float debt) {
            this.name = name;
            this.expense = expense;
            this.debt = debt;
            this.dongNumber = 1;
            this.chkImg = false;
        }

    //getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public boolean isChkImg() {
        return chkImg;
    }
    public float getExpense() {
        return expense;
    }
    public float getDebt() {
        return debt;
    }
    public int getDongNumber() {
        return dongNumber;
    }
    public int getEventId() {
        return eventId;
    }
    public int getContactId() {
        return contactId;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void setChkImg(boolean chkImg) {
        this.chkImg = chkImg;
    }
    public void setExpense(float expense) {
        this.expense = expense;
    }
    public void setDebt(float debt) {
        this.debt = debt;
    }
    public void setDongNumber(int dongNumber) {
        this.dongNumber = dongNumber;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**********************************************************************************************/

    public String getResult() {
        if (expense-debt > 0)
            return "+ "+ (expense-debt);
        else  return String.valueOf(expense-debt);
    }

}
