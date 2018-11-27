package com.prj666_183a06.xbudget.database;

public class Expenses {

    private String expenseStore;
    private String expenseItem;
    private double expenseCost;
    private String expenseDate;
    private String expenseCategory;
    private String expenseDescription;

    public Expenses() {

    }

    public Expenses(String expenseStore, String expenseItem, double expenseCost, String expenseDate, String expenseCategory, String expenseDescription) {
        this.expenseStore = expenseStore;
        this.expenseItem = expenseItem;
        this.expenseCost = expenseCost;
        this.expenseDate = expenseDate;
        this.expenseCategory = expenseCategory;
        this.expenseDescription= expenseDescription;
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

    public String getExpenseCategory(){
        return expenseCategory;
    }

    public String getExpenseDescription(){
        return expenseDescription;
    }
}
