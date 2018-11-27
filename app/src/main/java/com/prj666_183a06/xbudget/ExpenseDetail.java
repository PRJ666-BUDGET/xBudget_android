package com.prj666_183a06.xbudget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.prj666_183a06.xbudget.ExpenseRoom.Expense;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;
import com.prj666_183a06.xbudget.database.Expenses;

import java.util.Calendar;

import static com.prj666_183a06.xbudget.ExpenseAddEditExpenseR.EXTRA_ITEM;

public class ExpenseDetail extends AppCompatActivity {

    TextView itemTV, storeTV, costTV, categoryTV, dateTV, descriptionTV, titleTV;
    String item, store, cateegory, date, description;
    int id;
    Double cost;
    Button edit, exit;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        final Intent intent = getIntent();
        setTitle("Detail");

        itemTV = findViewById(R.id.item);
        storeTV = findViewById(R.id.store);
        costTV = findViewById(R.id.cost);
        categoryTV = findViewById(R.id.category);
        dateTV = findViewById(R.id.date);
        descriptionTV = findViewById(R.id.description);

        titleTV = findViewById(R.id.title);

        id = intent.getIntExtra(EXTRA_ID, 0);
        item = intent.getStringExtra(EXTRA_ITEM);
        store = intent.getStringExtra(EXTRA_STORE);
        cost = intent.getDoubleExtra(EXTRA_COST, 0.0);
        cateegory = intent.getStringExtra(EXTRA_CATEGORY);
        date = intent.getStringExtra(EXTRA_DATE);
        description = intent.getStringExtra(EXTRA_DESCRIPTION);


        itemTV.setText(item);
        storeTV.setText(store);
        costTV.setText(cost+"");
        categoryTV.setText(cateegory);
        dateTV.setText(date);
        descriptionTV.setText(description);

        titleTV.setText(intent.getStringExtra(EXTRA_ITEM));

        edit = findViewById(R.id.edit);
        exit = findViewById(R.id.exit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editIntent = new Intent(getApplicationContext(), ExpenseAddEditExpenseR.class);
                editIntent.putExtra(EXTRA_ID, id);
                editIntent.putExtra(EXTRA_STORE, store);
                editIntent.putExtra(EXTRA_ITEM, item);
                editIntent.putExtra(EXTRA_CATEGORY, cateegory);
                editIntent.putExtra(EXTRA_DATE, date);
                editIntent.putExtra(EXTRA_COST, cost);
                editIntent.putExtra(EXTRA_DESCRIPTION, description);
                startActivityForResult(editIntent, 2);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("return from edit", "true");
        if(requestCode == 2 && resultCode == RESULT_OK){
            Intent intent = getIntent();
            int id = intent.getIntExtra(EXTRA_ID, 0);
            String storeR = data.getStringExtra(EXTRA_STORE);
            String itemR = data.getStringExtra(EXTRA_ITEM);
            String dateR = data.getStringExtra(EXTRA_DATE);
            double costR = data.getDoubleExtra(EXTRA_COST, 0.0);
            String categoryR = data.getStringExtra(EXTRA_CATEGORY);
            String descriptionR = data.getStringExtra(EXTRA_DESCRIPTION);

            Log.e("intent stuff", data.getStringExtra(EXTRA_STORE));

            Intent ret = new Intent();
            ret.putExtra(EXTRA_STORE, storeR.trim());
            ret.putExtra(EXTRA_ITEM, itemR.trim());
            ret.putExtra(EXTRA_DATE, dateR.trim());
            ret.putExtra(EXTRA_COST, costR);
            ret.putExtra(EXTRA_CATEGORY, categoryR);
            ret.putExtra(EXTRA_DESCRIPTION, descriptionR.trim());
            ret.putExtra(EXTRA_ID, id);

            setResult(RESULT_OK, ret);
            finish();
        }
    }
}
