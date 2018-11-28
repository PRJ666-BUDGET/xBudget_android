package com.prj666_183a06.xbudget.receiptocr;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prj666_183a06.xbudget.DatePickerFragment;
import com.prj666_183a06.xbudget.ExpenseAddEditExpenseR;
import com.prj666_183a06.xbudget.ExpenseRoom.Expense;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;
import com.prj666_183a06.xbudget.R;
import com.prj666_183a06.xbudget.database.Expenses;
import com.prj666_183a06.xbudget.viewmodel.PlanViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//TODO: This activity should receive an array of possible totals along with the best guess EXTRA_COST

public class ReceiptFormActivity extends AppCompatActivity {

    private EditText editStore, editItem, editCost, editDescription;
    private TextView editDate;
    private DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference("expenses");
    private double total;
    CheckBox check;
    LinearLayout costList;
    List<String> planTitles;
    PlanViewModel pvm;
    Button exit;
    Spinner costDrop, editCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        Intent intent = getIntent();

        costList = findViewById(R.id.cameraSpinner);
        costList.setVisibility(View.VISIBLE);

        total = intent.getDoubleExtra("EXTRA_COST", 0);

        ArrayList<Double> arr = (ArrayList<Double>)intent.getSerializableExtra("EXTRA_COST_ARR");
        Collections.sort(arr, Collections.reverseOrder());

        costDrop = findViewById(R.id.costDrop);

        ArrayAdapter<Double> costAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, arr);

        costDrop.setAdapter(costAdapter);

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

        check = findViewById(R.id.check);

        /*editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });*/

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });

        editDate.setText(getDate());

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        setTitle("Confirm Receipt");
        editCost.setText(Double.toString(total));

    }

    private String getDate(){
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return month + "/" + day + "/" + year;
    }

    private void saveExpense(){

        String store, item, date, cost, category, description;
        Expense obj;

        store = editStore.getText().toString();
        item = editItem.getText().toString();
        date = editDate.getText().toString();
        cost = editCost.getText().toString();
        category = editCategory.getSelectedItem().toString();
        description = editDescription.getText().toString();

        double newAmount;

        if(store.trim().isEmpty() || item.trim().isEmpty() || date.trim().isEmpty() || cost.isEmpty() || !check.isChecked()){
            Toast.makeText(this, "Please fill out the form" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(check.isChecked()){
            newAmount = Double.parseDouble(costDrop.getSelectedItem().toString());
        }else{
            newAmount = Double.parseDouble(cost);
        }

        obj = new Expense(store, date, item, category, newAmount, description);

        String expId = expenseRef.push().getKey();
        Expenses expenses = new Expenses(store, item, newAmount, date, category, description);
        expenseRef.child(expId).setValue(expenses);

        ExpenseViewModel evm = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        evm.insert(obj);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_expense_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_expense:
                saveExpense();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
