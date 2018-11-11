package com.prj666_183a06.xbudget.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.prj666_183a06.xbudget.database.PlanRepository;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;

import java.util.List;

public class PlanViewModel extends AndroidViewModel {

    private PlanRepository repository;
    private LiveData<List<PlanEntity>> planList;

    public PlanViewModel(@NonNull Application application) {
        super(application);
        repository = new PlanRepository(application);
        planList = repository.getPlanList();
    }

//    public double getTotal() { return repository.getTotalCost(); }

    public void getPlanById(PlanEntity plan) { repository.getPlanById(plan); }

    public void insert(PlanEntity plan) {
        repository.insert(plan);
    }

    public void update(PlanEntity plan) {
        repository.update(plan);
    }

    public void delete(PlanEntity plan) {
        repository.delete(plan);
    }

    public void deleteAllPlans() {
        repository.deleteAllPlans();
    }

    public LiveData<List<PlanEntity>> getPlanList() {
        return planList;
    }
}
