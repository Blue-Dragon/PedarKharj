package com.example.pedarkharj_edit2.classes;

import com.example.pedarkharj_edit2.classes.web_db_pref.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Expense {
    //vital
    private int id;
    private int expenseId;
    private Event event;
    private Participant buyer;
    private List<Participant> userPartics;
    private String expenseTitle;
    //money
    private int expensePrice;
    private List<Integer> expenseDebts;

    private String created_at;

    //----------------------    Constructors    ---------------------//
    public Expense() {
    }

//    public Expense(Participant buyer, Participant[] userPartics, String expenseTitle, int expensePrice, int expenseDebts) {
//        //if we have same Dongs for all
//        this.expenseDebts = new int[userPartics.length];
//        int i =0;
//        for (int expense: this.expenseDebts){
//            this.expenseDebts[i++] = expenseDebts;
//        }
//
//        this.event = buyer.getEvent();
//        this.buyer = buyer;
//        this.userPartics = userPartics;
//        this.expenseTitle = expenseTitle;
//        this.expensePrice = expensePrice;
//    }

    public Expense(int expenseId ,Event event, Participant buyer, List<Participant> userPartics,
                   String expenseTitle, int expensePrice, List<Integer> expenseDebts) {
        this.expenseId = expenseId;
        this.event = buyer.getEvent();
        this.buyer = buyer;
        this.userPartics = userPartics;
        this.expenseTitle = expenseTitle;
        this.expensePrice = expensePrice;
        this.expenseDebts = expenseDebts;
        this.event = event;
    }


    //GETTER
    public int getId() {
        return id;
    }
    public Participant getBuyer() {
        return buyer;
    }
    public List<Participant> getUserPartics() {
        return userPartics;
    }
    public String getExpenseTitle() {
        return expenseTitle;
    }
    public int getExpensePrice() {
        return expensePrice;
    }
    public List<Integer> getExpenseDebts() { //todo: should be Map
        return expenseDebts;
    }
    public String getCreated_at() {
        return created_at;
    }
    public Event getEvent() {
        return event;
    }
    public int getExpenseId() {
        return expenseId;
    }

    //SETTER
    public void setId(int id) {
        this.id = id;
    }
    public void setBuyer(Participant buyer) {
        this.buyer = buyer;
    }
    public void setUserPartics(List<Participant> userPartics) {
        this.userPartics = userPartics;
    }
    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }
    public void setExpensePrice(int expensePrice) {
        this.expensePrice = expensePrice;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setExpenseDebts(List<Integer> expenseDebt) {
        this.expenseDebts = expenseDebt;
    }
    public void setExpenseDebts(int expenseDebt) {
        this.expenseDebts = new ArrayList<>();

        for (Participant ignored : this.userPartics){
            this.expenseDebts.add(expenseDebt);
        }
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int setExpenseIdByOrder(DatabaseHelper db){
        int lastExpenseId = db.getLastExpenseId();
        if (lastExpenseId < 1) this.expenseId = 1;
        else this.expenseId = lastExpenseId + 1;

        return expenseId;
    }
}