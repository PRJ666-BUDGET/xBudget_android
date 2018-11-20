package com.prj666_183a06.xbudget.database;

public class Expenses {

    private String expenseStore;
    private String expenseItem;
    private double expenseCost;
    private String expenseDate;

    public Expenses() {

    }

    public Expenses(String expenseStore, String expenseItem, double expenseCost, String expenseDate) {
        this.expenseStore = expenseStore;
        this.expenseItem = expenseItem;
        this.expenseCost = expenseCost;
        this.expenseDate = expenseDate;
    }

    public String getExpenseStore() {
        return expenseStore;
    }

    public String getExpenseItem() {
        return expenseItem;
    }

    public double getExpenseCost() {
        return expenseCost;
    }

    public String getExpenseDate() {
        return expenseDate;
    }
}
