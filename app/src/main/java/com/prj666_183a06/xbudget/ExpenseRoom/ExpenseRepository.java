package com.prj666_183a06.xbudget.ExpenseRoom;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.prj666_183a06.xbudget.HomeFragment;
import com.prj666_183a06.xbudget.R;
import com.prj666_183a06.xbudget.database.PlanRepository;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
        getTotalAsyncTask arr = new getTotalAsyncTask(expenseDao);
        arr.execute();
        return arr.ret();
    }

    public void getExpenseTotalFromHomeFragment(HomeFragment context) { new ExpenseRepository.GetExpenseTotalFromHomeFragment(context, expenseDao).execute(); }
//    public void getAccExpenseTotalFromHomeFragment(HomeFragment context) { new ExpenseRepository.GetAccExpenseTotalFromHomeFragment(context, expenseDao).execute(); }

    private static class getTotalAsyncTask extends AsyncTask<Expense, Void, Void>{
        private ExpenseDao expenseDao;
        private static double ret;
        private getTotalAsyncTask(ExpenseDao expenseDao){
            this.expenseDao = expenseDao;
        }

        @Override
        protected Void doInBackground(Expense...expenses){
            this.ret = expenseDao.getTotalCost();
            Log.e("ret in background", ""+ret);
            return null;
        }
        public double ret(){
            Log.e("ret in function", ""+ret);
            return ret;
        }
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


    private static class GetExpenseTotalFromHomeFragment extends AsyncTask<Void, Void, Double> {

        private WeakReference<HomeFragment> activityReference;
        private ExpenseDao expenseDao;

        private GetExpenseTotalFromHomeFragment(HomeFragment context, ExpenseDao expenseDao) {
            activityReference = new WeakReference<>(context);
            this.expenseDao = expenseDao;
        }

        @Override
        protected Double doInBackground(Void... voids) {
            Double result = expenseDao.getExpenseTotal();
            return result;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            HomeFragment homeActivity = activityReference.get();
            homeActivity.getTotalExpenses(aDouble);
        }
    }

//    private static class GetAccExpenseTotalFromHomeFragment extends AsyncTask<Void, Void, Double> {
//
//        private WeakReference<HomeFragment> activityReference;
//        private ExpenseDao expenseDao;
//        List<Expense> list = new ArrayList<Expense>();
//
//        private GetAccExpenseTotalFromHomeFragment(HomeFragment context, ExpenseDao expenseDao) {
//            activityReference = new WeakReference<>(context);
//            this.expenseDao = expenseDao;
//        }
//
//        @Override
//        protected List<Expense> doInBackground(Void... voids) {
//            List<Expense> result = expenseDao.getExpenseList();
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<Expense> expList) {
//            HomeFragment homeActivity = activityReference.get();
//            homeActivity.getExpensesList(expList);
//        }
//    }

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
