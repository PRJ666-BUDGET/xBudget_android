package com.prj666_183a06.xbudget.Plan;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.prj666_183a06.xbudget.Local.PlanDatabase;
import com.prj666_183a06.xbudget.Model.Plan;
import com.prj666_183a06.xbudget.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CreatePlan extends Fragment {

    private static final String TAG = "CreatePlan";

    private EditText planTitle;
    private EditText planAmount;
    private Button planSave;

//    PlanDatabase db = PlanDatabase();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Plans");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_add_plan, container, false);

        planTitle = view.findViewById(R.id.plan_title);
        planAmount = view.findViewById(R.id.plan_amount);
        planSave = view.findViewById(R.id.button_planSave);

        // DO THIS IN BACKGROUND THREAD
        final PlanDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), PlanDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        planSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: 2018-10-08 Save data in Room & Back to Plan ListView

                Log.d(TAG, "onClick: planTitle = " + planTitle.getText().toString());

                Plan plan = new Plan("budget", planTitle.getText().toString(), Double.parseDouble(planAmount.getText().toString()), "bi-weekly");
                db.planDAO().insertPlan(plan);


                // Dismiss Soft Keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                getActivity().getSupportFragmentManager().popBackStack();


//                getFragmentManager().popBackStack();

//                Intent intent = new Intent(view.getContext(), PlansActivity.class);
//                view.getContext().startActivity(intent);
//                getActivity().finish();
            }
        });

        return view;
    }


}
