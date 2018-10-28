package com.prj666_183a06.xbudget;

import android.app.DatePickerDialog;
import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class ExpenseEdit extends AppCompatActivity {
    EditText storeInput, itemInput, costInput;
    TextView dateInput;
    public static boolean loop = true;
    public final static int REQUEST_CODE = 1;
    static public TextView dateTester;
    static DatePickerDialog.OnDateSetListener dateListener;
    static JSONObject obj;
    static JSONArray arr;
    static int position;
    static String type;

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("jsonStorage.json");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createexpense);

        storeInput = (EditText) findViewById(R.id.storeText);
        dateInput = (TextView) findViewById(R.id.dateText);
        itemInput = (EditText) findViewById(R.id.itemText);
        costInput = (EditText) findViewById(R.id.costText);
        dateTester = (TextView) findViewById(R.id.dateText);

        String jsonarr = readFromFile(getApplicationContext());
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        Log.e("position", "" + position);

        try {
            arr = new JSONArray(jsonarr);
            Log.e("arr", arr.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            obj = arr.getJSONObject(position);
            storeInput.setText(obj.getString("store"));
            itemInput.setText(obj.getString("item"));
            dateInput.setText(obj.getString("date"));
            costInput.setText(obj.getString("cost"));
            Log.e("obj", obj.toString());
            //{"store":"testing","date":"Date","item":"testing","cost":"3.3"
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        dateTester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                Date date = new Date();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialogue = new DatePickerDialog(
                        ExpenseEdit.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateListener, year, month, day);

                dialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogue.show();

                dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Log.d("DATE TEST", "onDateSet: date:" + year + "/" + month + "/" + day);

                        String date = month + "/" + day + "/" + year;
                        dateTester.setText(date);
                    }
                };
            }
        });

        Button enterData = (Button) findViewById(R.id.submitButton);
        enterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent2 = new Intent(ExpenseEdit.this, ExpenseEditConfirm.class);

                intent2.putExtra("storeExtra", storeInput.getText().toString());
                intent2.putExtra("dateExtra", dateInput.getText().toString());
                intent2.putExtra("itemExtra", itemInput.getText().toString());
                intent2.putExtra("costExtra", costInput.getText().toString());
                intent2.putExtra("position", position);

                startActivity(intent2);
                finish();
            }
        });

        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}