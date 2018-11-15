package com.prj666_183a06.xbudget;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseListInterface;
import com.prj666_183a06.xbudget.viewmodel.PlanViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExpenseInfo extends AppCompatActivity {
    Button b1, b2, b3, b4;
    LinearLayout container;
    TextView info;
    ExpenseViewModel example;
    PlanViewModel vm;
    List<String> titles;

    static ExpenseListInterface obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenseinfo);


        info = findViewById(R.id.texthere);

        example = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        vm = ViewModelProviders.of(this).get(PlanViewModel.class);

        titles = new ArrayList();
        titles = vm.getTitleList();

        obj = new ExpenseListInterface(example);



        b1 = findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.append(obj.print());
            }
        });

        b2 = findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.append(obj.costTotal() + "\n");
            }
        });

        b3 = findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.append(titles.toString() + "\n");
            }
        });

        b4 = findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Spinner sp = findViewById(R.id.spin);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, titles);
        sp.setAdapter(adapter);

    }
}
