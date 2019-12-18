package com.example.pedarkharj_edit2.classes;

public class Expense {
    //vital
    private int id;
    private Event event;
    private Participant buyer;
    private Participant[] userPartics;
    private String expenseTitle;
    //money
    private int expensePrice;
    private int[] expenseDebts;

    String created_at;

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

    public Expense(Event event, Participant buyer, Participant[] userPartics, String expenseTitle, int expensePrice, int[] expenseDebts) {
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
    public Participant[] getUserPartics() {
        return userPartics;
    }
    public String getExpenseTitle() {
        return expenseTitle;
    }
    public int getExpensePrice() {
        return expensePrice;
    }
    public int[] getExpenseDebts() {
        return expenseDebts;
    }
    public String getCreated_at() {
        return created_at;
    }
    public Event getEvent() {
        return event;
    }

    //SETTER
    public void setId(int id) {
        this.id = id;
    }
    public void setBuyer(Participant buyer) {
        this.buyer = buyer;
    }
    public void setUserPartics(Participant[] userPartics) {
        this.userPartics = userPartics;
    }
    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }

    public void setExpensePrice(int expensePrice) {
        this.expensePrice = expensePrice;
    }
    public void setExpenseDebts(int[] expenseDebt) {
        this.expenseDebts = expenseDebt;
    }
    public void setExpenseDebts(int expenseDebt) {
        this.expenseDebts = new int[userPartics.length];
        int i =0;
        for (Participant ignored : this.userPartics){
            this.expenseDebts[i++] = expenseDebt;
        }
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}