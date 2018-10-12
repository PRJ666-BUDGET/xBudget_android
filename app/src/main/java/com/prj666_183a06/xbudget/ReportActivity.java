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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FileUtils;

import java.util.ArrayList;

public class ReportActivity extends Fragment {

    private PieChart mPie;
    private LineChart mLine;

    private Typeface tf;

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
        //
        //        // Pie Chart
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

//        // Line Chart
//        mLine = v.findViewById(R.id.lineChart1);
//        mLine.getDescription().setEnabled(false);
//
////        mLine.setCenterText(generateCenterText());
////        mLine.setCenterTextSize(10f);
//
////        mChart.setHoleRadius(45f);
////        mChart.setTransparentCircleRadius(50f);
//
//        Legend lgdLine = mLine.getLegend();
//        lgdLine.setForm(Legend.LegendForm.LINE);
//
//        mLine.setData(generateLineData());

        return v;
    }

//    protected LineData generateLineData() {
//        ArrayList<Entry> values = new ArrayList<Entry>();
//        int count = 5;
//        float range = 100;
//
//        for (int i = 0; i < count; i++){
//            float val = (float) (Math.random() * range) + 3;
//            values.add(new Entry(i, val));
//        }
//
//        LineDataSet ds1;
//
//        ds1 = new LineDataSet(values, "DateSet 1");
//        ds1.setLineWidth(2f);
//        ds1.setDrawCircles(false);
//        ds1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//        dataSets.add(ds1);
//
//        LineData d = new LineData(dataSets);
//        d.setValueTypeface(tf);
//        return d;
//
//    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Revenes\nQuaters 2015");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);

        return s;
    }

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

}
