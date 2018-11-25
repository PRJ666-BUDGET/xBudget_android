package com.prj666_183a06.xbudget;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FileUtils;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseObj;
import com.prj666_183a06.xbudget.ExpenseRoom.ExpenseViewModel;
import com.prj666_183a06.xbudget.model.PlanObj;
import com.prj666_183a06.xbudget.pojo.PlanItem;
import com.prj666_183a06.xbudget.viewmodel.PlanViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.github.mikephil.charting.data.PieDataSet.ValuePosition.OUTSIDE_SLICE;


public class ReportFragment extends Fragment {

    // Chart instances
    private PieChart mPie;
    private BarChart mBar;
    private Typeface tf;

    List<String> str_label_Pie, str_label_Bar;
    private List<Float> arr_plan, arr_actual, arr_actual_Bar;

    HashMap<String, Float> hashMap_expenses, hashMap_expenses_Bar;
    Map<String, Float> treeMap_expenses, treeMap_expenses_Bar;

    HashMap<String, Float> hashMap_plan;
    Map<String, Float> treeMap_plan;

    // Database instances
    ExpenseViewModel expenseViewModel;
    List<ExpenseObj> expenseObjs;

    PlanViewModel planViewModel;
    List<PlanObj> planObjs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Report");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_report, container, false);

        // View Charts
        mBar = v.findViewById(R.id.barChart2);
        mPie = v.findViewById(R.id.pieChart1);
        getPieChart();
        getBarChart();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        getPlanData();
        getActualData();
        getPieChart();
        getBarChart();
    }

    public void getCurrData() {
        // Initialize - database
        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        expenseObjs = new ArrayList();
        expenseObjs = expenseViewModel.getAll();
        Log.e("expenseObjs", "in onCreate Report" + expenseObjs);

        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        planObjs = new ArrayList();
        planObjs = planViewModel.getAll();
        Log.e("planObjs", "in onCreate Report" + planObjs);

        // Chart
        hashMap_expenses = new HashMap<>();
        treeMap_expenses = new TreeMap<String, Float>(hashMap_expenses);

        hashMap_expenses_Bar = new HashMap<>();
        treeMap_expenses_Bar = new TreeMap<String, Float>(hashMap_expenses_Bar);

        hashMap_plan = new HashMap<>();
        treeMap_plan = new TreeMap<String, Float>(hashMap_expenses);
    }

    protected void getPlanData(){
        hashMap_plan = new HashMap<>();

        // Group using hash
        for(PlanObj r: planObjs) {
            float temp = 0;
            if (str_label_Pie.contains(r.getTitle())){
                Log.e("temp in switch: ", r.getPeriod());
                switch (r.getPeriod()){
                    case "daily":
                        temp = (float) r.getAmount()*365/12;
                        break;
                    case "weekly":
                        temp = (float) r.getAmount()*52/12;
                        break;
                    case "bi-weekly":
                        temp = (float) r.getAmount()*26/12;
                        break;
                    case "monthly":
                        temp = (float) r.getAmount();
                        break;
                }
                hashMap_plan.put(r.getTitle(), temp);
            } else {
                hashMap_plan.put(r.getTitle(), 0f);
            }
            Log.e("temp in Plan: ", String.valueOf(temp));
            Log.e("hashmap in Plan: ", String.valueOf(hashMap_plan));
        }

        // Sort using treemap & Return by total desc
//        Comparator<String> comparator = new ValueComparator<String, Float>(hashMap_plan);
//        treeMap_plan = new TreeMap<String, Float>(comparator);
//        treeMap_plan.putAll(hashMap_plan);
//        Log.e("comparator in Plan: ", String.valueOf(treeMap_plan));
//
//        // Set to arrayList from treemap
//        arr_plan = new ArrayList<Float>(treeMap_plan.values());
//        str_label_Bar = new ArrayList<String>(treeMap_plan.keySet());
//        Log.e("total in getPlan: ", String.valueOf(arr_plan));
////        Log.e("label in getPlan: ", String.valueOf(str_label_Bar));
        arr_plan = new ArrayList<Float>(hashMap_plan.values());
        Log.e("hash in getPlan: ", String.valueOf(hashMap_plan));


        int temp_size = arr_plan.size();
        if (str_label_Pie.size() != temp_size){
            for(int i = 0; i < str_label_Pie.size()-temp_size; i++){
                arr_plan.add(0f);
            }
        } else {
            if (str_label_Pie.size() == 0 ) {
                arr_plan.add(100f);
            }
        }
    }

    public void getLabelsBar(){
        str_label_Pie.removeAll(str_label_Bar);
        str_label_Bar.addAll(str_label_Pie);
        Log.e("label in getPlan2: ", String.valueOf(str_label_Bar));
    }

    protected void getActualData(){
        hashMap_expenses = new HashMap<>();

        // Group using hash
        for(ExpenseObj r: expenseObjs) {
            if (!hashMap_expenses.containsKey(r.getCategory())){
                hashMap_expenses.put(r.getCategory(), (float) r.getCost());
            } else {
                hashMap_expenses.put(r.getCategory(), hashMap_expenses.get(r.getCategory()) + (float) r.getCost());
            }
        }

        Log.e("hash in Actual: ", String.valueOf(hashMap_expenses));

        // Sort using treemap & Return by total desc
        Comparator<String> comparator = new ValueComparator<String, Float>(hashMap_expenses);
        treeMap_expenses = new TreeMap<String, Float>(comparator);
        treeMap_expenses.putAll(hashMap_expenses);
        Log.e("comparator in Actual: ", String.valueOf(treeMap_expenses));

        // Set to arrayList from treemap
        arr_actual = new ArrayList<Float>(treeMap_expenses.values());
        str_label_Pie = new ArrayList<String>(treeMap_expenses.keySet());
        Log.e("total in getActual: ", String.valueOf(arr_actual));
        Log.e("label in getActual: ", String.valueOf(str_label_Pie));

        if (arr_actual.size() == 0){
            arr_actual.add(100f);
            str_label_Pie.add("Sample Data");
        }
    }

    private void getBarChart() {
        // Set bar UI
        mBar.getDescription().setEnabled(false);
        mBar.setData(generateBarData());

        // Set Axis
        XAxis xAxis = mBar.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(str_label_Pie));

        mBar.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

//        mBar.getAxis(YAxis.AxisDependency.LEFT);
        YAxis rightAxis = mBar.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);

        // Format
        float barSpace = 0.02f;
        float groupSpace = 0.38f;
        int groupCount = arr_actual.size();

        mBar.getXAxis().setAxisMinimum(0);
        mBar.getXAxis().setAxisMaximum(0 + mBar.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mBar.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping

        mBar.invalidate();
    }

    protected BarData generateBarData() {
        ArrayList<BarEntry> entries_plan = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries_actual = new ArrayList<BarEntry>();

        // Get data to chart
        getPlanData();
        getActualData();

        Log.e("data in Bar: ", Integer.toString(arr_actual.size()));

        for(int i=0; i < arr_actual.size(); i++ ){
            entries_plan.add(new BarEntry(i+1, arr_plan.get(i)));
            entries_actual.add(new BarEntry(i+1, arr_actual.get(i)));
            Log.e("act loop in bar: ", Float.toString(arr_actual.get(i)));
            Log.e("plan loop in bar: ", Float.toString(arr_plan.get(i)));
        }

        // Format Bar
        BarDataSet set1, set2;
        set1 = new BarDataSet(entries_plan, "Plan");
        set1.setColor(Color.rgb(164, 228, 251));

        set2 = new BarDataSet(entries_actual, "Actual");
        set2.setColor(Color.rgb(193, 37, 82));

        // Apply data to chart
        BarData data = new BarData(set1, set2);
        mBar.setData(data);
        data.setBarWidth(0.3f);

        return data;

    }

    private void getPieChart() {
//        mPie = v.findViewById(R.id.pieChart1);
        mPie.getDescription().setEnabled(false);

        mPie.setCenterText(generateCenterText());
        mPie.setCenterTextSize(8f);
//        mPie.setEntryLabelColor(Color.DKGRAY);
        mPie.setEntryLabelColor(Color.WHITE);

        mPie.setHoleRadius(45f);
        mPie.setTransparentCircleRadius(50f);

        Legend lgdPie = mPie.getLegend();
        lgdPie.setEnabled(false);

        mPie.setData(generatePieData());
    }

    protected PieData generatePieData() {
        ArrayList<PieEntry> entries_expenses = new ArrayList<PieEntry>();

        getActualData();

        // Apply data to chart
        Log.e("arr_actual in Pie: ", Integer.toString(arr_actual.size()));
        for(int i = 0; i < arr_actual.size(); i++){
            entries_expenses.add(new PieEntry((float) arr_actual.get(i), str_label_Pie.get(i)));
            Log.e("loop in Pie: ", Float.toString(arr_actual.get(i)));
            Log.e("loop in Pie: ", str_label_Pie.get(i));
        }

        PieDataSet ds = new PieDataSet(entries_expenses, "");
        ds.setColors(MyColorTemplate.CUTE_COLORS);
        ds.setSliceSpace(2f);
        ds.setValueTextColor(Color.WHITE);
        ds.setValueTextSize(12f);
//        ds.setValueLineColor(Color.DKGRAY);
//        ds.setXValuePosition(OUTSIDE_SLICE);

        PieData d = new PieData(ds);
        return d;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Expenses");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);

        return s;
    }

}