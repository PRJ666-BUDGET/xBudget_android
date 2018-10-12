package com.prj666_183a06.xbudget;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HomeActivity extends Fragment {

    private LineChart mLine;
    private Typeface tf;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("xBudget");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);

        // Line Chart
        mLine = v.findViewById(R.id.lineChart1);
        mLine.getDescription().setEnabled(false);

        Legend lgdLine = mLine.getLegend();
        lgdLine.setForm(Legend.LegendForm.LINE);

        mLine.setData(generateLineData());

        return v;
    }

    protected LineData generateLineData() {

        ArrayList<Entry> budget = new ArrayList<Entry>();
        ArrayList<Entry> expenses = new ArrayList<Entry>();

        LineDataSet budget_ds;
        LineDataSet expenses_ds;

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        // Budget Dataset
        budget.add(new Entry(1, 2000));
        budget.add(new Entry(8, 2000));
        budget.add(new Entry(15, 2000));
        budget.add(new Entry(22, 2000));
        budget.add(new Entry(27, 2000));

        budget_ds = new LineDataSet(budget, "My Budget");
        budget_ds.setLineWidth(2f);
        budget_ds.setDrawCircles(false);
        budget_ds.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);

        dataSets.add(budget_ds);

        // Expense Dataset
        expenses.add(new Entry(1, 170));
        expenses.add(new Entry(8, 220));
        expenses.add(new Entry(15, 530));
        expenses.add(new Entry(22, 700));
        expenses.add(new Entry(27, 1070));

        expenses_ds = new LineDataSet(expenses, "My Expenses");
        expenses_ds.setLineWidth(2f);
        expenses_ds.setDrawCircles(false);
        expenses_ds.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);

        dataSets.add(expenses_ds);

        LineData lineData = new LineData(dataSets);
        lineData.setValueTypeface(tf);
        return lineData;
    }
}
