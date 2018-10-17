package com.prj666_183a06.xbudget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class ExpenseCreateActivity extends AppCompatActivity {
    EditText storeInput, itemInput, costInput;
    TextView dateInput;
    public static boolean loop = true;
    public final static int REQUEST_CODE = 1;
    static public TextView dateTester;
    static DatePickerDialog.OnDateSetListener dateListener;

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


        dateTester = (TextView) findViewById(R.id.dateText);
        dateTester.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int year  = localDate.getYear();
                int month = localDate.getMonthValue() - 1;
                int day   = localDate.getDayOfMonth();

                /*int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.YEAR);*/

                DatePickerDialog dialogue = new DatePickerDialog(
                        ExpenseCreateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateListener, year, month, day);

                dialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogue.show();

                dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Log.d("DATE TEST", "onDateSet: date:" + year + "/" + month + "/" + day);

                        String date = month  + "/" + day + "/" + year;
                        dateTester.setText(date);
                    }
                };
            }
        });


        Button enterData = (Button) findViewById(R.id.submitButton);
        enterData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                storeInput = (EditText) findViewById(R.id.storeText);
                dateInput = (TextView) findViewById(R.id.dateText);
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
