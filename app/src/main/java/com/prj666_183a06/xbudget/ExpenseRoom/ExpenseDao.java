package com.prj666_183a06.xbudget.ExpenseRoom;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("SELECT SUM(expense_cost) FROM expense_table")
    double getExpenseTotal();

    @Query("SELECT * FROM expense_table ORDER BY expense_date DESC")
    List<Expense> getExpenseList();

//    @Query("SELECT expense_date, sum(expense_cost) FROM expense_table GROUP BY expense_date ORDER BY expense_date")
    @Query("SELECT * FROM expense_table ORDER BY expense_date")
    List<Expense> getExpenseGroupList();

}
