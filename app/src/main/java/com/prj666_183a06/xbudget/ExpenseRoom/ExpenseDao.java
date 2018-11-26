package com.prj666_183a06.xbudget.ExpenseRoom;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("DELETE FROM expense_table")
    void deleteAllExpense();

    @Query("SELECT * FROM expense_table ORDER BY expense_date DESC")
    LiveData<List<Expense>> getAllExpense();

    //Outside stuff
    @Query("SELECT SUM(expense_cost) FROM expense_table")
    double getTotalCost();

    @Query("SELECT expense_cost from expense_table")
    List<Double> getCostAll();

    @Query("SELECT expense_item from expense_table")
    List<String> getItemAll();

    @Query("SELECT expense_store from expense_table")
    List<String> getStoreAll();

    @Query("SELECT expense_date from expense_table")
    List<String> getDateAll();

    @Query("SELECT expense_category from expense_table")
    List<String> getCategoryAll();
    @Query("SELECT SUM(expense_cost) FROM expense_table")
    double getExpenseTotal();

    @Query("SELECT * FROM expense_table ORDER BY expense_date DESC")
    List<Expense> getExpenseList();

//    @Query("SELECT expense_date, sum(expense_cost) FROM expense_table GROUP BY expense_date ORDER BY expense_date")
    @Query("SELECT * FROM expense_table ORDER BY expense_date")
    List<Expense> getExpenseGroupList();


}
