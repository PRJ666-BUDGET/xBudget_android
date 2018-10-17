package com.prj666_183a06.xbudget.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.prj666_183a06.xbudget.model.Expense;

@Entity(tableName = "expense_table")
public class ExpenseEntity implements Expense {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "expense_store")
    private String expenseStore;

    @ColumnInfo(name = "expense_date")
    private String expenseDate;

    @ColumnInfo(name = "expense_category")
    private double expenseCategory;

    @ColumnInfo(name = "expense_item")
    private String expenseItem;

    @ColumnInfo(name = "expense_total")
    private  String expenseTotal;

    public ExpenseEntity(String expenseStore, String expenseDate, double expenseCategory, String expenseItem, String expenseTotal) {
        this.expenseStore = expenseStore;
        this.expenseDate = expenseDate;
        this.expenseCategory = expenseCategory;
        this.expenseItem = expenseItem;
        this.expenseTotal = expenseTotal;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
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

    public double getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(double expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getExpenseItem() {
        return expenseItem;
    }

    public void setExpenseItem(String expenseItem) {
        this.expenseItem = expenseItem;
    }

    public String getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(String expenseTotal) {
        this.expenseTotal = expenseTotal;
    }
}
