package com.prj666_183a06.xbudget.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.prj666_183a06.xbudget.database.ExpenseRepository;
import com.prj666_183a06.xbudget.database.ExpenseRepository;
import com.prj666_183a06.xbudget.database.entity.ExpenseEntity;
import com.prj666_183a06.xbudget.database.entity.ExpenseEntity;
import com.prj666_183a06.xbudget.model.Expense;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository repository;
    private LiveData<List<Expense>> allProducts;
    private MutableLiveData<List<Expense>> searchResults;

    public ExpenseViewModel (Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allProducts = repository.getAllExpense();
        searchResults = repository.getSearchResults();
    }

    MutableLiveData<List<Expense>> getSearchResults() {
        return searchResults;
    }

    LiveData<List<Expense>> getAllExpenses() {
        return getAllExpenses();
    }

    public void insertExpense(Expense expense) {
        repository.insertExpense(expense);
    }

    public void findExpense(String name) {
        repository.findExpense(name);
    }

    public void deleteExpense(String name) {
        repository.deleteExpense(name);
    }

    /*
    public void getExpenseById(ExpenseEntity Expense) { repository.getExpenseById(Expense); }

    public void insert(ExpenseEntity Expense) {
        repository.insert(Expense);
    }

    public void update(ExpenseEntity Expense) {
        repository.update(Expense);
    }

    public void delete(ExpenseEntity Expense) {
        repository.delete(Expense);
    }

    public void deleteAllExpenses() {
        repository.deleteAllExpenses();
    }

    public LiveData<List<ExpenseEntity>> getExpenseList() {
        return ExpenseList;
    }*/
}
