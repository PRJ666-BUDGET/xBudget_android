package com.prj666_183a06.xbudget.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.os.AsyncTask;

import com.prj666_183a06.xbudget.database.entity.ExpenseEntity;
import com.prj666_183a06.xbudget.model.Expense;

import java.util.List;

@Dao
public interface ExpenseDao {


    /*original
    @Query("SELECT * FROM expense_table")

    LiveData<List<ExpenseEntity>> getAllExpenses();

    @Query("SELECT * FROM expense_table WHERE id=:expenseId")
    LiveData<ExpenseEntity> getExpenseById(int expenseId);

    @Insert
    void insertExpense(ExpenseEntity expenseEntity);

    @Update
    void updateExpense(ExpenseEntity expenseEntity);

    @Delete
    void deleteExpense(ExpenseEntity expenseEntity);

    @Query("DELETE FROM expense_table")
    void deleteAllExpenses();
    */

    @Insert
    void insertExpense(Expense expense);

    @Query("SELECT * FROM expense_table WHERE expense_item = :name")
    List<Expense> findExpense(String name);

    @Query("DELETE FROM expense_table WHERE expense_item = :name")
    void deleteExpense(String name);

    @Query("SELECT * FROM expense_table")
    LiveData<List<Expense>> getAllExpenses();
}
