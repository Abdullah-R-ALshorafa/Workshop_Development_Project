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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        binding.pieChart.setExtraOffsets(5, 10, 5, 5);
        binding.pieChart.setDragDecelerationFrictionCoef(0.95f);
        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setHoleColor(Color.WHITE);
        binding.pieChart.setTransparentCircleRadius(61f);
        binding.pieChart.setEntryLabelColor(Color.BLACK);
        binding.pieChart.setEntryLabelTextSize(12f);

        // Bar Chart Setup
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.setDrawGridBackground(false);
        binding.barChart.setDrawBarShadow(false);
        binding.barChart.setHighlightFullBarEnabled(false);
        
        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
    }

    private void updateCharts() {
        List<TransactionWithCategory> filteredList = new ArrayList<>();
        
        if (currentTab.equals("Overview")) {
            filteredList = allTransactions;
        } else if (currentTab.equals("Income")) {
            for (TransactionWithCategory t : allTransactions) {
                if (t.transaction.getType() == TransactionType.INCOME) filteredList.add(t);
            }
        } else {
            for (TransactionWithCategory t : allTransactions) {
                if (t.transaction.getType() == TransactionType.EXPENSE) filteredList.add(t);
            }
        }

        updatePieChart(filteredList);
        updateBarChart(filteredList);
    }

    private void updatePieChart(List<TransactionWithCategory> list) {
        Map<String, Double> categoryMap = new HashMap<>();
        for (TransactionWithCategory t : list) {
            String catName = t.category.getName();
            double amount = t.transaction.getAmount();
            categoryMap.put(catName, categoryMap.getOrDefault(catName, 0.0) + amount);
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        binding.pieChart.setData(data);
        binding.pieChart.invalidate();
        binding.pieChart.animateY(1000);
    }

    private void updateBarChart(List<TransactionWithCategory> list) {
        Map<String, Double> monthlyMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.getDefault());
        
        for (TransactionWithCategory t : list) {
            String month = sdf.format(t.transaction.getDate());
            double amount = t.transaction.getAmount();
            monthlyMap.put(month, monthlyMap.getOrDefault(month, 0.0) + amount);
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;
        for (Map.Entry<String, Double> entry : monthlyMap.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue().floatValue()));
            labels.add(entry.getKey());
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Monthly Data");
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        BarData data = new BarData(dataSet);
        binding.barChart.setData(data);
        
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
