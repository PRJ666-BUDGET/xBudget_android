package com.prj666_183a06.xbudget.ExpenseRoom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "expense_table")
public class Expense{

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "expense_store")
    private String expenseStore;

    @ColumnInfo(name = "expense_item")
    private String expenseItem;

    @ColumnInfo(name = "expense_cost")
    private double expenseCost;

    @ColumnInfo(name = "expense_date")
    private String expenseDate;

    @ColumnInfo(name = "expense_category")
    private String expenseCategory;

    @ColumnInfo(name = "expense_description")
    private String expenseDescription;

    public Expense(String expenseStore, String expenseDate, String expenseItem, String expenseCategory, double expenseCost, String expenseDescription) {
        this.expenseStore = expenseStore;
        this.expenseCategory = expenseCategory;
        this.expenseItem = expenseItem;
        this.expenseDate = expenseDate;
        this.expenseCost = expenseCost;
        this.expenseDescription = expenseDescription;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public String getExpenseStore() {
        return expenseStore;
    }

    public void setExpenseStore(String expenseStore) {
        this.expenseStore = expenseStore;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }


    public String getExpenseItem() {
        return expenseItem;
    }

    public void setExpenseItem(String expenseItem) {
        this.expenseItem = expenseItem;
    }

    public double getExpenseCost() {
        return expenseCost;
    }

    public void setExpenseCost(double expenseTotal) {
        this.expenseCost = expenseTotal;
    }
}
