package com.prj666_183a06.xbudget.receiptocr;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prj666_183a06.xbudget.ExpenseAddEditExpenseR;
import com.prj666_183a06.xbudget.R;
import com.prj666_183a06.xbudget.database.Expenses;

import java.util.Calendar;
import java.util.Date;


public class ReceiptFormActivity extends AppCompatActivity {

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

    private EditText editStore, editItem, editCost;
    private TextView editDate;
    static DatePickerDialog.OnDateSetListener dateListener;
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

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                Date date = new Date();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogue = new DatePickerDialog(
                        ReceiptFormActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateListener, year, month, day);

                dialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogue.show();

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
        });

        editDate.setText(getDate());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Expense");
            editStore.setText(intent.getStringExtra(EXTRA_STORE));
            editItem.setText(intent.getStringExtra(EXTRA_ITEM));
            editCost.setText(""+intent.getDoubleExtra(EXTRA_COST, 0.0));
            editDate.setText(intent.getStringExtra(EXTRA_DATE));
            tempAmount = intent.getDoubleExtra(EXTRA_COST, 0.0);
        }else{
            setTitle("Create Expense");
        }

    }

    private String getDate(){
        Calendar cal = Calendar.getInstance();
        Date date = new Date();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return month + "/" + day + "/" + year;
    }

    private void saveExpense(){
        String store = editStore.getText().toString();
        String item = editItem.getText().toString();
        String date = editDate.getText().toString();
        String cost = editCost.getText().toString();

        if(store.trim().isEmpty() || item.trim().isEmpty() || date.trim().isEmpty() || cost.isEmpty()){
            Toast.makeText(this, "Please fill out the form" , Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_STORE, store.trim());
        data.putExtra(EXTRA_ITEM, item.trim());
        data.putExtra(EXTRA_DATE, date.trim());
        data.putExtra(EXTRA_COST, Double.parseDouble(cost));

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
