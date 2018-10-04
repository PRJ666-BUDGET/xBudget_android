package com.prj666_183a06.xbudget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prj666_183a06.xbudget.adapter.PlanAdapter;
import com.prj666_183a06.xbudget.pojo.PlanItem;

import java.util.ArrayList;

public class PlansActivity extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Plans");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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

        ArrayList<Object> planItems = new ArrayList<>();
        planItems.add(new String("Budget"));
        planItems.add(new PlanItem("budget", "Co-op", "1000", "bi-weekly"));
        planItems.add(new PlanItem("budget", "Tutor", "400", "bi-weekly"));

        planItems.add(new String("Saving"));
        planItems.add(new PlanItem("saving", "Coffee", "5", "daily"));
        planItems.add(new PlanItem("saving", "Lunch", "10", "daily"));
        planItems.add(new PlanItem("saving", "Cigarette", "15", "weekly"));

        listView.setAdapter(new PlanAdapter(getContext(), planItems));

        return view;
    }
}
