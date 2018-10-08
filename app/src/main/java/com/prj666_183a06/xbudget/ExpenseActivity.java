package com.prj666_183a06.xbudget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ExpenseActivity extends Fragment {
    EditText storeInput, dateInput, itemInput, costInput;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Expense");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_expense, container, false);

        Button button2 = view.findViewById(R.id.submitButton);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                storeInput = getActivity().findViewById(R.id.storeText);
                dateInput = getActivity().findViewById(R.id.dateText);
                itemInput = getActivity().findViewById(R.id.itemText);
                costInput = getActivity().findViewById(R.id.costText);

                Intent intent = new Intent(getActivity(), ExpenseCreateActivity.class);

                intent.putExtra("storeExtra", storeInput.getText().toString());
                intent.putExtra("dateExtra", dateInput.getText().toString());
                intent.putExtra("itemExtra", itemInput.getText().toString());
                intent.putExtra("costExtra", costInput.getText().toString());

                startActivity(intent);
            }
        });
        return view;
    }
}
