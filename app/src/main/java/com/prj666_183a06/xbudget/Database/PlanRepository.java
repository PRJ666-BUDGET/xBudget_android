package com.prj666_183a06.xbudget.Database;

import com.prj666_183a06.xbudget.Model.Plan;

import java.util.List;

public class PlanRepository implements IPlanDataSource {

    private IPlanDataSource mLocalDataSource;
    private static PlanRepository mInstance;

    public PlanRepository(IPlanDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static PlanRepository getInstance(IPlanDataSource mLocalDataSource)
    {
        if(mInstance == null)
        {
            mInstance = new PlanRepository(mLocalDataSource);
        }
        return mInstance;
    }

    @Override
    public List<Plan> getPlanById(int planId) {
        return mLocalDataSource.getPlanById(planId);
    }

    @Override
    public List<Plan> getAllPlans() {
        return mLocalDataSource.getAllPlans();
    }

    @Override
    public void insertPlan(Plan... plans) {
        mLocalDataSource.insertPlan(plans);
    }

    @Override
    public void updatePlan(Plan... plans) {
        mLocalDataSource.updatePlan(plans);
    }

    @Override
    public void deletePlan(Plan plan) {
        mLocalDataSource.deletePlan(plan);
    }

    @Override
    public void deleteAllPlan() {
        mLocalDataSource.deleteAllPlan();
    }
}
