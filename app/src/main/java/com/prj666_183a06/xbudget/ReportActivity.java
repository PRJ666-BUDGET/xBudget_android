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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ReportActivity extends Fragment {

    private PieChart mChart;
    private Typeface tf;

    public ReportActivity() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Plans");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_report, container, false);
//        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mChart = v.findViewById(R.id.pieChart1);
        mChart.getDescription().setEnabled(false);

//        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
//
//        mChart.setCenterTextTypeface(tf);
        mChart.setCenterText(generateCenterText());
        mChart.setCenterTextSize(10f);
//        mChart.setCenterTextTypeface(tf);

        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        mChart.setData(generatePieData());

        return v;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Revenes\nQuaters 2015");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);

        return s;
    }

//    private Typeface tf;

//    public ReportActivity() {
//
//    }

//    @Override
//    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
////        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    protected PieData generatePieData() {
        int count = 4;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for(int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 60) + 40), "Quater " + (i+1)));
        }

        PieDataSet ds = new PieDataSet(entries, "Quarterly Revenues 2015");
        ds.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds.setSliceSpace(2f);
        ds.setValueTextColor(Color.WHITE);
        ds.setValueTextSize(12f);

        PieData d = new PieData(ds);
        d.setValueTypeface(tf);

        return d;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        getActivity().setTitle("Plans");
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_report, container, false);
//    }
}
