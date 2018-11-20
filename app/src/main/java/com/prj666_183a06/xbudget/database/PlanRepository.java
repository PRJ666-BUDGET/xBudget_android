package com.prj666_183a06.xbudget.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.prj666_183a06.xbudget.ExpenseRoom.Expense;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseDao;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseObj;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseRepository;
import com.prj666_183a06.xbudget.HomeFragment;
import com.prj666_183a06.xbudget.R;
import com.prj666_183a06.xbudget.database.dao.PlanDao;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;

import java.util.ArrayList;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    //Getting list title
    public List<String> getTitleList(){
        GetTitleListAsyncTask at = new GetTitleListAsyncTask(planDao);
        List<String> temp = new ArrayList<>();
        at.execute();
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        temp = at.getTitleList();
        return temp;
    }

    public double getTotalCost() {
        getTotalAsyncTask arr = new PlanRepository.getTotalAsyncTask(planDao);
        arr.execute();
        try{
            Thread.sleep(200);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return arr.ret();
    }

//    private static class getAllAsyncTask extends AsyncTask<Expense, Void, Void>{
//        ExpenseDao expenseDao;
//        static List<ExpenseObj> temp;
//        List<String>  storel;
//        List<String>  iteml;
//        List<String>  datel;
//        List<Double>  costl;
//        List<String>  categoryl;
//
//        private getAllAsyncTask(ExpenseDao expenseDao){
//            this.expenseDao = expenseDao;
//            temp = new ArrayList<>();
//            storel = new ArrayList();
//            iteml = new ArrayList();
//            datel = new ArrayList();
//            costl = new ArrayList();
//            categoryl = new ArrayList();
//        }
//
//        @Override
//        protected Void doInBackground(Expense...expenses){
//            Log.e("doInbackground", "do");
//            this.storel = expenseDao.getStoreAll();
//            this.iteml = expenseDao.getItemAll();
//            this.datel = expenseDao.getDateAll();
//            this.costl = expenseDao.getCostAll();
//            this.categoryl = expenseDao.getCategoryAll();
//
//            for(int i = 0; i < iteml.size(); i++){
//                temp.add(new ExpenseObj(iteml.get(i), storel.get(i), datel.get(i),categoryl.get(i), costl.get(i)));
//            }
//
//            return null;
//        }
//
//        List<ExpenseObj> getAll(){
//            return temp;
//        }
//    }

    private static class getTotalAsyncTask extends AsyncTask<PlanEntity, Void, Void>{
        private PlanDao planDao;
        private static double ret;
        private getTotalAsyncTask(PlanDao planDao){
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(PlanEntity...plans){
            this.ret = planDao.getPlanIncomeTotalDaily()
                        + planDao.getPlanIncomeTotalWeekly()
                        + planDao.getPlanIncomeTotalBiweekly()
                        + planDao.getPlanIncomeTotalMonthly();
            Log.e("pTotal in background", ""+ret);
            return null;
        }
        public double ret(){
            Log.e("pTotal in function", ""+ret);
            return ret;
        }
    }

    private static class GetTitleListAsyncTask extends  AsyncTask<PlanEntity, Void, Void> {
        private PlanDao planDao;
        List<String> list;

        private GetTitleListAsyncTask(PlanDao planDao) {
            this.planDao = planDao;
            this.list = new ArrayList();
        }

        @Override
        protected Void doInBackground(PlanEntity... plans) {
            this.list = planDao.titleList();
            Log.e("background", planDao.titleList().toString());
            return null;
        }

        List<String> getTitleList(){
            Log.e("title get", list.toString());
            return list;
        }
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

//            TextView mtvIncome = homeActivity.getActivity().findViewById(R.id.tvIncome);
//            mtvIncome.setText("$" + String.format("%.2f", aDouble));

//            homeActivity.getTotalIncome(aDouble);
        }
    }

}
