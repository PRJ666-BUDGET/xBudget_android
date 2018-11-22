package com.prj666_183a06.xbudget;

import android.arch.lifecycle.ViewModelProviders;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseListInterface;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseObj;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;
import com.prj666_183a06.xbudget.pojo.PlanItem;
import com.prj666_183a06.xbudget.receiptocr.CameraActivity;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;
import com.prj666_183a06.xbudget.viewmodel.PlanViewModel;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class HomeFragment extends Fragment {

    // Chart instances
    private LineChart mLine;
    private HorizontalBarChart mBar;
    private Typeface tf;

    private double total_income, total_expenses, curr_balance;
    List<String> str_label;
    private List<Float> arr_plan, arr_expenses;

    // Database instances - expenses
    ExpenseViewModel expenseViewModel;
    List<ExpenseObj> expenseObjs;
    HashMap<String, Float> hashMap_exp;
    Map<String, Float> treeMap_expenses;

    // Database instances - plans
    PlanViewModel planViewModel;
    List<PlanItem> planItems;

    // View instances
    Fragment fragment;
    Locale locale;
    NumberFormat fmt;
    TextView mtvIncome, mtvSpent, mtvBalance;
    String currencyIncome, currencySpent, currencyBal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrData();
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

        // Set Currency
        locale = Locale.CANADA;
        fmt = NumberFormat.getCurrencyInstance(locale);

        // Get Data
//        total_income = 2000.00;

        // Text View & Image View
        mtvIncome = (TextView) v.findViewById(R.id.tvIncome);
        mtvSpent = (TextView) v.findViewById(R.id.tvSpent);
        mtvBalance = (TextView) v.findViewById(R.id.tvBalance);
        getImageView(v);
        getCurrData();

        // Get Bar & Line Chart
        mBar = v.findViewById(R.id.barChart1);
        mLine = v.findViewById(R.id.lineChart1);
        getBarChart();
        getLineChart();

        // Camera Button
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

        return v;
    }

    public void getCurrData(){
        // Initialize - expenses
        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        expenseObjs = new ArrayList();
        expenseObjs = expenseViewModel.getAll();
        Log.e("expenseObjs", "in getCurrData Home" + expenseObjs);
        total_expenses = expenseViewModel.getTotal();
        Log.e("total_spent", "in getCurrData Home" + total_expenses);

        // Initialize - plans
        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        total_income = planViewModel.getTotalIncome();
        Log.e("expenseObjs", "in getCurrData Home" + planItems);

        curr_balance = total_income - total_expenses;

        // Chart
        hashMap_exp = new HashMap<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        getCurrData();
        getTextView();
        getBarChart();
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

    private void getTextView() {
        // Apply the current data to textview
        currencyIncome = fmt.format(this.total_income);
        mtvIncome.setText(currencyIncome);

        currencySpent = fmt.format(this.total_expenses);
        mtvSpent.setText(currencySpent);

        currencyBal = fmt.format(this.curr_balance);
        mtvBalance.setText(currencyBal);
    }

    private void getStringLabels(){
        str_label = new ArrayList<String>();
        for(ExpenseObj r: expenseObjs) {
            str_label.add(r.getDate().substring(0,5));
        }

        Log.e("getStringLabels: ", str_label.toString());
        if (str_label.size() == 0){
            str_label.add("5");
        }
    }

    protected void getPlanData(){
        arr_plan = new ArrayList<Float>();
        arr_plan.add((float) total_income);
    }

    protected void getExpensesData(){
        PlanViewModel pvm = ViewModelProviders.of(this).get(PlanViewModel.class);
        ExpenseListInterface obj = new ExpenseListInterface(expenseViewModel, pvm);

        hashMap_exp = obj.getCostByDaily();
        Log.e("hash compare in home: ", String.valueOf(hashMap_exp));
//        treeMap_expenses = new TreeMap<String, Float>(hashMap_exp);

        // Sort by date using tree
        treeMap_expenses = new TreeMap<String, Float>(hashMap_exp);
        Log.e("tree compare in home: ", String.valueOf(treeMap_expenses));

        arr_expenses = new ArrayList<Float>();
        float value = 0;
        for(Map.Entry<String,Float> entry : treeMap_expenses.entrySet()) {
            String key = entry.getKey().substring(0,5);
            value += entry.getValue();
            arr_expenses.add(value);
//            str_label.add(key);
        }

//        Log.e("data in key: ", String.valueOf(str_label));

//        arr_expenses = new ArrayList<Float>(treeMap_expenses.values());
        Log.e("data in arr_expenses: ", String.valueOf(arr_expenses));
        Log.e("data in hashMap_exp: ", hashMap_exp.toString());

        if (arr_expenses.size() == 0){
            arr_expenses.add(0f);
//            str_label.add("5");
        }
    }

    private void getBarChart() {
        // Set Bar UI & Data
        mBar.getDescription().setEnabled(false);
        mBar.setData(generateBarData());
        mBar.setDrawGridBackground(false);

        // Set Axis
        XAxis xAxis = mBar.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);

        YAxis rightAxis = mBar.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mBar.getAxisLeft();
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);

        // Get Legend
        Legend lgdBar = mBar.getLegend();
        lgdBar.setTypeface(tf);
        lgdBar.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);

        mBar.setDrawValueAboveBar(false);
        mBar.invalidate();
    }

    private void getLineChart() {
        // Set Line UI
        mLine.getDescription().setEnabled(false);

        Legend lgdLine = mLine.getLegend();
        lgdLine.setForm(Legend.LegendForm.LINE);

        // Apply data to chart
        mLine.setData(generateLineData());
        Log.e("set in getLineChart: ", "set");

        // Set Axis
        XAxis xAxis = mLine.getXAxis();
        Log.e("set in getLineChart: ", xAxis.toString());
//        xAxis.setEnabled(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(str_label));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        Log.e("XAxis in getLineChart: ", str_label.toString());
    }

    protected BarData generateBarData() {
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        float v_expenses = (float) (total_expenses/total_income) * 100;
        float v_balance = 100 - v_expenses;

        entries.add(new BarEntry(1, new float[] {v_expenses, v_balance}, ""));

        BarDataSet ds = new BarDataSet(entries, "");
        ds.setDrawIcons(false);
        ds.setColors(MyColorTemplate.VORDIPLOM_COLORS[4], MyColorTemplate.VORDIPLOM_COLORS[3]);
        ds.setStackLabels(new String[]{"Expenses", "Balance"});

        ArrayList<IBarDataSet> iBarDataSets = new ArrayList<IBarDataSet>();
        iBarDataSets.add(ds);

        BarData barData = new BarData(ds);
        barData.setValueFormatter(new MyValueFormatter());

        return barData;
    }

    protected LineData generateLineData() {
        ArrayList<Entry> arrList_budget = new ArrayList<Entry>();
        ArrayList<Entry> arrList_expenses = new ArrayList<Entry>();

        LineDataSet budget_ds;
        LineDataSet expenses_ds;

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<ILineDataSet>();

        getPlanData();
        getExpensesData();
        getStringLabels();

        // Budget Dataset
        for(int i=1; i <= arr_expenses.size(); i++){
            arrList_budget.add(new Entry(i, arr_plan.get(0)));
        }

        // Budget Dataset
        int temp = 0;
        for(int i=1; i <= arr_expenses.size(); i++){
            arrList_expenses.add(new Entry(i, arr_expenses.get(temp)));
            temp++;
        }

        budget_ds = new LineDataSet(arrList_budget, "My Budget");
        budget_ds.setLineWidth(2f);
        budget_ds.setDrawCircles(false);
//        budget_ds.setDrawValues(false);
        budget_ds.setColor(MyColorTemplate.VORDIPLOM_COLORS[0]);

        iLineDataSets.add(budget_ds);

        expenses_ds = new LineDataSet(arrList_expenses, "My Expenses");
        expenses_ds.setLineWidth(2f);
        expenses_ds.setDrawCircles(false);
//        expenses_ds.setDrawValues(false);
        expenses_ds.setColor(MyColorTemplate.VORDIPLOM_COLORS[4]);

        iLineDataSets.add(expenses_ds);

        LineData lineData = new LineData(iLineDataSets);
        return lineData;
    }
}