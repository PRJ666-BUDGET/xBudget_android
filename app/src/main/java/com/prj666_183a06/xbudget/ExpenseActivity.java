package com.prj666_183a06.xbudget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;

public class ExpenseActivity extends Fragment {

    static int index;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Expense");
    }

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

    @Override
    public void onCreateContextMenu(final ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, 0, 0, "Edit");
        menu.add(0, 1, 0, "Delete");

        for(int i = 0; i < menu.size(); i++){
            menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch(menuItem.getItemId()){
                        case 0:
                            Toast.makeText(getContext(), menuItem.getItemId() + " Edit", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(getContext(), menuItem.getItemId() + " Delete", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return false;
                }
            });
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_expense, container, false);
        String very = readFromFile(getContext());
        //TextView te = view.findViewById(R.id.jsonText);
        //te.setText(very);

        //populate

        ArrayList<String> items = new ArrayList<>();
        JSONArray temp;
        JSONObject jsonObj;
        try{
            temp = new JSONArray(very);
            for(int i = 0; i < temp.length(); i++){
                jsonObj = temp.getJSONObject(i);
                items.add(
                        jsonObj.getString("item") + "\n" +
                                jsonObj.getString("store") + "\n" +
                                jsonObj.getString("cost") + "\n" +
                                jsonObj.getString("date")  );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> arr =
                new ArrayAdapter<>(getActivity(),
                        R.layout.button_layout, items);


        ListView listStore = view.findViewById(R.id.listId);
        listStore.setAdapter(arr);

        //end

        registerForContextMenu(listStore);

        Button createButton = view.findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent (getActivity(), ExpenseCreateActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
