package com.prj666_183a06.xbudget;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.data.PieDataSet.ValuePosition.OUTSIDE_SLICE;


public class ReportActivity extends Fragment {

    private PieChart mPie;
    private BarChart mBar;

    private Typeface tf;

    List<String> str_label;
    private final int count_Category = 10;
    private List<Float> arr_plan;
    private List<Float> arr_actual;


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
        getPieChart(v);
        getBarChart(v);

        return v;
    }

    private void getStringLabels(){
        str_label = new ArrayList<String>();
        str_label.add("Grocery");
        str_label.add("TTC");
        str_label.add("School");
        str_label.add("Clothes");
    }

    protected void getPlanData(){

        arr_plan = new ArrayList<Float>();

        arr_plan.add(120f);
        arr_plan.add(75f);
        arr_plan.add(55f);
        arr_plan.add(164f);
    }

    protected void getActualData(){
        arr_actual = new ArrayList<Float>();

        arr_actual.add(120f);
        arr_actual.add(80f);
        arr_actual.add(20f);
        arr_actual.add(20f);
    }

    private void getBarChart(View v) {
        mBar = v.findViewById(R.id.barChart2);
        mBar.getDescription().setEnabled(false);

        mBar.setData(generateBarData());

        XAxis xAxis = mBar.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(str_label));
        mBar.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
        float barSpace = 0.02f;
        float groupSpace = 0.38f;
        int groupCount = 4;

        mBar.getXAxis().setAxisMinimum(0);
        mBar.getXAxis().setAxisMaximum(0 + mBar.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mBar.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping

        mBar.invalidate();
    }

    protected BarData generateBarData() {
        ArrayList<BarEntry> entries_plan = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries_actual = new ArrayList<BarEntry>();

        getPlanData();
        getActualData();
        getStringLabels();

        for(int i=0; i < arr_plan.size(); i++ ){
            entries_plan.add(new BarEntry(i+1, arr_plan.get(i)));
            entries_actual.add(new BarEntry(i+1, arr_actual.get(i)));
        }

        BarDataSet set1, set2;
        set1 = new BarDataSet(entries_plan, "Plan");
        set1.setColor(Color.rgb(164, 228, 251));

        set2 = new BarDataSet(entries_actual, "Actual");
        set2.setColor(Color.rgb(193, 37, 82));

        BarData data = new BarData(set1, set2);
        mBar.setData(data);
        data.setBarWidth(0.3f);

        return data;
    }

    private void getPieChart(View v) {
        mPie = v.findViewById(R.id.pieChart1);
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
        ArrayList<PieEntry> entries_spent = new ArrayList<PieEntry>();

        getActualData();
        getStringLabels();

        for(int i = 0; i < arr_actual.size(); i++){
            entries_spent.add(new PieEntry((float) arr_actual.get(i), str_label.get(i)));
        }

        PieDataSet ds = new PieDataSet(entries_spent, "");
        ds.setColors(MyColorTemplate.COLORFUL_COLORS);
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
