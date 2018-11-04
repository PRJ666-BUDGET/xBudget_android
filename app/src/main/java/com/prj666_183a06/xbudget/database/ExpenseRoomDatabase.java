package com.prj666_183a06.xbudget.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.prj666_183a06.xbudget.database.dao.ExpenseDao;
import com.prj666_183a06.xbudget.database.entity.ExpenseEntity;

@Database(entities ={ExpenseEntity.class,}, version = 1, exportSchema = false)

public abstract class ExpenseRoomDatabase extends RoomDatabase {

    public abstract ExpenseDao expenseDao();
    private static ExpenseRoomDatabase INSTANCE;

    static ExpenseRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (ExpenseRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    ExpenseRoomDatabase.class, "expense_database")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}