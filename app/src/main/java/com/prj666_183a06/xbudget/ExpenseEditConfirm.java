package com.prj666_183a06.xbudget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExpenseEditConfirm extends AppCompatActivity {
    TextView js, op;
    static JSONObject obj;
    static JSONArray arr;
    static JSONArray ret;
    static int position;
    static String type;
    static String output = "";
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

        ExpenseObj result;
        ExpenseJSON jsonDude = new ExpenseJSON();
        Intent intent = getIntent();

        js = findViewById(R.id.jsonTest);
        op = findViewById(R.id.textConfirm);

        String jsonString = readFromFile(getApplicationContext());
        Log.e("string", jsonString);

        result = new ExpenseObj(
                intent.getStringExtra("storeExtra"),
                intent.getStringExtra("dateExtra"),
                intent.getStringExtra("itemExtra"),
                intent.getStringExtra("costExtra")
        );

        try{

            arr = new JSONArray(jsonString);
            Log.e("arr", arr.toString());

            obj = new JSONObject(jsonDude.jsonToString(result));
            Log.e("obj", obj.toString());

            ret = new JSONArray();
            position = intent.getIntExtra("position", 0);

            for(int i = 0; i < arr.length() ; i++){
                if(position != i){
                    ret.put(arr.getJSONObject(i));
                }else{
                    ret.put(obj);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        op.setText("Are you sure you want to edit?\n");
        js.setText(
                "\nStore: "  + intent.getStringExtra("storeExtra") + "\n" +
                "Item: " +  intent.getStringExtra("itemExtra")+ "\n" +
                "Date: " +  intent.getStringExtra("dateExtra")+ "\n" +
                "Cost: " +  intent.getStringExtra("costExtra")+ "\n");

        Log.e("ret final", ret.toString());
        Button confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                writeToFile(ret.toString());
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
