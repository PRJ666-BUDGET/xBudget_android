package com.prj666_183a06.xbudget.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.widget.TextView;

import com.prj666_183a06.xbudget.HomeFragment;
import com.prj666_183a06.xbudget.R;
import com.prj666_183a06.xbudget.database.dao.PlanDao;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;

import java.lang.ref.WeakReference;
import java.util.List;

public class PlanRepository {

    private PlanDao planDao;
    private LiveData<List<PlanEntity>> planList;
//    private LiveData<List<PlanEntity>> planIncomeList;
//    private LiveData<List<PlanEntity>> planSavingList;

    public PlanRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        planDao = database.planDao();
        planList = planDao.getAllPlans();
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

    public void getPlanIncomeTotalFromHomeFragment(HomeFragment context) { new GetPlanIncomeTotalAsyncTaskFromHomeFragment(context, planDao).execute(); }

//    public LiveData<List<PlanEntity>> getPlanSavingList() { return planSavingList; }

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

    private static class GetPlanIncomeTotalAsyncTaskFromHomeFragment extends AsyncTask<Void, Void, Double> {

        private WeakReference<HomeFragment> activityReference;
        private PlanDao planDao;

        private GetPlanIncomeTotalAsyncTaskFromHomeFragment(HomeFragment context, PlanDao planDao) {
            activityReference = new WeakReference<>(context);
            this.planDao = planDao;
        }

        @Override
        protected Double doInBackground(Void... voids) {
            Double result = planDao.getPlanIncomeTotalDaily()
                            + planDao.getPlanIncomeTotalWeekly()
                            + planDao.getPlanIncomeTotalBiweekly()
                            + planDao.getPlanIncomeTotalMonthly();
            return result;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            HomeFragment homeActivity = activityReference.get();

            TextView mtvIncome = homeActivity.getActivity().findViewById(R.id.tvIncome);
            mtvIncome.setText("$" + String.format("%.2f", aDouble));
        }
    }
}
