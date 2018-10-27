package com.prj666_183a06.xbudget.crud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.prj666_183a06.xbudget.R;

public class CreateUpdatePlanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "CreateUpdatePlanActivity";

    public static final String PLAN_ID = "com.prj666_183a06.xbudget.PLAN_ID";
    public static final String PLAN_TYPE = "com.prj666_183a06.xbudget.PLAN_TYPE";
    public static final String PLAN_TITLE = "com.prj666_183a06.xbudget.PLAN_TITLE";
    public static final String PLAN_AMOUNT = "com.prj666_183a06.xbudget.PLAN_AMOUNT";
    public static final String PLAN_PERIOD = "com.prj666_183a06.xbudget.PLAN_PERIOD";

    // TODO: 2018-10-14 MUST INCLUDE PLAN_TYPE & PLAN_PERIOD WITH DROPDOWN COMPONENTS
    private EditText editTextTitle;
    private EditText editTextAmount;
    private Spinner spinnerType;
    private Spinner spinnerPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        editTextTitle = findViewById(R.id.editText_plaTitle);
        editTextAmount = findViewById(R.id.editText_planAamount);
        spinnerType = findViewById(R.id.spinner_planType);
        spinnerPeriod = findViewById(R.id.spinner_planPeriod);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_planType, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        spinnerType.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_planPeriod, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(periodAdapter);
        spinnerPeriod.setOnItemSelectedListener(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(PLAN_ID)) {
            setTitle("Update Plan");
            editTextTitle.setText(intent.getStringExtra(PLAN_TITLE));
            editTextAmount.setText(String.valueOf(intent.getDoubleExtra(PLAN_AMOUNT, 0.00)));
            spinnerType.setSelection(getIndex(spinnerType, intent.getStringExtra(PLAN_TYPE)));
            spinnerPeriod.setSelection(getIndex(spinnerPeriod, intent.getStringExtra(PLAN_PERIOD)));
        } else {
            setTitle("Create Plan");
        }
    }

    private void savePlan() {
        String title = editTextTitle.getText().toString();
        double amount = Double.parseDouble(editTextAmount.getText().toString());

        // Input validation
        if (title.trim().isEmpty() || editTextAmount.getText().toString().isEmpty()) {
            Toast.makeText(this, "Incomplete create form", Toast.LENGTH_SHORT).show();
            return;
        } else if (amount > 10000) {

            Toast.makeText(this, "Plan amount cannot be greater than $10000", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(PLAN_TYPE, spinnerType.getSelectedItem().toString());
        data.putExtra(PLAN_TITLE, title);
        data.putExtra(PLAN_AMOUNT, amount);
        data.putExtra(PLAN_PERIOD, spinnerPeriod.getSelectedItem().toString());

        int planId = getIntent().getIntExtra(PLAN_ID, -1);

        if (planId != -1) {
            data.putExtra(PLAN_ID, planId);
            Toast.makeText(this, "PLAN ID: " + planId + " IS UPDATED.", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_plan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_plan:
                savePlan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }
}
