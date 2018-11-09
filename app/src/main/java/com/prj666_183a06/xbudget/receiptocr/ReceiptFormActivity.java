package com.prj666_183a06.xbudget.receiptocr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prj666_183a06.xbudget.R;



public class ReceiptFormActivity extends AppCompatActivity {

    private LinearLayout receiptElementsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        receiptElementsContainer = (LinearLayout) findViewById(R.id.receiptElementsContainer);

        //Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //receiptScroll content
        String[] textArray = {"One", "Two", "Three", "Four"};
        LinearLayout linearLayout = new LinearLayout(this);
        for( int i = 0; i < textArray.length; i++ )
        {
            TextView textView = new TextView(this);
            textView.setText(textArray[i]);
            receiptElementsContainer.addView(textView);
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
