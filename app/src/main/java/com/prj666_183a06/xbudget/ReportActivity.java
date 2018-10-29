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

public class ReportActivity extends Fragment {

    private PieChart mPie;
    private BarChart mBar;
    private float mTotalExpenses;

    private Typeface tf;

    String[] str_label;

    public ReportActivity() {

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

        // Pie Chart
        generatePieChart(v);
        getBarChart(v);

        return v;
    }

    private void getBarChart(View v) {

        str_label = new String[] {"Grocery", "TTC", "School", "Clothes"};

        mBar = v.findViewById(R.id.barChart2);
        mBar.getDescription().setEnabled(false);

        mBar.setData(generateBarData());

        XAxis xAxis = mBar.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(str_label));
        mBar.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
//        xAxis.setGranularity(1);
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
        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();

        List<Float> arr_plan = new ArrayList<Float>();
        List<Float> arr_actual = new ArrayList<Float>();

        arr_plan.add(120f);
        arr_plan.add(75f);
        arr_plan.add(55f);
        arr_plan.add(164f);

        arr_actual.add(120f);
        arr_actual.add(80f);
        arr_actual.add(20f);
        arr_actual.add(20f);

        entries1.add(new BarEntry(1, 120f));
        entries1.add(new BarEntry(2, 75f));
        entries1.add(new BarEntry(3, 55f));
        entries1.add(new BarEntry(4, 164f));

        entries2.add(new BarEntry(1, 120f));
        entries2.add(new BarEntry(2, 80f));
        entries2.add(new BarEntry(3, 20f));
        entries2.add(new BarEntry(4, 20f));

        BarDataSet set1, set2;
        set1 = new BarDataSet(entries1, "Plan");
        set1.setColor(Color.rgb(164, 228, 251));

        set2 = new BarDataSet(entries2, "Actual");
        set2.setColor(Color.rgb(193, 37, 82));

        BarData data = new BarData(set1, set2);
        mBar.setData(data);
        data.setBarWidth(0.3f);

        return data;
    }

    private void generatePieChart(View v) {
        mPie = v.findViewById(R.id.pieChart1);
        mPie.getDescription().setEnabled(false);

        mPie.setCenterText(generateCenterText());
        mPie.setCenterTextSize(10f);

        mPie.setHoleRadius(45f);
        mPie.setTransparentCircleRadius(50f);

        Legend lgdPie = mPie.getLegend();
        lgdPie.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lgdPie.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lgdPie.setOrientation(Legend.LegendOrientation.VERTICAL);
        lgdPie.setDrawInside(false);

        mPie.setData(generatePieData());
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Expenses");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);

        return s;
    }

    protected PieData generatePieData() {
        int count = 7;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        float totalExpenses;
        float v_grocery = 400;
        float v_clothes = 120;
        float v_transportation = 150;
        float v_drink = 50;
        float v_eatout = 250;
        float v_financing = 130;
        float v_insurance = 350;

        entries.add(new PieEntry((float) v_grocery, "Grocery"));
        entries.add(new PieEntry((float) v_clothes, "Clothes"));
        entries.add(new PieEntry((float) v_transportation, "Transportation"));
        entries.add(new PieEntry((float) v_drink, "Drink"));
        entries.add(new PieEntry((float) v_eatout, "Eatout"));
        entries.add(new PieEntry((float) v_financing, "Financing"));
        entries.add(new PieEntry((float) v_insurance, "Insurance"));

        PieDataSet ds = new PieDataSet(entries, "");
        ds.setColors(MyColorTemplate.COLORFUL_COLORS);
        ds.setSliceSpace(2f);
        ds.setValueTextColor(Color.WHITE);
        ds.setValueTextSize(12f);

        PieData d = new PieData(ds);
//        d.setValueTypeface(tf);

        return d;
    }

}
