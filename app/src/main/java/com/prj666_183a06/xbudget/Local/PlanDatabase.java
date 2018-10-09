package com.prj666_183a06.xbudget.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.prj666_183a06.xbudget.Model.Plan;
import com.prj666_183a06.xbudget.Model.Test;

import static com.prj666_183a06.xbudget.Local.PlanDatabase.DATABASE_VERSION;

@Database(entities = {Plan.class, Test.class}, version = DATABASE_VERSION)
public abstract class PlanDatabase extends RoomDatabase {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "xBudget";

    public abstract PlanDAO planDAO();

    public static PlanDatabase mInstance;

    public static PlanDatabase getInstance(Context context) {
        if(mInstance == null) {
            mInstance = Room.databaseBuilder(context, PlanDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }
}
