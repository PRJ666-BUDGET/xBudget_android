package com.prj666_183a06.xbudget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;
import com.prj666_183a06.xbudget.viewmodel.PlanViewModel;
import java.util.ArrayList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prj666_183a06.xbudget.database.Expenses;
import java.util.Calendar;
import java.util.Date;
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
    private Spinner plansDrop;
    Button exit;
    PlanViewModel pvm;
    static DatePickerDialog.OnDateSetListener dateListener;
    List<String> planTitles;

    private DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference("expenses");
    private double tempAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        editStore = findViewById(R.id.edit_text_store);
        editItem = findViewById(R.id.edit_text_item);
        editCost = findViewById(R.id.edit_text_cost);
        editDate = findViewById(R.id.edit_date_view);
        editDescription = findViewById(R.id.edit_text_description);

        //Populate title
        pvm = ViewModelProviders.of(this).get(PlanViewModel.class);

        planTitles = new ArrayList<>();
        planTitles = pvm.getTitleList();
        planTitles.add(0, "None");

        Log.e("title", planTitles.toString());

        plansDrop = findViewById(R.id.plans);
        ArrayAdapter<String> planAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, planTitles);
        plansDrop.setAdapter(planAdapter);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        /*editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogue = new DatePickerDialog(
                        ExpenseAddEditExpenseR.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateListener, year, month, day);

                dialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogue.show();

                Log.e("testing", "date listener");
                dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Log.d("DATE TEST", "onDateSet: date:" + year + "/" + month + "/" + day);

                        String date = month + "/" + day + "/" + year;
                        editDate.setText(date);
                    }
                };
            }
        });*/

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        final Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Expense");
            editStore.setText(intent.getStringExtra(EXTRA_STORE));
            editItem.setText(intent.getStringExtra(EXTRA_ITEM));
            editCost.setText(""+intent.getDoubleExtra(EXTRA_COST, 0.0));
            editDate.setText(intent.getStringExtra(EXTRA_DATE));
            editDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            tempAmount = intent.getDoubleExtra(EXTRA_COST, 0.0);
        }else{
            setTitle("Create Expense");
        }

        exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveExpense(1, intent);
            }
        });

        editDate.setText(getDate());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        editDate.setText(month+1 + "/" + dayOfMonth  + "/" +  year);
    }

    private String getDate(){
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return month + "/" + day + "/" + year;
    }

    private void saveExpense(int i, Intent org){

        String store, item, date, cost, category, description;

        if(i == 1){
            store = editStore.getText().toString();
            item = editItem.getText().toString();
            date = editDate.getText().toString();
            cost = editCost.getText().toString();
            category = plansDrop.getSelectedItem().toString();
            description = editDescription.getText().toString();
        }else{
            store = org.getStringExtra(EXTRA_STORE);
            item = org.getStringExtra(EXTRA_ITEM);
            cost = ""+ org.getDoubleExtra(EXTRA_COST, 0.0);
            date = org.getStringExtra(EXTRA_DATE);
            category = org.getStringExtra(EXTRA_CATEGORY);
            description = org.getStringExtra(EXTRA_DESCRIPTION);
        }

        if(store.trim().isEmpty() || item.trim().isEmpty() || date.trim().isEmpty() || cost.isEmpty()){
            Toast.makeText(this, "Please fill out the form" , Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_STORE, store.trim());
        data.putExtra(EXTRA_ITEM, item.trim());
        data.putExtra(EXTRA_DATE, date.trim());
        data.putExtra(EXTRA_COST, Double.parseDouble(cost));
        data.putExtra(EXTRA_CATEGORY, category);
        data.putExtra(EXTRA_DESCRIPTION, description.trim());

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        String expId = expenseRef.push().getKey();
        double newAmount = Double.valueOf(cost);
        if (tempAmount != newAmount){
            newAmount -= tempAmount;
        }

        Expenses expenses = new Expenses(store, item, newAmount, date);
        expenseRef.child(expId).setValue(expenses);

        setResult(RESULT_OK, data);
        finish();
    }

    /*@Override

    top right corner
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
    }*/
}
