package com.prj666_183a06.xbudget;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prj666_183a06.xbudget.adapter.ExpenseListAdapter;
import com.prj666_183a06.xbudget.crud.CreateUpdatePlanActivity;
import com.prj666_183a06.xbudget.pojo.Expense;
import com.prj666_183a06.xbudget.viewmodel.ExpenseViewModel;

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

import static android.app.Activity.RESULT_OK;

public class ExpenseActivity extends Fragment{

    private ExpenseViewModel eViewModel;
    private ExpenseListAdapter adapater;

    private TextView expenseId;
    private EditText expenseName;
    private EditText expenseCost;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Expense");
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            File file = new File("/data/data/com.prj666_183a06.xbudget/files/jsonStorage.json");
            if(!file.exists()){
                file.createNewFile();
            }
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

        Log.e("return", ret);
        return ret;
    }

    private void clearFields() {
        expenseId.setText("");
        expenseName.setText("");
        expenseCost.setText("");
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

                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
                    int listPosition = info.position;
                    Intent intent;
                    switch(menuItem.getItemId()){
                        case 0:
                            intent = new Intent (getActivity(), ExpenseEdit.class);
                            intent.putExtra("position", listPosition);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent (getActivity(), ExpenseDelete.class);
                            intent.putExtra("position", listPosition);
                            startActivity(intent);
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

        eViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);

        expenseId = getView().findViewById(R.id.expenseId);
        expenseName = getView().findViewById(R.id.expenseName);
        expenseCost = getView().findViewById(R.id.expenseCost);

        listenerSetup();
        observerSetup();
        recyclerSetup();

        private void listenerSetup() {

            Button addButton = getView().findViewById(R.id.addButton);
            Button findButton = getView().findViewById(R.id.findButton);
            Button deleteButton = getView().findViewById(R.id.deleteButton);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String name = expenseName.getText().toString();
                    String cost = expenseCost.getText().toString();


                    if (!name.equals("") && !quantity.equals("")) {
                        Expense expense = new Expense(name,
                                Double.parseDouble(cost));
                        eViewModel.insertExpense(expense);
                        clearFields();
                    } else {
                        expenseId.setText("Incomplete information");
                    }
                }
            });

            findButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eViewModel.findExpense(expenseName.getText().toString());
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eViewModel.deleteExpense(expenseName.getText().toString());
                    clearFields();
                }
            });
        }

        return view;
    }
}
