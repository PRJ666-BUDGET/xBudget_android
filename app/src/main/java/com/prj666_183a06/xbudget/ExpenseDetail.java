package com.prj666_183a06.xbudget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        Log.e("obj get:", obj.print());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent editIntent = new Intent(getApplicationContext(), ExpenseAddEditExpenseR.class);
                editIntent.putExtra("expense", obj);
                editIntent.putExtra("type", "edit");
                startActivityForResult(editIntent, 2);
                return true;
//            case R.id.delete:
//                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                builder.setMessage("You can't undo this action. Do you want to delete all plans?").setPositiveButton("Yes", delete)
//                        .setNegativeButton("No", delete).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    DialogInterface.OnClickListener delete = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:

                    break;
                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };
}
