package com.prj666_183a06.xbudget;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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


public class ExpenseActivity extends Fragment {

    public static final int ADD_REQUEST = 1;
    public static final int EDIT_REQUEST = 2;
    private ExpenseViewModel expenseViewModel;
    Expense returnedObj;

    final ExpenseAdapter adapter = new ExpenseAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_expense, container, false);

        FloatingActionButton buttonAddExpense = view.findViewById(R.id.button_add_expense);
        buttonAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExpenseAddEditExpenseR.class);

                intent.putExtra("type", "add");
                startActivityForResult(intent, ADD_REQUEST);

//                Intent intent = new Intent(getActivity(), ExpenseInfo.class);
//                startActivity(intent);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        //final ExpenseAdapter adapter = new ExpenseAdapter();
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
                Log.e("move", "doing");
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == 4) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Delete");
                    builder.setMessage("Are you sure you want to delete?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    expenseViewModel.delete(adapter.getExpenseAt(viewHolder.getAdapterPosition()));
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

//                expenseViewModel.delete(adapter.getExpenseAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(getContext(), "Expense deleted", Toast.LENGTH_SHORT).show();;
                }
                if (direction == 8) {
                    Expense temp = adapter.getExpenseAt(viewHolder.getAdapterPosition());
                    Intent intent = new Intent(getActivity(), ExpenseAddEditExpenseR.class);
                    //store, date, item, cat, cost, desc

                    Expense editObj = new Expense(
                            temp.getExpenseStore(),
                            temp.getExpenseDate(),
                            temp.getExpenseItem(),
                            temp.getExpenseCategory(),
                            temp.getExpenseCost(),
                            temp.getExpenseDescription());

                    editObj.setId(temp.getId());

                    intent.putExtra("expense", editObj);
                    /*
                    intent.putExtra(ExpenseAddEditExpenseR.EXTRA_ID, temp.getId());
                    intent.putExtra(ExpenseAddEditExpenseR.EXTRA_STORE, temp.getExpenseStore());
                    intent.putExtra(ExpenseAddEditExpenseR.EXTRA_ITEM, temp.getExpenseItem());
                    intent.putExtra(ExpenseAddEditExpenseR.EXTRA_COST, temp.getExpenseCost());
                    intent.putExtra(ExpenseAddEditExpenseR.EXTRA_DATE, temp.getExpenseDate());
                    intent.putExtra(ExpenseAddEditExpenseR.EXTRA_CATEGORY, temp.getExpenseCategory());
                    intent.putExtra(ExpenseAddEditExpenseR.EXTRA_DESCRIPTION, temp.getExpenseDescription());
                    */

                    intent.putExtra("type", "edit");
                    startActivityForResult(intent, EDIT_REQUEST);
                }
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ExpenseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Expense expense) {
                Intent intent = new Intent(getActivity(), ExpenseDetail.class);

                /*
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_ID, expense.getId());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_STORE, expense.getExpenseStore());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_ITEM, expense.getExpenseItem());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_COST, expense.getExpenseCost());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_DATE, expense.getExpenseDate());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_CATEGORY, expense.getExpenseCategory());
                intent.putExtra(ExpenseAddEditExpenseR.EXTRA_DESCRIPTION, expense.getExpenseDescription());
                startActivityForResult(intent, EDIT_REQUEST);
                */

                Log.e("id", ""+expense.getId());
                intent.putExtra("expense", expense);

                intent.putExtra("type", "edit");
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


        if (requestCode == ADD_REQUEST && resultCode == RESULT_OK) {
//            store = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_STORE);
//            item = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_ITEM);
//            date = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DATE);
//            cost = data.getDoubleExtra(ExpenseAddEditExpenseR.EXTRA_COST, 0.0);
//            category = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_CATEGORY);
//            description = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DESCRIPTION);

//            Expense expense = new Expense(store, date, item, category, cost, description);
//            expenseViewModel.insert(exp);


            returnedObj = (Expense) data.getSerializableExtra("expense");

            String temp =
                    returnedObj.getId() + "\n" +
                            returnedObj.getExpenseStore() + "\n" +
                            returnedObj.getExpenseItem() + "\n" +
                            returnedObj.getExpenseDate() + "\n" +
                            returnedObj.getExpenseCost() + "\n" +
                            returnedObj.getExpenseCategory() + "\n" +
                            returnedObj.getExpenseDescription() + "\n";

            Log.e("expense b4 ins/upd", temp);

            expenseViewModel.insert(returnedObj);
            Toast.makeText(getActivity(), "Expense saved", Toast.LENGTH_SHORT);

        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            /*
            int id = data.getIntExtra(ExpenseAddEditExpenseR.EXTRA_ID, -1);


            if (id == -1) {
                Toast.makeText(getContext(), "Cannot update expense record", Toast.LENGTH_SHORT).show();
                return;
            }

            store = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_STORE);
            item = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_ITEM);
            date = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DATE);
            cost = data.getDoubleExtra(ExpenseAddEditExpenseR.EXTRA_COST, 0.0);
            category = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_CATEGORY);
            description = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DESCRIPTION);


            Log.e("edit", category);
            Log.e("edit", description);

            Expense expense = new Expense(store, date, item, category, cost, description);
            expense.setId(id);
            expenseViewModel.update(expense);

            */


            returnedObj = (Expense) data.getSerializableExtra("expense");

            String temp =
                    returnedObj.getId() + "\n" +
                            returnedObj.getExpenseStore() + "\n" +
                            returnedObj.getExpenseItem() + "\n" +
                            returnedObj.getExpenseDate() + "\n" +
                            returnedObj.getExpenseCost() + "\n" +
                            returnedObj.getExpenseCategory() + "\n" +
                            returnedObj.getExpenseDescription() + "\n";

            Log.e("expense b4 ins/upd", temp);

            expenseViewModel.update(returnedObj);

        }
        adapter.notifyDataSetChanged();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Delete all");
                builder.setMessage("Are you sure you want to delete all expense data?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                expenseViewModel.deleteAllExpenses();
                                Toast.makeText(getContext(), "All expense deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
//                expenseViewModel.deleteAllExpenses();
//                Toast.makeText(getContext(),"All expense deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
