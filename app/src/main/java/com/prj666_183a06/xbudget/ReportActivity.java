package com.prj666_183a06.xbudget;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ReportActivity extends Fragment {

    private Typeface tf;

    public ReportActivity() {

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        return super.onCreateView(inflater, container, savedInstanceState);
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
