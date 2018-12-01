package com.prj666_183a06.xbudget.database;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.prj666_183a06.xbudget.ExpenseRoom.Expense;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseDao;
import com.prj666_183a06.xbudget.database.dao.PlanDao;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;

import static com.prj666_183a06.xbudget.database.AppDatabase.DATABASE_VERSION;

@Database(entities = {PlanEntity.class, Expense.class}, version = DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    // for Singleton
    public static AppDatabase instance;

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "xbudget";

    public abstract PlanDao planDao();
    public abstract ExpenseDao expenseDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbExpenseAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private PlanDao planDao;

        private PopulateDbAsyncTask(AppDatabase database) {
            planDao = database.planDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            planDao.insertPlan(new PlanEntity("income", "Co-op", 1000, "bi-weekly"));
            planDao.insertPlan(new PlanEntity("income", "tutor", 500, "bi-weekly"));
            planDao.insertPlan(new PlanEntity("category", "Coffee", 5, "daily"));
            return null;
        }
    }

    public static class PopulateDbExpenseAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExpenseDao expenseDao;

        private PopulateDbExpenseAsyncTask(AppDatabase db){
            expenseDao = db.expenseDao();
        }

        @Override
        protected Void doInBackground(Void... voids){
            //rent
            expenseDao.insert(new Expense("Rent", "12/01/2018/", "Rent", "Rent", 700.00, ""));
            //takout
            expenseDao.insert(new Expense("SushiQ", "11/24/2018/", "Sushi", "Takeout", 10, ""));
            expenseDao.insert(new Expense("SushiQ", "11/17/2018/", "Sushi", "Takeout", 10, ""));
            expenseDao.insert(new Expense("SushiQ", "11/10/2018/", "Sushi", "Takeout", 10, ""));
            expenseDao.insert(new Expense("SushiQ", "11/03/2018/", "Sushi", "Takeout", 10, ""));
            expenseDao.insert(new Expense("SushiQ", "10/27/2018/", "Sushi", "Takeout", 10, ""));
            //coffee
            expenseDao.insert(new Expense("Tims", "10/27/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "10/28/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "10/29/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "10/30/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "10/31/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/1/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/2/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/3/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/4/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/5/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/6/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/7/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/8/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/9/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/10/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/11/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/12/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/13/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/14/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/15/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/16/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/17/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/18/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/19/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/20/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/21/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/22/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/23/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/24/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/25/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/26/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/27/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/28/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/29/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/28/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "11/29/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "11/30/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "12/1/2018/", "Coffee", "Coffee", 3, ""));
            expenseDao.insert(new Expense("Tims", "12/2/2018/", "Coffee", "Coffee", 2, ""));
            expenseDao.insert(new Expense("Aroma", "12/3/2018/", "Coffee", "Coffee", 3, ""));
            //groceries
            expenseDao.insert(new Expense("NoFrills", "11/24/2018/", "Groceries", "Groceries", 75, ""));
            expenseDao.insert(new Expense("NoFrills", "11/17/2018/", "Groceries", "Groceries", 75, ""));
            expenseDao.insert(new Expense("NoFrills", "11/10/2018/", "Groceries", "Groceries", 75, ""));
            expenseDao.insert(new Expense("NoFrills", "11/03/2018/", "Groceries", "Groceries", 75, ""));
            expenseDao.insert(new Expense("NoFrills", "10/27/2018/", "Groceries", "Groceries", 75, ""));
            //gas
            expenseDao.insert(new Expense("Gas", "11/24/2018/", "Gas", "Gas", 30, ""));
            expenseDao.insert(new Expense("Gas", "11/17/2018/", "Gas", "Gas", 30, ""));
            expenseDao.insert(new Expense("Gas", "11/10/2018/", "Gas", "Gas", 30, ""));
            expenseDao.insert(new Expense("Gas", "11/03/2018/", "Gas", "Gas", 30, ""));
            expenseDao.insert(new Expense("Gas", "10/27/2018/", "Gas", "Gas", 30, ""));
            //Some Incidentals
            expenseDao.insert(new Expense("BestBuy", "11/10/2018/", "Laptop", "None", 500, "Replacement"));
            expenseDao.insert(new Expense("Optical", "11/24/2018/", "Glasses", "None", 200, ""));
            expenseDao.insert(new Expense("Wallmart", "11/29/2018/", "Shoes", "None", 20, ""));
            return null;
        }
    }
}