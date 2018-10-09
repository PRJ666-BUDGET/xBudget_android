package com.prj666_183a06.xbudget.Local;

import com.prj666_183a06.xbudget.Database.IPlanDataSource;
import com.prj666_183a06.xbudget.Model.Plan;

import java.util.List;

public class PlanDataSource implements IPlanDataSource {

    private PlanDAO planDAO;
    private static PlanDataSource mInstance;

    public PlanDataSource(PlanDAO planDAO) {
        this.planDAO = planDAO;
    }

    public static PlanDataSource getIntance(PlanDAO planDAO)
    {
        if(mInstance == null)
        {
            mInstance = new PlanDataSource(planDAO);
        }
        return mInstance;
    }

    @Override
    public List<Plan> getPlanById(int planId) {
        return planDAO.getPlanById(planId);
    }

    @Override
    public List<Plan> getAllPlans() {
        return planDAO.getAllPlans();
    }

    @Override
    public void insertPlan(Plan... plans) {
        planDAO.insertPlan(plans);
    }

    @Override
    public void updatePlan(Plan... plans) {
        planDAO.updatePlan(plans);
    }

    @Override
    public void deletePlan(Plan plan) {
        planDAO.deletePlan(plan);
    }

    @Override
    public void deleteAllPlan() {
        planDAO.deleteAllPlan();
    }
}
