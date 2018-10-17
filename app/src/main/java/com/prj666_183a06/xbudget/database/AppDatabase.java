package com.prj666_183a06.xbudget.database;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.prj666_183a06.xbudget.database.dao.ExpenseDao;
import com.prj666_183a06.xbudget.database.dao.PlanDao;
import com.prj666_183a06.xbudget.database.entity.ExpenseEntity;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;

import static com.prj666_183a06.xbudget.database.AppDatabase.DATABASE_VERSION;

@Database(entities = {PlanEntity.class, ExpenseEntity.class}, version = DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    // for Singleton
    public static AppDatabase instance;

    public static final int DATABASE_VERSION = 1;
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
            new PopulateDbAsyncTask(instance).execute();
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
            planDao.insertPlan(new PlanEntity("saving", "Coffee", 5, "daily"));

            return null;
        }
    }
}