package com.prj666_183a06.xbudget.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.prj666_183a06.xbudget.database.dao.ExpenseDao;
import com.prj666_183a06.xbudget.database.entity.ExpenseEntity;

import java.util.List;

public class ExpenseRepository {

    private ExpenseDao expenseDao;
    private LiveData<List<ExpenseEntity>> expenseList;

    public ExpenseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        expenseDao = database.expenseDao();
        expenseList = expenseDao.getAllExpenses();
    }
}
