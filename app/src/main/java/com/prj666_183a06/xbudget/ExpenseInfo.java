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
import java.util.HashMap;
import java.util.List;

public class ExpenseInfo extends AppCompatActivity {
    Button b1, b2, b3, b4;
    LinearLayout container;
    TextView info;
    ExpenseViewModel evm;
    PlanViewModel pvm;
    List<String> titles;

    static ExpenseListInterface obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenseinfo);


        info = findViewById(R.id.texthere);

        evm = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        pvm = ViewModelProviders.of(this).get(PlanViewModel.class);


        titles = new ArrayList();
        titles = pvm.getTitleList();

        obj = new ExpenseListInterface(evm, pvm);

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
                List<Float> templist = new ArrayList<>();
                templist = obj.costTotalByCategory();

                System.out.println(templist);

                for(int i = 0; i < templist.size(); i++){
                    info.append(titles.get(i) + ": " + templist.get(i) + "\n");
                    System.out.println(titles.get(i) + ": " + templist.get(i));
                }
            }
        });

        b3 = findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Float> listTemp = new HashMap<>();
                listTemp = obj.getCostByDaily();
                info.append(""+listTemp);
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
