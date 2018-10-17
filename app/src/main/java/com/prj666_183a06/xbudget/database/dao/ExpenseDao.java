package com.prj666_183a06.xbudget.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.prj666_183a06.xbudget.database.entity.ExpenseEntity;

import java.util.List;

@Dao
public interface ExpenseDao {


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
}
