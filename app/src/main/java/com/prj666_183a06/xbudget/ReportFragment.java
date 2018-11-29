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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import static com.github.mikephil.charting.data.PieDataSet.ValuePosition.OUTSIDE_SLICE;


public class ReportFragment extends Fragment {

    // Chart instances
    private PieChart mPie;
    private BarChart mBar;
    private Typeface tf;


    List<String> str_label_Pie, str_label_Bar;
    private List<Float> arr_plan, arr_actual, arr_actual_Bar;

    HashMap<String, Float> hashMap_expenses, hashMap_expenses_Bar, hashMap_plan;
    Map<String, Float> treeMap_expenses, treeMap_expenses_Bar, treeMap_plan;

    // Database instances
    ExpenseViewModel expenseViewModel;
    List<ExpenseObj> expenseObjs;

    PlanViewModel planViewModel;
    List<PlanObj> planObjs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getCurrData();

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

        getDataForBar();
        getActualData();
        getPieChart();
        getBarChart();
    }

    public void getCurrData() {
        // Initialize - database
        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        expenseObjs = new ArrayList();
        expenseObjs = expenseViewModel.getAll();
        Log.d("expenseObjs", "in onCreate Report" + expenseObjs);

        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        planObjs = new ArrayList();
        planObjs = planViewModel.getAll();
        Log.d("planObjs", "in onCreate Report" + planObjs);

        // Chart
        hashMap_expenses = new HashMap<>();
        treeMap_expenses = new TreeMap<String, Float>(hashMap_expenses);

        hashMap_expenses_Bar = new HashMap<>();
        treeMap_expenses_Bar = new TreeMap<String, Float>(hashMap_expenses_Bar);

        hashMap_plan = new HashMap<>();
        treeMap_plan = new TreeMap<String, Float>(hashMap_expenses);
    }

    public String getLastDate() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String lastDate;

        for (int i = 0; i < 31; i++) {
            if (day != 1) {
                day--;
            } else {
                if (month != 1) {
                    month--;
                    day = 31;
                } else {
                    year--;
                    month = 12;
                    day = 31;
                }
            }
        }

        if (day < 10) {
            lastDate = month + "/0" + day + "/" + year;
        } else {
            lastDate = month + "/" + day + "/" + year;
        }

        lastDate = month + "/" + day + "/" + year;
        return lastDate;
    }

    //Convert to date
    public Date convertDate(String s, Date d) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            d = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d;
    }

    //current
    public String getCurrentDate(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);

        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;

        return month + "/" + day  + "/" + year;
    }

    public String getLastDay(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -30);

        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;

        return month + "/" + day  + "/" + year;
    }

    //This is to compare
    //Old
    /*
    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (day < 31) {
            return month + "/" + day + "/" + year;
        } else {
            if (month < 12) {
                return month + 1 + "/" + 1 + "/" + year;
            } else {
                return "1/1/" + year + 1;
            }
        }
    }
    */


    protected void getDataForBar() {
        hashMap_plan = new HashMap<>();
        str_label_Bar = new ArrayList<String>();

        double total_expenses = expenseViewModel.getTotal();
        double total_income = planViewModel.getTotalIncome();
        double balance = total_income - total_expenses;

        for (PlanObj r : planObjs) {
            float temp = 0;
            switch (r.getPeriod()) {
                case "daily":
                    temp = (float) r.getAmount() * 365 / 12;
                    break;
                case "weekly":
                    temp = (float) r.getAmount() * 52 / 12;
                    break;
                case "bi-weekly":
                    temp = (float) r.getAmount() * 26 / 12;
                    break;
                case "monthly":
                    temp = (float) r.getAmount();
                    break;
            }
            balance -= temp;
            if (hashMap_plan.containsKey(r.getTitle())) {
                hashMap_plan.put(r.getTitle(), hashMap_plan.get(r.getTitle()) + temp);
            } else {
                hashMap_plan.put(r.getTitle(), temp);
            }
        }

        // Get the balance
        hashMap_plan.put("Other", (float) balance);
        Map<String, Float> treeMap_plan = new TreeMap<>(hashMap_plan);
        Log.d("treeMap_plan: ", String.valueOf(treeMap_plan));

        // Change to array for chart
        arr_plan = new ArrayList<Float>(treeMap_plan.values());
        str_label_Bar = new ArrayList<String>(treeMap_plan.keySet());
        hashMap_expenses_Bar = new HashMap<>();

        //String lastDate = getLastDate();


        Date currentDate, lastDate, objDate;

        currentDate = new Date();
        lastDate = new Date();
        objDate = new Date();

        currentDate = convertDate(getCurrentDate(), currentDate);
        lastDate = convertDate(getLastDay(), lastDate);

        // Group using hash
        for (ExpenseObj r : expenseObjs) {

            //Add code to get current date of object
            objDate = convertDate(r.getDate(), objDate);
            if ((objDate.after(lastDate) || objDate.equals(lastDate)) && objDate.before(currentDate)) {
                if (str_label_Bar.contains(r.getCategory())) {
                    if (!hashMap_expenses_Bar.containsKey(r.getCategory())) {
                        hashMap_expenses_Bar.put(r.getCategory(), (float) r.getCost());
                    } else {
                        hashMap_expenses_Bar.put(r.getCategory(), hashMap_expenses_Bar.get(r.getCategory()) + (float) r.getCost());
                    }
                } else if (r.getCategory().equals("None")) {
                    if (!hashMap_expenses_Bar.containsKey("Other")) {
                        hashMap_expenses_Bar.put("Other", (float) r.getCost());
                    } else {
                        hashMap_expenses_Bar.put("Other", hashMap_expenses_Bar.get("Other") + (float) r.getCost());
                    }
                }
            }
        }

        for (String s : str_label_Bar) {
            if (!hashMap_expenses_Bar.containsKey(s)) {
                hashMap_expenses_Bar.put(s, (float) 0f);
            }
        }

        Map<String, Float> treeMap_expenses_bar = new TreeMap<>(hashMap_expenses_Bar);
        Log.d("treeMap_expenses_bar: ", String.valueOf(treeMap_expenses_bar));

        // Change to array for chart
        arr_actual_Bar = new ArrayList<Float>(treeMap_expenses_bar.values());
    }

    protected void getActualData() {
        hashMap_expenses = new HashMap<>();

        Date objDate = new Date();
        Date lastDate = new Date();
        Date currentDate = new Date();

        lastDate = convertDate(getLastDate(), lastDate);
        currentDate = convertDate(getCurrentDate(), currentDate);

        // Group using hash
        for (ExpenseObj r : expenseObjs) {

            objDate = convertDate(r.getDate(), objDate);

            //if (r.getDate().compareTo(lastDate) >= 0) {
            if((objDate.after(lastDate) || objDate.equals(lastDate)) && objDate.before(currentDate)){
                if (!hashMap_expenses.containsKey(r.getCategory())) {
                    hashMap_expenses.put(r.getCategory(), (float) r.getCost());
                } else {
                    hashMap_expenses.put(r.getCategory(), hashMap_expenses.get(r.getCategory()) + (float) r.getCost());
                }
            }
        }

        Log.d("hash in Actual: ", String.valueOf(hashMap_expenses));

        // Sort using treemap & Return by total desc
//        Comparator<String> comparator = new ValueComparator<String, Float>(hashMap_expenses);
//        treeMap_expenses = new TreeMap<String, Float>(comparator);
//        treeMap_expenses.putAll(hashMap_expenses);
//        Log.d("comparator in Actual: ", String.valueOf(treeMap_expenses));

        // Set to arrayList from treemap
//        arr_actual = new ArrayList<Float>(treeMap_expenses.values());
//        str_label_Pie = new ArrayList<String>(treeMap_expenses.keySet());
        arr_actual = new ArrayList<Float>(hashMap_expenses.values());
        str_label_Pie = new ArrayList<String>(hashMap_expenses.keySet());
        Log.d("total in getActual: ", String.valueOf(arr_actual));
        Log.d("label in getActual: ", String.valueOf(str_label_Pie));

        if (arr_actual.size() == 0) {
            arr_actual.add(1f);
            str_label_Pie.add("No data");
        }
    }

    private void getBarChart() {
        // Set bar UI
        mBar.getDescription().setEnabled(false);
        mBar.setData(generateBarData());

        // Set Axis
        XAxis xAxis = mBar.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(str_label_Bar));

        mBar.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        YAxis rightAxis = mBar.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);

        // Format
        float barSpace = 0.02f;
        float groupSpace = 0.38f;

        // Display the num of arr_actual_Bar
        int groupCount = arr_actual_Bar.size();

        mBar.getXAxis().setAxisMinimum(0);
        mBar.getXAxis().setAxisMaximum(0 + mBar.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mBar.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping

        mBar.invalidate();
    }

    protected BarData generateBarData() {
        ArrayList<BarEntry> entries_plan = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries_actual = new ArrayList<BarEntry>();

        // Get data to chart
        getDataForBar();
        getActualData();

        Log.d("data in Bar: ", Integer.toString(arr_actual_Bar.size()));

        for (int i = 0; i < arr_actual_Bar.size(); i++) {
            entries_plan.add(new BarEntry(i + 1, arr_plan.get(i)));
            entries_actual.add(new BarEntry(i + 1, arr_actual_Bar.get(i)));
            Log.d("act loop in bar: ", Float.toString(arr_actual_Bar.get(i)));
            Log.d("plan loop in bar: ", Float.toString(arr_plan.get(i)));
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
        mPie.getDescription().setEnabled(false);

//        mPie.setCenterText(generateCenterText());
//        mPie.setCenterTextSize(8f);
        mPie.setEntryLabelColor(Color.DKGRAY);

        mPie.setHoleRadius(25f);
        mPie.setTransparentCircleRadius(50f);

        Legend lgdPie = mPie.getLegend();
        lgdPie.setEnabled(false);

        mPie.setData(generatePieData());

    }

    protected PieData generatePieData() {
        ArrayList<PieEntry> entries_expenses = new ArrayList<PieEntry>();

        getActualData();

        // Apply data to chart
        Log.d("arr_actual in Pie: ", Integer.toString(arr_actual.size()));
        for (int i = 0; i < arr_actual.size(); i++) {
            entries_expenses.add(new PieEntry((float) arr_actual.get(i), str_label_Pie.get(i)));
            Log.d("loop in Pie: ", Float.toString(arr_actual.get(i)));
            Log.d("loop in Pie: ", str_label_Pie.get(i));
        }

        PieDataSet ds = new PieDataSet(entries_expenses, "");
        ds.setColors(MyColorTemplate.CUTE_COLORS);
        ds.setSliceSpace(2f);
        ds.setValueTextColor(Color.DKGRAY);
        ds.setValueTextSize(12f);
        ds.setValueLineColor(Color.DKGRAY);
        ds.setXValuePosition(OUTSIDE_SLICE);
        ds.setValueLinePart1OffsetPercentage(65f);

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