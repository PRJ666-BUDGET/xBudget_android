package com.prj666_183a06.xbudget;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prj666_183a06.xbudget.ExpenseRoom.Expense;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseRepository;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;
import com.prj666_183a06.xbudget.database.Expenses;
import com.prj666_183a06.xbudget.database.PlanRepository;
import com.prj666_183a06.xbudget.database.Plans;
import com.prj666_183a06.xbudget.receiptocr.CameraActivity;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private LineChart mLine;
    private HorizontalBarChart mBar;
    private Typeface tf;

    private double mIncome, mSpent, mBalance, mAccSpent;
    List<String> str_label;
    private List<Float> arr_spent = new ArrayList<Float>();
    ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
    public List<Float> expense_arr = new ArrayList<Float>();

    Fragment fragment;
    Locale locale;
    NumberFormat fmt;

    TextView mtvIncome, mtvSpent, mtvBalance;
    String currencyIncome, currencySpent, currencyBal;

    private DatabaseReference planRef = FirebaseDatabase.getInstance().getReference("plans");
    private DatabaseReference expenseRef = FirebaseDatabase.getInstance().getReference("expenses");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSpentData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("xBudget");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);

        PlanRepository planRepo = new PlanRepository(getActivity().getApplication());
        planRepo.getPlanIncomeTotalFromHomeFragment(this);

        ExpenseRepository expRepo = new ExpenseRepository(getActivity().getApplication());
        expRepo.getExpenseTotalFromHomeFragment(this);
//        expRepo.getAccExpenseTotalFromHomeFragment(this);

        // Set Currency
        locale = Locale.CANADA;
        fmt = NumberFormat.getCurrencyInstance(locale);

//        planRef.addValueEventListener(new ValueEventListener() {
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mIncome = 0;
//                for (DataSnapshot planData : dataSnapshot.getChildren()) {
//                    Plans planValue = planData.getValue(Plans.class);
//                    if(planValue.getPlan_type().equals("income")){
//                        mIncome += planValue.getPlan_amount();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                System.out.println("The read failed!!!");
//            }
//        });
//
//        expenseRef.addValueEventListener(new ValueEventListener() {
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mSpent = 0;
//                for (DataSnapshot planData : dataSnapshot.getChildren()) {
//                    Expenses expensesValue = planData.getValue(Expenses.class);
//                    mSpent += expensesValue.getExpenseCost();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                System.out.println("The read failed!!!");
//            }
//        });

        // Text View & Image View
        getTextView(v);
        getImageView(v);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent openCamera = new Intent(getActivity(), CameraActivity.class);
                startActivity(openCamera);
            }
        });

        // Get Bar & Line Chart
        mBar = v.findViewById(R.id.barChart1);
        mLine = v.findViewById(R.id.lineChart1);
        getBarChart();
        getLineChart();

        return v;
    }

    private void getImageView(View v) {
        ImageView mImgIncome, mImgSpent, mImgBal;

        mImgIncome = (ImageView) v.findViewById(R.id.img_income);
        mImgSpent = (ImageView) v.findViewById(R.id.img_spent);
        mImgBal = (ImageView) v.findViewById(R.id.img_balance);

        // Direct to other views
        mImgIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new PlansFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        mImgSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ExpenseActivity();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        mImgBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ReportFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private void getTextView(View v) {
        mtvIncome = (TextView) v.findViewById(R.id.tvIncome);
        mtvSpent = (TextView) v.findViewById(R.id.tvSpent);
        mtvBalance = (TextView) v.findViewById(R.id.tvBalance);
    }

    private void getStringLabels(){
        str_label = new ArrayList<String>();
        str_label.add("1");

        expenseRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                str_label.clear();
                for (DataSnapshot planData : dataSnapshot.getChildren()) {
                    Expenses expensesValue = planData.getValue(Expenses.class);
                    str_label.add(expensesValue.getExpenseDate());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed!!!");
            }
        });

        str_label.add("5");
        str_label.add("10");
        str_label.add("15");
        str_label.add("20");
        str_label.add("25");
        str_label.add("30");
    }

    protected void getSpentData(){
        expenseRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                arr_spent.clear();
                mAccSpent = 0;
                for (DataSnapshot planData : dataSnapshot.getChildren()) {
                    Expenses expensesValue = planData.getValue(Expenses.class);
                    mAccSpent += expensesValue.getExpenseCost();
                    arr_spent.add((float) mAccSpent);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("The read failed!!!");
            }
        });

        if (arr_spent.size() == 0) {
            arr_spent.add(0f);
            arr_spent.add(220f);
            arr_spent.add(530f);
            arr_spent.add(700f);
            arr_spent.add(1070f);
        }
    }

    private void getBarChart() {
        mBar.getDescription().setEnabled(false);

        mBar.setData(generateBarData());
        mBar.setDrawGridBackground(false);

        XAxis xAxis = mBar.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);
        xAxis.setEnabled(false);

        YAxis rightAxis = mBar.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mBar.getAxisLeft();
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);

        Legend lgdBar = mBar.getLegend();
        lgdBar.setEnabled(false);
//        lgdBar.setTypeface(tf);
//        lgdBar.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);

        mBar.setDrawValueAboveBar(false);
        mBar.invalidate();
    }

    private void getLineChart() {
        mLine.getDescription().setEnabled(false);

        Legend lgdLine = mLine.getLegend();
        lgdLine.setForm(Legend.LegendForm.LINE);

        mLine.setData(generateLineData());
    }

    protected BarData generateBarData() {
        float v_expenses = (float) (mSpent/mIncome * 100);
        float v_balance = 100 - v_expenses;

        entries.add(new BarEntry(1, new float[] {v_expenses, v_balance}, ""));

        BarDataSet ds = new BarDataSet(entries, "");
        ds.setDrawIcons(false);
        ds.setColors(MyColorTemplate.VORDIPLOM_COLORS[4], MyColorTemplate.VORDIPLOM_COLORS[3]);
        ds.setStackLabels(new String[]{"Expenses", "Balance"});
        ds.setDrawValues(false);

        ArrayList<IBarDataSet> iBarDataSets = new ArrayList<IBarDataSet>();
        iBarDataSets.add(ds);

        BarData barData = new BarData(ds);
        barData.setValueFormatter(new MyValueFormatter());

        return barData;
    }

    protected LineData generateLineData() {
        PlanEntity plan = new PlanEntity("income" ,"", 0, "bi-weekly");

        ArrayList<Entry> budget_arrList = new ArrayList<Entry>();
        ArrayList<Entry> expenses_arrList = new ArrayList<Entry>();
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<ILineDataSet>();

        LineDataSet budget_ds;
        LineDataSet expenses_ds;

        getSpentData();
        getStringLabels();

        for(int i=1; i < 31; i+=7){
            budget_arrList.add(new Entry(i, (float) mIncome));
        }

        int temp = 0;

        expenses_arrList.add(new Entry(0, 0f));

        for(int i=1; i <= arr_spent.size(); i++){
            expenses_arrList.add(new Entry(i, arr_spent.get(temp)));
            temp++;
        }

//        if (expense_arr.size() > 0) {
//            for(int i=1; i <= expense_arr.size(); i++){
//                expenses_arrList.add(new Entry(i, expense_arr.get(temp)));
//                temp++;
//            }
//        }

        budget_ds = new LineDataSet(budget_arrList, "My Budget");
        budget_ds.setLineWidth(2f);
        budget_ds.setDrawCircles(false);
        budget_ds.setColor(MyColorTemplate.VORDIPLOM_COLORS[0]);

        iLineDataSets.add(budget_ds);

        expenses_ds = new LineDataSet(expenses_arrList, "My Expenses");
        expenses_ds.setLineWidth(2f);
        expenses_ds.setDrawCircles(false);
        expenses_ds.setColor(MyColorTemplate.VORDIPLOM_COLORS[4]);

        iLineDataSets.add(expenses_ds);

        LineData lineData = new LineData(iLineDataSets);
        return lineData;
    }

    public void getTotalIncome(Double aDouble) {
        mIncome = aDouble;
//        Toast.makeText(getActivity(), Double.toString(mIncome), Toast.LENGTH_SHORT).show();
        currencyIncome = fmt.format(this.mIncome);
        mtvIncome.setText(currencyIncome);
        updateBalance();
    }

    public void getTotalExpenses(Double aDouble) {
        mSpent = aDouble;
//        Toast.makeText(getActivity(), Double.toString(mSpent), Toast.LENGTH_SHORT).show();
        currencySpent = fmt.format(this.mSpent);
        mtvSpent.setText(currencySpent);

        if (aDouble > 0) {
            expense_arr.add((float)mSpent);
        } else {
            expense_arr.clear();
        }
        updateBalance();
    }

    public void updateBalance() {
        mBalance = mIncome - mSpent;
        currencyBal = fmt.format(mBalance);
//        Toast.makeText(getActivity(), Double.toString(mBalance), Toast.LENGTH_SHORT).show();
        mtvBalance.setText(currencyBal);
        getBarChart();
        getLineChart();
    }

    public void getExpensesList(List<Expense> expList){

    }
}
