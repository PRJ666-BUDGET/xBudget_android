package com.prj666_183a06.xbudget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ExpenseCreateActivity extends AppCompatActivity {
    EditText storeInput, dateInput, itemInput, costInput;
    public static boolean loop = true;
    public final static int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getStringExtra("ret") == "false") {
                    loop = false;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createexpense);

        Intent intent = getIntent();
        Button enterData = (Button) findViewById(R.id.submitButton);
        enterData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                storeInput = (EditText) findViewById(R.id.storeText);
                dateInput = (EditText) findViewById(R.id.dateText);
                itemInput = (EditText) findViewById(R.id.itemText);
                costInput = (EditText) findViewById(R.id.costText);

                Intent intent2 = new Intent(ExpenseCreateActivity.this, ExpenseConfirm.class);

                intent2.putExtra("storeExtra", storeInput.getText().toString());
                intent2.putExtra("dateExtra", dateInput.getText().toString());
                intent2.putExtra("itemExtra", itemInput.getText().toString());
                intent2.putExtra("costExtra", costInput.getText().toString());


                startActivityForResult(intent2, REQUEST_CODE);

                finish();
            }
        });

        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
