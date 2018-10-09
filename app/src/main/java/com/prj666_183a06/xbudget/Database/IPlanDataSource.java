package com.prj666_183a06.xbudget.Database;

import com.prj666_183a06.xbudget.Model.Plan;

import java.util.List;

public interface IPlanDataSource {

    List<Plan> getPlanById(int planId);
    List<Plan> getAllPlans();
    void insertPlan(Plan... plans);
    void updatePlan(Plan... plans);
    void deletePlan(Plan plan);
    void deleteAllPlan();
}
