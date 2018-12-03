package com.prj666_183a06.xbudget;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prj666_183a06.xbudget.ExpenseRoom.Expense;
import com.prj666_183a06.xbudget.viewmodel.PlanViewModel;

import java.util.ArrayList;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prj666_183a06.xbudget.database.Expenses;

import java.util.Calendar;
import java.util.List;

public class ExpenseAddEditExpenseR extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_ID =
            "com.prj666_183a06.xbudget.EXTRA_ID";
    public static final String EXTRA_STORE =
            "com.prj666_183a06.xbudget.EXTRA_STORE";

    public static final String EXTRA_ITEM =
            "com.prj666_183a06.xbudget.EXTRA_ITEM";

    public static final String EXTRA_DATE =
            "com.prj666_183a06.xbudget.EXTRA_DATE";

    public static final String EXTRA_COST =
            "com.prj666_183a06.xbudget.EXTRA_COST";

    public static final String EXTRA_CATEGORY =
            "com.prj666_183a06.xbudget.EXTRA_CATEGORY";

    public static final String EXTRA_DESCRIPTION =
            "com.prj666_183a06.xbudget.EXTRA_CATEGORY";


    private EditText editStore, editItem, editCost, editDescription;
    private TextView editDate;
    private Spinner editCategory;
    String type;
    int id;
    Button exit;
    PlanViewModel pvm;
    List<String> planTitles;
    Expense temp;

    private DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference("expenses");
    private double tempAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Intent intent = getIntent();

        if (intent.getStringExtra("type").equals("edit")) {
            type = "edit";
        } else {
            type = "add";
        }

        editStore = findViewById(R.id.edit_text_store);
        editItem = findViewById(R.id.edit_text_item);
        editCost = findViewById(R.id.edit_text_cost);
        editDate = findViewById(R.id.edit_date_view);
        editDescription = findViewById(R.id.edit_text_description);
        editCategory = findViewById(R.id.plans);

        //Populate title
        pvm = ViewModelProviders.of(this).get(PlanViewModel.class);

        planTitles = new ArrayList<>();
        planTitles = pvm.getTitleList();
        planTitles.add(0, "None");

        Log.e("title", planTitles.toString());

        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, planTitles);
        editCategory.setAdapter(planAdapter);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        if (type.equals("edit")) {
            setTitle("Edit Expense");

            temp = (Expense) intent.getSerializableExtra("expense");

            id = temp.getId();
            editStore.setText(temp.getExpenseStore());
            editItem.setText(temp.getExpenseItem());
            editCost.setText("" + temp.getExpenseCost());
            editDate.setText(temp.getExpenseDate());
            editDescription.setText(temp.getExpenseDescription());
            tempAmount = temp.getExpenseCost();

        } else {
            setTitle("Create Expense");
            id = -1;
        }

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        FloatingActionButton buttonSave = findViewById(R.id.button_save);
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveExpense();
//            }
//        });

        editDate.setText(getDate());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        editDate.setText(month + 1 + "/" + dayOfMonth + "/" + year);
    }

    private String getDate() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return month + "/" + day + "/" + year;
    }

    private void saveExpense() {

        String store, item, date, cost, category, description;
        Expense obj;
        Intent ret = new Intent();

        store = editStore.getText().toString();
        item = editItem.getText().toString();
        date = editDate.getText().toString();
        cost = editCost.getText().toString();
        category = editCategory.getSelectedItem().toString();
        description = editDescription.getText().toString();

        //if(store.trim().isEmpty() || item.trim().isEmpty() || date.trim().isEmpty() || cost.isEmpty()){
        if (date.trim().isEmpty() || cost.isEmpty()){
            Toast.makeText(this, "Please fill out the form", Toast.LENGTH_SHORT).show();
            return;
        }

        double newAmount = Double.valueOf(cost);

        obj = new Expense(store, date, item, category, newAmount, description);

        if (id != -1) {
            obj.setId(id);
        }

        String expId = expenseRef.push().getKey();
        Expenses expenses = new Expenses(store, item, newAmount, date, category, description);
        expenseRef.child(expId).setValue(expenses);

        ret.putExtra("expense", obj);
        setResult(RESULT_OK, ret);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveExpense();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
