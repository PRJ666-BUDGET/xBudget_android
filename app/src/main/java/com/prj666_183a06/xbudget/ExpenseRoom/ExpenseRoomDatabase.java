package com.prj666_183a06.xbudget.ExpenseRoom;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.prj666_183a06.xbudget.database.entity.PlanEntity;

@Database(entities ={Expense.class, PlanEntity.class}, version = 1, exportSchema = false)

public abstract class ExpenseRoomDatabase extends RoomDatabase {

    public abstract ExpenseDao expenseDao();
    private static ExpenseRoomDatabase instance;

    static ExpenseRoomDatabase getInstance(final Context context){
        if (instance == null) {
            synchronized (ExpenseRoomDatabase.class) {
                if (instance == null) {
                    instance =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    ExpenseRoomDatabase.class, "xbudget")
                                    .fallbackToDestructiveMigration()
                                    .addCallback(roomCallBack)
                                    .build();
                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExpenseDao expenseDao;

        private PopulateDbAsyncTask(ExpenseRoomDatabase db){
            expenseDao = db.expenseDao();
        }

        @Override
        protected Void doInBackground(Void... voids){
            expenseDao.insert(new Expense("McDonalds", "12/12/2018", "Coffee", "None", 2.00));
            expenseDao.insert(new Expense("Best Buy", "12/12/2018", "Computer","None", 34442.00));
            expenseDao.insert(new Expense("Walmart", "12/12/2018", "Groceries", "None", 165.32));
            return null;
        }
    }
}