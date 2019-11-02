package com.example.pedarkharj_edit2.classes;

public class Expense {
    //vital
    private int id;
    private int buyerId;
    private UserPartic[] userPartics;
    private String expenseTitle;
    //money
    private float expensePrice;
    private float expenseDebt;

    String created_at;

    public Expense(){

    }

    public Expense(int buyerId, UserPartic[] userPartics, String expenseTitle, float expensePrice, float expenseDebt) {
        this.buyerId = buyerId;
        this.userPartics = userPartics;
        this.expenseTitle = expenseTitle;
        this.expensePrice = expensePrice;
        this.expenseDebt = expenseDebt;
    }

    //GETTER
    public int getId() {
        return id;
    }
    public int getBuyerId() {
        return buyerId;
    }
    public UserPartic[] getUserPartics() {
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

    //SETTER
    public void setId(int id) {
        this.id = id;
    }
    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }
    public void setUserPartics(UserPartic[] userPartics) {
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

    //***********************************************/
       public  static class UserPartic {
       int userId;
       private float userDebt;

        public UserPartic() {
        }
        public UserPartic(int userId, float userDebt) {
            this.userId = userId;
            this.userDebt = userDebt;
        }

        public int getUserId() {
            return userId;
        }
        public void setUserId(int userId) {
            this.userId = userId;
        }
        public float getUserDebt() {
            return userDebt;
        }
        public void setUserDebt(float userDebt) {
            this.userDebt = userDebt;
        }
    }
}
