package com.prj666_183a06.xbudget.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.prj666_183a06.xbudget.database.dao.ExpenseDao;
import com.prj666_183a06.xbudget.database.entity.ExpenseEntity;
import com.prj666_183a06.xbudget.model.Expense;

import java.util.List;

public class ExpenseRepository implements AsyncResult {

    private MutableLiveData<List<Expense>> searchResults =
            new MutableLiveData<>();

    private LiveData<List<Expense>> allExpenses;
    private ExpenseDao expenseDao;

    public ExpenseRepository(Application application) {

        ExpenseRoomDatabase db;
        db = ExpenseRoomDatabase.getDatabase(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
    }

    public void insertExpense(Expense newexpense) {
        new queryAsyncTask.insertAsyncTask(expenseDao).execute(newexpense);
    }

    public void deleteExpense(String name) {
        new queryAsyncTask.deleteAsyncTask(expenseDao).execute(name);
    }

    public void findExpense(String name) {
        queryAsyncTask task = new queryAsyncTask(expenseDao);
        task.delegate = this;
        task.execute(name);
    }

    public LiveData<List<Expense>> getAllExpense() {
        return allExpenses;
    }

    public MutableLiveData<List<Expense>> getSearchResults() {
        return searchResults;
    }
    @Override
    public void asyncFinished(List<Expense> results){
        searchResults.setValue(results);
    }

    private static class queryAsyncTask extends
            AsyncTask<String, Void, List<Expense>> {

        private ExpenseDao asyncTaskDao;
        private ExpenseRepository delegate = null;

        queryAsyncTask(ExpenseDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<Expense> doInBackground(final String... params) {
            return asyncTaskDao.findExpense(params[0]);
        }

        @Override
        protected void onPostExecute(List<Expense> result) {
            delegate.asyncFinished(result);
        }

        private static class insertAsyncTask extends AsyncTask<Expense, Void, Void> {

            private ExpenseDao asyncTaskDao;

            insertAsyncTask(ExpenseDao dao) {
                asyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final Expense... params) {
                asyncTaskDao.insertExpense(params[0]);
                return null;
            }
        }

        private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {

            private ExpenseDao asyncTaskDao;

            deleteAsyncTask(ExpenseDao dao) {
                asyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final String... params) {
                asyncTaskDao.deleteExpense(params[0]);
                return null;
            }
        }
    }
}
