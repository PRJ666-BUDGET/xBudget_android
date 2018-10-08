package com.prj666_183a06.xbudget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ExpenseCreateActivity extends AppCompatActivity {
    TextView store, date, item, cost;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createexpense);

        Intent intent = getIntent();

        String storeString = "Store: " + intent.getStringExtra("storeExtra");
        String dateString = "Date" + intent.getStringExtra("dateExtra");
        String itemString = "Item" + intent.getStringExtra("itemExtra");
        String costString = "Cost" + intent.getStringExtra("costExtra");


        store = (TextView) findViewById(R.id.storeResult);
        store.setText(storeString);

        date = (TextView) findViewById(R.id.dateResult);
        date.setText(dateString);

        item = (TextView)findViewById(R.id.itemResult);
        item.setText(itemString);

        cost = (TextView)findViewById(R.id.costResult);
        cost.setText(costString);


        Button button2 = (Button) findViewById(R.id.buttonHome);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}
