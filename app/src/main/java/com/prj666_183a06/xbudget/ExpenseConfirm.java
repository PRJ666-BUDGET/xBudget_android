package com.prj666_183a06.xbudget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExpenseConfirm extends AppCompatActivity {
    TextView js;
    static JSONObject obj;
    static JSONArray arr;

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("jsonStorage.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void writeToFile(String data) {
        String filename = "jsonStorage.json";
        String fileContents = data;
        FileOutputStream outputStream;
        Log.d("intial", "setitng up data");
        Log.d("data", data);

        try {
            Log.d("attempting", "attempting");
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createconfirm);

        File file = new File("jsonStorage.json");

        ExpenseObj result;
        ExpenseJSON jsonDude = new ExpenseJSON();
        Intent intent = getIntent();


        String jsonString = readFromFile(getApplicationContext());
        try {
            arr = new JSONArray(jsonString);
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("string", jsonString);
        result = new ExpenseObj(
                intent.getStringExtra("storeExtra"),
                intent.getStringExtra("dateExtra"),
                intent.getStringExtra("itemExtra"),
                intent.getStringExtra("costExtra")
                );

        try{
            obj = new JSONObject(jsonDude.jsonToString(result));
            arr.put(obj);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        String output =
                "Store:"  + intent.getStringExtra("storeExtra") + "\n" +
                "Date: " + intent.getStringExtra("dateExtra") + "\n" +
                "Item: " + intent.getStringExtra("itemExtra") + "\n" +
                "Cost: " + intent.getStringExtra("costExtra") +
                        "\n Are you sure you want to submit?";

        js = (TextView) findViewById(R.id.jsonTest);
        if(obj.toString() != null){
            js.setText(output);
        }

        Button confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                writeToFile(arr.toString());
                finish();
            }
        });

        Button homeButton = (Button) findViewById(R.id.cancel);
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}
