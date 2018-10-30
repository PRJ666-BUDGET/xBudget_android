package com.prj666_183a06.xbudget.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.prj666_183a06.xbudget.database.dao.PlanDao;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;

import java.util.List;

public class PlanRepository {

    private PlanDao planDao;
    private LiveData<List<PlanEntity>> planList;
    private LiveData<List<PlanEntity>> planIncomeList;
    private LiveData<List<PlanEntity>> planSavingList;

    public PlanRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        planDao = database.planDao();
        planList = planDao.getAllPlans();
        planIncomeList = planDao.getPlanIncomeList();
        planSavingList = planDao.getPlanSavingList();
    }

    public double getTotalCost(){
        getTotalAsyncTask arr = new getTotalAsyncTask(planDao);
        arr.execute();
        return arr.ret();
    }

    private static class getTotalAsyncTask extends AsyncTask<PlanEntity, Void, Void>{
        private PlanDao planDao_total;
        private static double ret;
        private getTotalAsyncTask(PlanDao planDao_total){
            this.planDao_total = planDao_total;
        }

        @Override
        protected Void doInBackground(PlanEntity...plans){
            this.ret = planDao_total.getPlanIncomeTotalDaily() + planDao_total.getPlanIncomeTotalWeekly()
                       + planDao_total.getPlanIncomeTotalBiweekly() + planDao_total.getPlanIncomeTotalMonthly();
            Log.e("ret in background", ""+ret);
            return null;
        }
        public double ret(){
            Log.e("ret in function", ""+ret);
            return ret;
        }
    }

    public void getPlanById(PlanEntity plan) {
        new GetPlanAsyncTask(planDao).execute(plan);
    }

    public void insert(PlanEntity plan) {
        new InsertPlanAsyncTask(planDao).execute(plan);
    }

    public void update(PlanEntity plan) {
        new UpdatePlanAsyncTask(planDao).execute(plan);
    }

    public void delete(PlanEntity plan) {
        new DeletePlanAsyncTask(planDao).execute(plan);
    }

    public void deleteAllPlans() {
        new DeleteAllPlansAsyncTask(planDao).execute();
    }

    public LiveData<List<PlanEntity>> getPlanList() {
        return planList;
    }

    public LiveData<List<PlanEntity>> getPlanIncomeList() { return planIncomeList; }

    public LiveData<List<PlanEntity>> getPlanSavingList() { return planSavingList; }

    // TODO: 2018-10-16 planDao.getPlanById(position)
    private static class GetPlanAsyncTask extends  AsyncTask<PlanEntity, Void, Void> {

        private PlanDao planDao;

        private GetPlanAsyncTask(PlanDao planDao) { this.planDao = planDao; }

        @Override
        protected Void doInBackground(PlanEntity... plans) {
            return null;
        }
    }

    private static class InsertPlanAsyncTask extends AsyncTask<PlanEntity, Void, Void> {

        private PlanDao planDao;

        private InsertPlanAsyncTask(PlanDao planDao) {
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(PlanEntity... plans) {
            planDao.insertPlan(plans[0]);
            return null;
        }
    }

    private static class UpdatePlanAsyncTask extends AsyncTask<PlanEntity, Void, Void> {

        private PlanDao planDao;

        private UpdatePlanAsyncTask(PlanDao planDao) {
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(PlanEntity... plans) {
            planDao.updatePlan(plans[0]);
            return null;
        }
    }

    private static class DeletePlanAsyncTask extends AsyncTask<PlanEntity, Void, Void> {

        private PlanDao planDao;

        private DeletePlanAsyncTask(PlanDao planDao) {
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(PlanEntity... plans) {
            planDao.deletePlan(plans[0]);
            return null;
        }
    }

    private static class DeleteAllPlansAsyncTask extends AsyncTask<Void, Void, Void> {

        private PlanDao planDao;

        private DeleteAllPlansAsyncTask(PlanDao planDao) {
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            planDao.deleteAllPlan();
            return null;
        }
    }
}
