package com.prj666_183a06.xbudget.database.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.prj666_183a06.xbudget.database.entity.PlanEntity;

import java.util.List;

@Dao
public interface PlanDao {

    @Query("SELECT * FROM plan_table ORDER BY plan_type")
    LiveData<List<PlanEntity>> getAllPlans();

    @Query("SELECT * FROM plan_table WHERE plan_id=:planId")
    LiveData<PlanEntity> getPlanById(int planId);

    @Insert
    void insertPlan(PlanEntity planEntities);

    @Update
    void updatePlan(PlanEntity planEntities);

    @Delete
    void deletePlan(PlanEntity planEntities);

    @Query("DELETE FROM plan_table")
    void deleteAllPlan();

}
