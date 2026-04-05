package com.example.workshop_development_project.MainScreens;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workshop_development_project.Database.FinanceViewModel;
import com.example.workshop_development_project.Helper.TransactionType;
import com.example.workshop_development_project.Model.TransactionWithCategory;
import com.example.workshop_development_project.databinding.FragmentCharesFragmentBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Chares_fragment extends Fragment {

    private FragmentCharesFragmentBinding binding;
    private FinanceViewModel viewModel;
    private List<TransactionWithCategory> allTransactions = new ArrayList<>();
    private String currentTab = "Overview";

    public Chares_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCharesFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        setupCharts();

        viewModel.getTransactionsWithCategory().observe(getViewLifecycleOwner(), transactions -> {
            if (transactions != null) {
                allTransactions = transactions;
                updateCharts();
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getText().toString();
                updateCharts();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupCharts() {
        // Pie Chart Setup
        binding.pieChart.setUsePercentValues(true);
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setHoleColor(Color.TRANSPARENT);
        binding.pieChart.setHoleRadius(58f);
        binding.pieChart.setTransparentCircleRadius(61f);
        binding.pieChart.setDrawCenterText(true);
        binding.pieChart.setCenterTextSize(16f);
        binding.pieChart.setRotationEnabled(true);
        binding.pieChart.setHighlightPerTapEnabled(true);
        
        Legend l = binding.pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setWordWrapEnabled(true);

        // Bar Chart Setup
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.setDrawGridBackground(false);
        binding.barChart.setDrawBarShadow(false);
        
        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(false);
        
        binding.barChart.getAxisRight().setEnabled(false);
        binding.barChart.getAxisLeft().setAxisMinimum(0f);
    }

    private void updateCharts() {
        if (allTransactions == null || allTransactions.isEmpty()) {
            binding.pieChart.clear();
            binding.barChart.clear();
            return;
        }

        List<TransactionWithCategory> filteredList = new ArrayList<>();
        if (currentTab.equals("Overview")) {
            filteredList = allTransactions;
        } else {
            TransactionType targetType = currentTab.equals("Income") ? TransactionType.INCOME : TransactionType.EXPENSE;
            for (TransactionWithCategory t : allTransactions) {
                if (t.transaction.getType() == targetType) filteredList.add(t);
            }
        }

        updatePieChart(filteredList);
        updateBarChart(filteredList);
    }

    private void updatePieChart(List<TransactionWithCategory> list) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        double total = 0;

        if (currentTab.equals("Overview")) {
            double income = 0;
            double expense = 0;
            for (TransactionWithCategory t : list) {
                if (t.transaction.getType() == TransactionType.INCOME) income += t.transaction.getAmount();
                else expense += t.transaction.getAmount();
            }
            if (income > 0) entries.add(new PieEntry((float) income, "Income"));
            if (expense > 0) entries.add(new PieEntry((float) expense, "Expense"));
            colors.add(Color.parseColor("#00CE9D")); // Green
            colors.add(Color.parseColor("#FF5252")); // Red
            total = income - expense;
        } else {
            Map<String, Double> categoryMap = new HashMap<>();
            for (TransactionWithCategory t : list) {
                String name = t.category.getName();
                categoryMap.put(name, categoryMap.getOrDefault(name, 0.0) + t.transaction.getAmount());
                total += t.transaction.getAmount();
            }
            for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
                entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
            }
            for (int c : ColorTemplate.VORDIPLOM_COLORS) colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS) colors.add(c);
        }

        if (entries.isEmpty()) {
            binding.pieChart.clear();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(2f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(binding.pieChart));
        
        binding.pieChart.setCenterText(String.format(Locale.getDefault(), "Total\n$%.2f", total));
        binding.pieChart.setData(data);
        binding.pieChart.invalidate();
        binding.pieChart.animateXY(800, 800);
    }

    private void updateBarChart(List<TransactionWithCategory> list) {
        if (list.isEmpty()) {
            binding.barChart.clear();
            return;
        }

        // Group by month-year to avoid merging different years
        SimpleDateFormat keyFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        SimpleDateFormat labelFormat = new SimpleDateFormat("MMM yy", Locale.getDefault());
        
        TreeMap<String, Float> incomeMap = new TreeMap<>();
        TreeMap<String, Float> expenseMap = new TreeMap<>();
        Map<String, String> keyToLabel = new HashMap<>();

        for (TransactionWithCategory t : list) {
            String key = keyFormat.format(t.transaction.getDate());
            String label = labelFormat.format(t.transaction.getDate());
            keyToLabel.put(key, label);
            
            if (!incomeMap.containsKey(key)) incomeMap.put(key, 0f);
            if (!expenseMap.containsKey(key)) expenseMap.put(key, 0f);

            if (t.transaction.getType() == TransactionType.INCOME) {
                incomeMap.put(key, incomeMap.get(key) + (float) t.transaction.getAmount());
            } else {
                expenseMap.put(key, expenseMap.get(key) + (float) t.transaction.getAmount());
            }
        }

        ArrayList<BarEntry> incomeEntries = new ArrayList<>();
        ArrayList<BarEntry> expenseEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int i = 0;
        for (String key : incomeMap.keySet()) {
            labels.add(keyToLabel.get(key));
            incomeEntries.add(new BarEntry(i, incomeMap.get(key)));
            expenseEntries.add(new BarEntry(i, expenseMap.get(key)));
            i++;
        }

        BarDataSet incomeSet = new BarDataSet(incomeEntries, "Income");
        incomeSet.setColor(Color.parseColor("#00CE9D"));
        
        BarDataSet expenseSet = new BarDataSet(expenseEntries, "Expense");
        expenseSet.setColor(Color.parseColor("#FF5252"));

        BarData data;
        if (currentTab.equals("Overview")) {
            data = new BarData(incomeSet, expenseSet);
            float groupSpace = 0.3f;
            float barSpace = 0.05f;
            float barWidth = 0.3f;
            data.setBarWidth(barWidth);
            binding.barChart.setData(data);
            binding.barChart.groupBars(0f, groupSpace, barSpace);
            binding.barChart.getXAxis().setAxisMinimum(0f);
            binding.barChart.getXAxis().setAxisMaximum(labels.size());
            binding.barChart.getXAxis().setCenterAxisLabels(true);
        } else {
            data = new BarData(currentTab.equals("Income") ? incomeSet : expenseSet);
            data.setBarWidth(0.5f);
            binding.barChart.setData(data);
            binding.barChart.getXAxis().setAxisMinimum(-0.5f);
            binding.barChart.getXAxis().setAxisMaximum(labels.size() - 0.5f);
            binding.barChart.getXAxis().setCenterAxisLabels(false);
        }

        binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        binding.barChart.invalidate();
        binding.barChart.animateY(1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
