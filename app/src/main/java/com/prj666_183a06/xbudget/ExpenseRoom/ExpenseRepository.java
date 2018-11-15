package com.prj666_183a06.xbudget.ExpenseRoom;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.prj666_183a06.xbudget.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository /*implements AsyncResult*/ {

    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;
    private ArrayList<Expense> data;

    public ExpenseRepository(Application application) {

        //ExpenseRoomDatabase db = ExpenseRoomDatabase.getInstance(application);
        AppDatabase db = AppDatabase.getInstance(application);
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

    public List<ExpenseObj> getAll(){
        getAllAsyncTask li = new getAllAsyncTask(expenseDao);
        li.execute();
        return li.getAll();
    }

    private static class getAllAsyncTask extends AsyncTask<Expense, Void, Void>{
        ExpenseDao expenseDao;
        static List<ExpenseObj> temp;
        List<String>  storel;
        List<String>  iteml;
        List<String>  datel;
        List<Double>  costl;

        private getAllAsyncTask(ExpenseDao expenseDao){
            this.expenseDao = expenseDao;
            temp = new ArrayList<>();
            storel = new ArrayList();
            iteml = new ArrayList();
            datel = new ArrayList();
            costl = new ArrayList();
        }

        @Override
        protected Void doInBackground(Expense...expenses){
            Log.e("doInbackground", "do");
            this.storel = expenseDao.getStoreAll();
            this.iteml = expenseDao.getItemAll();
            this.datel = expenseDao.getDateAll();
            this.costl = expenseDao.getCostAll();

            for(int i = 0; i < iteml.size(); i++){
                temp.add(new ExpenseObj(iteml.get(i), storel.get(i), datel.get(i), costl.get(i) ));
            }
            return null;
        }

        List<ExpenseObj> getAll(){
            return temp;
        }
    }

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
}
