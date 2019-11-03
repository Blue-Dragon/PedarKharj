package com.example.pedarkharj_edit2.classes;

public class Expense {
    //vital
    private int id;
    private Event event;
    private Participant buyer;
    private Participant[] userPartics;
    private String expenseTitle;
    //money
    private float expensePrice;
    private float expenseDebt;

    String created_at;

    public Expense() {

    }

    public Expense(Event event, Participant buyer, Participant[] userPartics, String expenseTitle, float expensePrice, float expenseDebt) {
        this.event = event;
        this.buyer = buyer;
        this.userPartics = userPartics;
        this.expenseTitle = expenseTitle;
        this.expensePrice = expensePrice;
        this.expenseDebt = expenseDebt;
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

    public float getExpensePrice() {
        return expensePrice;
    }

    public float getExpenseDebt() {
        return expenseDebt;
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

    public void setExpensePrice(float expensePrice) {
        this.expensePrice = expensePrice;
    }

    public void setExpenseDebt(float expenseDebt) {
        this.expenseDebt = expenseDebt;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}