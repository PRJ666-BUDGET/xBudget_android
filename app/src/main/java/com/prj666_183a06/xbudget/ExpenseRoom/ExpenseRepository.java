package com.prj666_183a06.xbudget.ExpenseRoom;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ExpenseRepository /*implements AsyncResult*/ {

    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;

    public ExpenseRepository(Application application) {

        ExpenseRoomDatabase db = ExpenseRoomDatabase.getInstance(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllExpense();
    }

    public void insert(Expense expense){
        new InsertExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void update(Expense expense){
        new UpdateExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void delete(Expense expense){
        new DeleteExpenseAsyncTask(expenseDao).execute(expense);
    }

    public void deleteAll(){
        new DeleteAllExpenseAsyncTask(expenseDao).execute();
    }

    public LiveData<List<Expense>> getAllExpenses(){
        return allExpenses;
    }

    public double getTotalCost(){
        return 0;
    }
    private static class InsertExpenseAsyncTask extends AsyncTask<Expense, Void, Void>{
        private ExpenseDao expenseDao;

        private InsertExpenseAsyncTask(ExpenseDao expenseDao){
                this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense...expenses){
            expenseDao.insert(expenses[0]);
            return null;
        }
    }

    private static class UpdateExpenseAsyncTask extends AsyncTask<Expense, Void, Void>{
        private ExpenseDao expenseDao;

        private UpdateExpenseAsyncTask(ExpenseDao expenseDao){
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense...expenses){
            expenseDao.update(expenses[0]);
            return null;
        }
    }

    private static class DeleteExpenseAsyncTask extends AsyncTask<Expense, Void, Void>{
        private ExpenseDao expenseDao;

        private DeleteExpenseAsyncTask(ExpenseDao expenseDao){
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense...expenses){
            expenseDao.delete(expenses[0]);
            return null;
        }
    }
    private static class DeleteAllExpenseAsyncTask extends AsyncTask<Void, Void, Void>{
        private ExpenseDao expenseDao;

        private DeleteAllExpenseAsyncTask(ExpenseDao expenseDao){
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Void...voids){
            expenseDao.deleteAllExpense();
            return null;
        }
    }

    /*private MutableLiveData<List<Expense>> searchResults =
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
    }*/
}
