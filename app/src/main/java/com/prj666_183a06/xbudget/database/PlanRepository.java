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
import com.prj666_183a06.xbudget.model.PlanObj;
import com.prj666_183a06.xbudget.pojo.PlanItem;

import java.util.ArrayList;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlanRepository {

    private PlanDao planDao;
    private LiveData<List<PlanEntity>> planList;
//    private LiveData<List<PlanEntity>> planIncomeList;
//    private LiveData<List<PlanEntity>> planSavingList;
    static List<PlanItem> temp;

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

    // Added by Irene to get all
    public List<PlanObj> getAll() {
        getAllAsyncTask li = new getAllAsyncTask(planDao);
        li.execute();
        try{
            Thread.sleep(100);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return li.getAll();
    }

    // Added by Irene
    private static class getAllAsyncTask extends AsyncTask<PlanEntity, Void, Void>{
        PlanDao planDao;
        static List<PlanObj> temp;
        List<String>  type;
        List<String>  title;
        List<Double>  amount;
        List<String>  period;

        private getAllAsyncTask(PlanDao planDao){
            this.planDao = planDao;
            temp = new ArrayList<>();
            type = new ArrayList();
            title = new ArrayList();
            amount = new ArrayList();
            period = new ArrayList();
        }

        //String type, String title, Double amount, String period
        @Override
        protected Void doInBackground(PlanEntity ...plans){
            Log.e("doInbackground", "do");
            this.type = planDao.getTypeAll();
            this.title = planDao.getTitleAll();
            this.amount = planDao.getAllCategoryTotal();
            this.period = planDao.getPeriodAll();

            for(int i = 0; i < title.size(); i++){
                temp.add(new PlanObj(type.get(i), title.get(i), amount.get(i), period.get(i)));
                Log.e("PlanObj", "doInbackground" + temp.get(i).getType() + temp.get(i).getAmount());
            }

            Log.e("PlanObj", "doInbackground" + temp.toString());

            return null;
        }


        List<PlanObj> getAll(){
            Log.e("temp", "getAll" + temp.toString());
            return temp;
        }
    }

    // Added by Irene to get totals
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
