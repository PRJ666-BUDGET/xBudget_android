package com.prj666_183a06.xbudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prj666_183a06.xbudget.ExpenseRoom.Expense;

import static com.prj666_183a06.xbudget.ExpenseAddEditExpenseR.EXTRA_ITEM;

public class ExpenseDetail extends AppCompatActivity {

    TextView itemTV, storeTV, costTV, categoryTV, dateTV, descriptionTV, titleTV;
    String item, store, category, date, description;
    int id;
    Double cost;
    Button edit, exit;
    Expense obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        final Intent intent = getIntent();
        setTitle("Detail");

        obj = (Expense) intent.getSerializableExtra("expense");

        itemTV = findViewById(R.id.item);
        storeTV = findViewById(R.id.store);
        costTV = findViewById(R.id.cost);
        categoryTV = findViewById(R.id.category);
        dateTV = findViewById(R.id.date);
        descriptionTV = findViewById(R.id.description);

        titleTV = findViewById(R.id.title);

        /*
        id = intent.getIntExtra(ExpenseAddEditExpenseR.EXTRA_ID, 0);
        item = intent.getStringExtra(ExpenseAddEditExpenseR.EXTRA_ITEM);
        store = intent.getStringExtra(ExpenseAddEditExpenseR.EXTRA_STORE);
        cost = intent.getDoubleExtra(ExpenseAddEditExpenseR.EXTRA_COST, 0.0);
        category = intent.getStringExtra(ExpenseAddEditExpenseR.EXTRA_CATEGORY);
        date = intent.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DATE);
        description = intent.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DESCRIPTION);
        */

        id = obj.getId();
        item = obj.getExpenseItem();
        store = obj.getExpenseStore();
        cost = obj.getExpenseCost();
        category = obj.getExpenseCategory();
        date = obj.getExpenseDate();
        description = obj.getExpenseDescription();

        itemTV.setText(item);
        storeTV.setText(store);
        costTV.setText(cost+"");
        categoryTV.setText(category);
        dateTV.setText(date);
        descriptionTV.setText(description);

        titleTV.setText(obj.getExpenseItem());

        edit = findViewById(R.id.edit);
        exit = findViewById(R.id.exit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editIntent = new Intent(getApplicationContext(), ExpenseAddEditExpenseR.class);
                /*editIntent.putExtra(ExpenseAddEditExpenseR.EXTRA_ID, id);
                editIntent.putExtra(ExpenseAddEditExpenseR.EXTRA_STORE, store);
                editIntent.putExtra(ExpenseAddEditExpenseR.EXTRA_ITEM, item);
                editIntent.putExtra(ExpenseAddEditExpenseR.EXTRA_CATEGORY, category);
                editIntent.putExtra(ExpenseAddEditExpenseR.EXTRA_DATE, date);
                editIntent.putExtra(ExpenseAddEditExpenseR.EXTRA_COST, cost);
                editIntent.putExtra(ExpenseAddEditExpenseR.EXTRA_DESCRIPTION, description);*/

                editIntent.putExtra("expense", obj);
                editIntent.putExtra("type", "edit");
                startActivityForResult(editIntent, 2);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent empty = new Intent();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("return from edit", "true");
        if(requestCode == 2 && resultCode == RESULT_OK){
            /*
            int id = intent.getIntExtra(ExpenseAddEditExpenseR.EXTRA_ID, 0);
            String storeR = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_STORE);
            String itemR = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_ITEM);
            String dateR = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DATE);
            double costR = data.getDoubleExtra(ExpenseAddEditExpenseR.EXTRA_COST, 0.0);
            String categoryR = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_CATEGORY);
            String descriptionR = data.getStringExtra(ExpenseAddEditExpenseR.EXTRA_DESCRIPTION);


            Intent ret = new Intent();
            ret.putExtra(ExpenseAddEditExpenseR.EXTRA_STORE, storeR.trim());
            ret.putExtra(ExpenseAddEditExpenseR.EXTRA_ITEM, itemR.trim());
            ret.putExtra(ExpenseAddEditExpenseR.EXTRA_DATE, dateR.trim());
            ret.putExtra(ExpenseAddEditExpenseR.EXTRA_COST, costR);
            ret.putExtra(ExpenseAddEditExpenseR.EXTRA_CATEGORY, categoryR);
            ret.putExtra(ExpenseAddEditExpenseR.EXTRA_DESCRIPTION, descriptionR.trim());
            ret.putExtra(ExpenseAddEditExpenseR.EXTRA_ID, id);
            */

            Intent ret = new Intent();
            Expense obj = (Expense) data.getSerializableExtra("expense");
            String temp =
                    obj.getId() + "\n" +
                    obj.getExpenseStore() + "\n" + obj.getExpenseItem() + "\n" +
                    obj.getExpenseDate() + "\n" +
                    obj.getExpenseCost() + "\n" +
                    obj.getExpenseCategory() + "\n" +
                    obj.getExpenseDescription() + "\n";

            Log.e("expense obj in detail", temp);

            ret.putExtra("expense", obj);
            setResult(RESULT_OK, ret);
            finish();
        }
    }
}
