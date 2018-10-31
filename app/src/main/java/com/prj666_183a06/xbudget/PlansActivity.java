package com.prj666_183a06.xbudget;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prj666_183a06.xbudget.adapter.PlanAdapter;
import com.prj666_183a06.xbudget.crud.CreateUpdatePlanActivity;
import com.prj666_183a06.xbudget.crud.DetailPlanActivity;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;
import com.prj666_183a06.xbudget.viewmodel.PlanViewModel;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PlansActivity extends Fragment {

    private static final String TAG = "PlansFragment";

    public static final int ADD_PLAN_REQUEST = 1;
    public static final int DELETE_PLAN_REQUEST = 2;
    public static final int EDIT_PLAN_REQUEST = 3;

    private PlanViewModel planViewModel;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Plans");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_plans, container, false);

        FloatingActionButton buttonCreatePlan = view.findViewById(R.id.fab_add_plan);
        buttonCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viedw) {
                // TODO: 2018-10-14 I HAVE CreatePlanFragment READY, USE IT AFTER THE TUTORIAL DONE
                Intent intent = new Intent(getActivity(), CreateUpdatePlanActivity.class);
                startActivityForResult(intent, ADD_PLAN_REQUEST);
            }
        });

        setHasOptionsMenu(true);

        // WORKING NOW https://www.youtube.com/watch?v=reSPN7mgshI 14:20 COMPLETED
        RecyclerView recyclerView = view.findViewById(R.id.plan_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final PlanAdapter adapter = new PlanAdapter();
        recyclerView.setAdapter(adapter);

        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        planViewModel.getPlanList().observe(this, new Observer<List<PlanEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlanEntity> plans) {
                adapter.setPlans(plans);
                // UPDATE RecyclerView
                //Toast.makeText(getContext(),"onChanged", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         *  DELETE PLAN BY SWIPING
         *
         *  ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT)
         *  .attachToRecyclerView(recyclerView); <- MUST BE INCLUDED
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                planViewModel.delete(adapter.getPlanPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(),"Plan deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        adapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PlanEntity plan) {
                Intent intent = new Intent(getActivity(), DetailPlanActivity.class);

                intent.putExtra(DetailPlanActivity.PLAN_ID, plan.getPlanId());
                intent.putExtra(DetailPlanActivity.PLAN_TYPE, plan.getPlanType());
                intent.putExtra(DetailPlanActivity.PLAN_TITLE, plan.getPlanTitle());
                intent.putExtra(DetailPlanActivity.PLAN_AMOUNT, plan.getPlanAmount());
                intent.putExtra(DetailPlanActivity.PLAN_PERIOD, plan.getPlanPeriod());
                Log.d(TAG, "onItemClick: PLAN_ID:" + plan.getPlanId() + "------------------------------------");
                startActivityForResult(intent, DELETE_PLAN_REQUEST);
            }
        });

        return view;
    }

    /**
     *  DATA RETRIEVES FROM CreateUpdatePlanActivity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PLAN_REQUEST && resultCode == RESULT_OK) {
            String type = data.getStringExtra(CreateUpdatePlanActivity.PLAN_TYPE);
            String title = data.getStringExtra(CreateUpdatePlanActivity.PLAN_TITLE);
            double amount = data.getDoubleExtra(CreateUpdatePlanActivity.PLAN_AMOUNT, 0.00);
            String period = data.getStringExtra(CreateUpdatePlanActivity.PLAN_PERIOD);

            PlanEntity plan = new PlanEntity(type ,title, amount, period);
            planViewModel.insert(plan);

            Toast.makeText(getActivity(), "Plan is created.", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == DELETE_PLAN_REQUEST && resultCode == RESULT_OK) {
            String type = data.getStringExtra(CreateUpdatePlanActivity.PLAN_TYPE);
            String title = data.getStringExtra(CreateUpdatePlanActivity.PLAN_TITLE);
            double amount = data.getDoubleExtra(CreateUpdatePlanActivity.PLAN_AMOUNT, 0.00);
            String period = data.getStringExtra(CreateUpdatePlanActivity.PLAN_PERIOD);

            int id = data.getIntExtra(CreateUpdatePlanActivity.PLAN_ID, -1);

            Log.d(TAG, "onActivityResult: planId: " + id);

            if (id == -1) {
                Toast.makeText(getActivity(), "BAD REQUEST [PlansActivity.java onActivityResult()], PlanId: " + id, Toast.LENGTH_SHORT).show();
                return;
            }

            PlanEntity plan = new PlanEntity(type ,title, amount, period);

            plan.setPlanId(id);
            planViewModel.delete(plan);
        }
        else{
//            Toast.makeText(getActivity(), "BACK TO LIST VIEW FROM DETAIL VIEW [PlansActivity.java onActivityResult()]", Toast.LENGTH_SHORT).show();
        }
    }

//        /**
//         * DEMO DATA
//         */
//
//        ArrayList<Object> planItems = new ArrayList<>();
//        planItems.add(new String("Budget"));
//        planItems.add(new PlanItem("budget", "Starbucks", "1000", "bi-weekly"));
//        planItems.add(new PlanItem("budget", "Tutor", "400", "bi-weekly"));
//
//        planItems.add(new String("Saving"));
//        planItems.add(new PlanItem("saving", "Coffee", "5", "daily"));
//        planItems.add(new PlanItem("saving", "Lunch", "10", "daily"));
//        planItems.add(new PlanItem("saving", "Cigarette", "15", "weekly"));
//

    /**
     *  DELETE ALL PLANS FOR DEMO ONLY
     *
     *  onCreateOptionsMenu(Menu menu, MenuInflater inflater)
     *  onOptionsItemSelected(MenuItem item)
     *
     *  setHasOptionsMenu(true); <- MUST BE INCLUDED IN onCreateView()
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.listview_plan_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_allPlan:
                planViewModel.deleteAllPlans();
                Toast.makeText(getActivity(), "All Plans are deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
