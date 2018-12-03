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
    public static final int DETAIL_REQUEST = 3;
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

        setHasOptionsMenu(true);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

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

                    adapter.notifyDataSetChanged();
                }
                if (direction == 8) {
                    Expense temp = adapter.getExpenseAt(viewHolder.getAdapterPosition());
                    Intent intent = new Intent(getActivity(), ExpenseAddEditExpenseR.class);

                    Expense editObj = new Expense(
                            temp.getExpenseStore(),
                            temp.getExpenseDate(),
                            temp.getExpenseItem(),
                            temp.getExpenseCategory(),
                            temp.getExpenseCost(),
                            temp.getExpenseDescription());

                    editObj.setId(temp.getId());

                    intent.putExtra("expense", editObj);

                    intent.putExtra("type", "edit");
                    startActivityForResult(intent, EDIT_REQUEST);
                }
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ExpenseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Expense expense) {
                Intent intent = new Intent(getActivity(), ExpenseDetail.class);

                Log.e("id", ""+expense.getId());
                intent.putExtra("expense", expense);

                intent.putExtra("type", "edit");
                startActivityForResult(intent, DETAIL_REQUEST);

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


        if (requestCode == ADD_REQUEST && resultCode == RESULT_OK) {

            returnedObj = (Expense) data.getSerializableExtra("expense");

            Log.e("expense b4 ins/upd", returnedObj.print());
            Toast.makeText(getActivity(), "Expense saved", Toast.LENGTH_SHORT);

            expenseViewModel.insert(returnedObj);
            Toast.makeText(getActivity(), "Expense saved", Toast.LENGTH_SHORT);

        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {

            returnedObj = (Expense) data.getSerializableExtra("expense");

            Log.e("expense b4 ins/upd", returnedObj.print());

            expenseViewModel.update(returnedObj);

        }

        if(requestCode == DETAIL_REQUEST && resultCode == RESULT_OK){
            String type = data.getStringExtra("type");
            if(type.equals("delete")){

                returnedObj = (Expense) data.getSerializableExtra("expense");

                expenseViewModel.delete(returnedObj);

            }else if (type.equals("edit")){

                returnedObj = (Expense) data.getSerializableExtra("expense");

                Log.e("expense b4 up detail", returnedObj.print());

                expenseViewModel.update(returnedObj);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuInflater menuInflater = inflater;
        menuInflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_expense:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("You can't undo this action. Do you want to delete all expense data?").setPositiveButton("Yes", confirmDeleteDialogClickListener)
                        .setNegativeButton("No", confirmDeleteDialogClickListener).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    DialogInterface.OnClickListener confirmDeleteDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:

                    expenseViewModel.deleteAllExpenses();
                    Toast.makeText(getActivity(), "All expense are deleted", Toast.LENGTH_SHORT).show();

                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    /*public void deleteAll(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Delete all");
        builder.setMessage("You can't undo this action. Are you sure you want to delete all expenses?");
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
    }*/

}
