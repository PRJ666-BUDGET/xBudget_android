package com.prj666_183a06.xbudget.crud;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.prj666_183a06.xbudget.R;

public class DetailPlanActivity extends AppCompatActivity {

    private static final String TAG = "DetailPlanActivity";

    public static final String PLAN_ID = "com.prj666_183a06.xbudget.PLAN_ID";
    public static final String PLAN_TYPE = "com.prj666_183a06.xbudget.PLAN_TYPE";
    public static final String PLAN_TITLE = "com.prj666_183a06.xbudget.PLAN_TITLE";
    public static final String PLAN_AMOUNT = "com.prj666_183a06.xbudget.PLAN_AMOUNT";
    public static final String PLAN_PERIOD = "com.prj666_183a06.xbudget.PLAN_PERIOD";

    private TextView typeRecord;
    private TextView titleRecord;
    private TextView amountRecord;
    private TextView periodRecord;

    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plan);

        typeRecord = findViewById(R.id.textView_typeRecord);
        titleRecord = findViewById(R.id.textView_titleRecord);
        amountRecord = findViewById(R.id.textView_amountRecord);
        periodRecord = findViewById(R.id.textView_periodRecord);

        Intent intent = getIntent();

        typeRecord.setText(intent.getStringExtra(PLAN_TYPE));
        titleRecord.setText(intent.getStringExtra(PLAN_TITLE));
        amountRecord.setText(String.valueOf(intent.getDoubleExtra(PLAN_AMOUNT, 0.00)));
        periodRecord.setText(intent.getStringExtra(PLAN_PERIOD));


        Toast.makeText(this, "Plan is loaded.", Toast.LENGTH_SHORT).show();
    }

    /**
     * MENU ON CREATE VIEW
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_plan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_plan:

                Intent data = new Intent();
                data.putExtra(PLAN_TYPE, typeRecord.getText().toString());
                data.putExtra(PLAN_TITLE, titleRecord.getText().toString());
                data.putExtra(PLAN_AMOUNT, Double.parseDouble(amountRecord.getText().toString()));
                data.putExtra(PLAN_PERIOD, periodRecord.getText().toString());

                int planId = getIntent().getIntExtra(PLAN_ID, -1);

                if (planId != -1) {
                    data.putExtra(PLAN_ID, planId);
                }

                setResult(RESULT_OK, data);
                Toast.makeText(this, "Plan is deleted", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.update_plan:

                Intent intent = getIntent();

                int itemId = intent.getIntExtra(PLAN_ID, -1);

                if (itemId == -1) {
                    Toast.makeText(this, "Plan cannot be updated.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Intent newIntent = new Intent(this, CreateUpdatePlanActivity.class);

                newIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                newIntent.putExtra(CreateUpdatePlanActivity.PLAN_ID, getId());
                newIntent.putExtra(CreateUpdatePlanActivity.PLAN_TYPE, typeRecord.getText().toString());
                newIntent.putExtra(CreateUpdatePlanActivity.PLAN_TITLE, titleRecord.getText().toString());
                newIntent.putExtra(CreateUpdatePlanActivity.PLAN_AMOUNT, Double.parseDouble(amountRecord.getText().toString()));
                newIntent.putExtra(CreateUpdatePlanActivity.PLAN_PERIOD, periodRecord.getText().toString());
                this.finish();
                startActivity(newIntent);
                Toast.makeText(this, "UPDATE", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
