package com.prj666_183a06.xbudget;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
//imported expenseviewmodel class
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;

public class test_activity extends AppCompatActivity {
    //creating expenseviewmodel object but not initializing
    private ExpenseViewModel example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activity);

        TextView test;
        test = findViewById(R.id.testDouble);

        /*

        Import this at the top
        "import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;"

        create a ExpenseViewModel object with
        "public/private ExpenseViewModel example = ViewModelProviders.of(this).get(ExpenseViewModel.class);"

        call the function "getTotal()" in the ExpenseViewModel
        double temp = example.getTotal();
        */

        //initialize and define the ExepnseViewModel example object
        example = ViewModelProviders.of(this).get(ExpenseViewModel.class);

        //Setting the cost to the textview to show
        test.setText(""+example.getTotal());
    }
}
