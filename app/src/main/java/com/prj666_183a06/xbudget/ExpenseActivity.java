package com.prj666_183a06.xbudget;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseAdapter;
import com.prj666_183a06.xbudget.ExpenseRoom.Expense;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ExpenseActivity extends Fragment{

    public static final int ADD_REQUEST = 1;
    public static final int EDIT_REQUEST= 2;
    private ExpenseViewModel expenseViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_expense, container, false);

        FloatingActionButton buttonAddExpense = view.findViewById(R.id.button_add_expense);
        buttonAddExpense.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExpenseAddEditExpenseR.class);
                startActivityForResult(intent, ADD_REQUEST);
            }
        });

        /*FloatingActionButton buttonTest = view.findViewById(R.id.test);
        buttonTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), test_activity.class);
                startActivity(intent);
            }
        });*/

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final ExpenseAdapter adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);

        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                adapter.submitList(expenses);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                expenseViewModel.delete(adapter.getExpenseAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Expense deleted", Toast.LENGTH_SHORT).show();;
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ExpenseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Expense expense) {
                Intent intent = new Intent(getActivity(), ExpenseAddEditExpenseR.class);
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_ID, expense.getId());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_STORE, expense.getExpenseStore());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_ITEM, expense.getExpenseItem());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_COST, expense.getExpenseCost());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_DATE, expense.getExpenseDate());
                startActivityForResult(intent, EDIT_REQUEST);
            }
        });
        
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Expense");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setHasOptionsMenu(true);
        if(requestCode == ADD_REQUEST && resultCode == RESULT_OK){
            String store = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_STORE);
            String item = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_ITEM);
            String date = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DATE);
            double cost = data.getDoubleExtra(ExpenseAddEditExpenseR.EXTRA_COST, 0.0);

            Expense expense = new Expense(store, date, item, cost);
            expenseViewModel.insert(expense);

            Toast.makeText(getActivity(), "Expense saved", Toast.LENGTH_SHORT);
        }else if(requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(ExpenseAddEditExpenseR.EXTRA_ID, -1);

            if(id == -1){
                Toast.makeText(getContext(), "Cannot update expense record", Toast.LENGTH_SHORT).show();
                return;
            }

            String store = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_STORE);
            String item = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_ITEM);
            String date = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DATE);
            double cost = data.getDoubleExtra(ExpenseAddEditExpenseR.EXTRA_COST, 0.0);

            Expense expense = new Expense(store, date, item, cost);
            expense.setId(id);
            expenseViewModel.update(expense);
            Toast.makeText(getContext(), "Expense updated", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), "Expense not saved", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuInflater menuInflater = inflater;
        menuInflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_expense:
                expenseViewModel.deleteAllExpenses();
                Toast.makeText(getContext(),"All expense deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
