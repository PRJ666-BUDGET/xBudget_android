package com.prj666_183a06.xbudget;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prj666_183a06.xbudget.Adapter.PlanAdapter;
import com.prj666_183a06.xbudget.Local.PlanDatabase;
import com.prj666_183a06.xbudget.Model.Plan;
import com.prj666_183a06.xbudget.Plan.CreatePlan;

import java.util.List;

public class PlansActivity extends Fragment {

    private static final String TAG = "PlansActivity";

//    private Button btnAddPlan;
    private FloatingActionButton fabAddPlan;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Plans");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_plans, container, false);


//        String[] plansItems = {"this", "that", "haaa"};
//
//        ListView listView = (ListView) view.findViewById(R.id.plansListView);
//
//        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
//                getActivity(),
//                android.R.layout.simple_list_item_1,
//                plansItems
//        );
//
//        listView.setAdapter(listViewAdapter);

        ListView listView = (ListView) view.findViewById(R.id.plansListView);

        /**
         * DEMO DATA
         */

        // DO THIS IN BACKGROUND THREAD
        final PlanDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), PlanDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        List<Plan> plans = db.planDAO().getAllPlans();

//        ArrayList<Object> planItems = new ArrayList<>();
//        planItems.add(new String("Budget"));
//        planItems.add(new PlanItem("budget", "Co-op", "1000", "bi-weekly"));
//        planItems.add(new PlanItem("budget", "Tutor", "400", "bi-weekly"));
//
//        planItems.add(new String("Saving"));
//        planItems.add(new PlanItem("saving", "Coffee", "5", "daily"));
//        planItems.add(new PlanItem("saving", "Lunch", "10", "daily"));
//        planItems.add(new PlanItem("saving", "Cigarette", "15", "weekly"));

        listView.setAdapter(new PlanAdapter(getContext(), plans));

        fabAddPlan = view.findViewById(R.id.fab_addPlan);

        fabAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: 2018-10-08 Open Plan Create Form

                Fragment fragment = new CreatePlan();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        return view;
    }

//    @Override
//    public void onClick(View v) {
//
//        Log.d(TAG, "onClick: ");
//    }
}
