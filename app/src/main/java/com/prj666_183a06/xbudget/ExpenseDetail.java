package com.prj666_183a06.xbudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prj666_183a06.xbudget.ExpenseRoom.Expense;

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
                editIntent.putExtra("expense", obj);
                editIntent.putExtra("type", "edit");
                startActivityForResult(editIntent, 2);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("return from edit", "true");
        if(requestCode == 2 && resultCode == RESULT_OK){

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
