package com.prj666_183a06.xbudget.ExpenseRoom;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;


import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository repository;
    private LiveData<List<Expense>> allExpenses;
    private MutableLiveData<List<Expense>> searchResults;

    public ExpenseViewModel (@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
        //searchResults = repository.getSearchResults();
    }

    public MutableLiveData<List<Expense>> getSearchResults() {
        return searchResults;
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public void insert(Expense expense){
        repository.insert(expense);
    }

    public void update(Expense expense){
        repository.update(expense);
    }

    public void delete(Expense expense){
        repository.delete(expense);
    }

    public void deleteAllExpenses(){
        repository.deleteAll();
    }

}
