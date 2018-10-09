package com.prj666_183a06.xbudget.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.prj666_183a06.xbudget.Model.Plan;

import java.util.List;

@Dao
public interface PlanDAO {

    @Query("SELECT * FROM plans WHERE id=:planId")
    List<Plan> getPlanById(int planId);

    @Query("SELECT * FROM plans")
    List<Plan> getAllPlans();

    @Insert
    void insertPlan(Plan... plans);

    @Update
    void updatePlan(Plan... plans);

    @Delete
    void deletePlan(Plan... plans);

    @Query("DELETE FROM plans")
    void deleteAllPlan();

}
