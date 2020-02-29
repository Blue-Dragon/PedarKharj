package com.example.pedarkharj_edit3.classes.models;

public class Participant  {
    //vital
    private int id;
    private String name;
    private Event event;
    private Contact contact;
    //money
    private int expense;
    private int debt;
    //
    private boolean chkImg;
    private String created_at;



    // constructors
    public Participant() {
            this.chkImg = false;
        }

        //main
    public Participant(String name) {
            this.name = name;
            this.chkImg = false;
        }

    public Participant(String name, int expense, int debt) {
            this.name = name;
            this.expense = expense;
            this.debt = debt;
            this.chkImg = false;
        }


    /**
     * getters
     */

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBitmapStr() {
        return contact.getBitmapStr();
    }
    public boolean isChkImg() {
        return chkImg;
    }
    public int getExpense() {
        return expense;
    }
    public int getDebt() {
        return debt;
    }
    public Event getEvent() {
        return event;
    }
    public Contact getContact() {
        return contact;
    }
    public String getCreated_at() {
        return created_at;
    }

    /**
     * setters
     */
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBitmapStr(String bitmapStr) {
        this.contact.setBitmapStr(bitmapStr);
    }
    public void setChkImg(boolean chkImg) {
        this.chkImg = chkImg;
    }
    public void setExpense(int expense) {
        this.expense = expense;
    }
    public void setDebt(int debt) {
        this.debt = debt;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**********************************************************************************************/

    public String getResult() {
        if (expense-debt > 0)
            return "+ "+ (expense-debt);
        else  return String.valueOf(expense-debt);
    }

}
