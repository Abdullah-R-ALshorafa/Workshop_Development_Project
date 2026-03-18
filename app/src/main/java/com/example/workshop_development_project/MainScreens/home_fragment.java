package com.example.workshop_development_project.MainScreens;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workshop_development_project.Adapter.TransactionAdapter;
import com.example.workshop_development_project.AddTransactionActivity;
import com.example.workshop_development_project.Database.FinanceViewModel;
import com.example.workshop_development_project.Helper.TransactionType;
import com.example.workshop_development_project.Model.TransactionWithCategory;
import com.example.workshop_development_project.R;
import com.example.workshop_development_project.databinding.FragmentHomeFragmentBinding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class home_fragment extends Fragment {
    RecyclerView recyclerView;
    TransactionAdapter adapter;
    FragmentHomeFragmentBinding binding;
    ArrayList<TransactionWithCategory> transactionsList = new ArrayList<>();
    FinanceViewModel model;

    private LiveData<List<TransactionWithCategory>> currentLiveData;

    public home_fragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(this).get(FinanceViewModel.class);

        setupMiniChart();

        binding.addTransactionFb.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTransactionActivity.class);
            startActivity(intent);
        });

        recyclerView = binding.recyclerViewHome;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter(transactionsList);
        recyclerView.setAdapter(adapter);

        // Observe Totals
        model.getTransactionsEpense().observe(getViewLifecycleOwner(), total -> {
            if (total == null) total = 0.0;
            binding.tvExpense.setText(String.format(Locale.getDefault(), "-$%.2f", total));
            updateMiniChartData();
        });

        model.gitBalance().observe(getViewLifecycleOwner(), balance -> {
            if (balance == null) balance = 0.0;
            binding.tvBalance.setText(String.format(Locale.getDefault(), "$%.2f", balance));
        });

        model.getTransactionsIncome().observe(getViewLifecycleOwner(), income -> {
            if (income == null) income = 0.0;
            binding.revenueValue.setText(String.format(Locale.getDefault(), "$%.2f", income));
            updateMiniChartData();
        });

        // Live observation of the last expense
        model.getTransactionsWithCategory().observe(getViewLifecycleOwner(), list -> {
            boolean found = false;
            if (list != null) {
                for (TransactionWithCategory t : list) {
                    if (t.transaction.getType() == TransactionType.EXPENSE) {
                        binding.lastPaymentTv.setText(String.format(Locale.getDefault(), "-$%.2f", t.transaction.getAmount()));
                        found = true;
                        break; 
                    }
                }
            }
            if (!found) {
                binding.lastPaymentTv.setText("$0.00");
            }
        });

        // Click Listeners for time filtering
        binding.showAllBtn.setOnClickListener(v -> showAll());
        binding.monthSortBtn.setOnClickListener(v -> showMonthly());
        binding.yearSortBtn.setOnClickListener(v -> showYearly());

        // Default: Show all
        showAll();
    }

    private void setupMiniChart() {
        binding.miniPieChart.setUsePercentValues(true);
        binding.miniPieChart.getDescription().setEnabled(false);
        binding.miniPieChart.setDrawHoleEnabled(true);
        binding.miniPieChart.setHoleColor(Color.TRANSPARENT);
        binding.miniPieChart.getLegend().setEnabled(false);
        binding.miniPieChart.setDrawEntryLabels(false);
        binding.miniPieChart.setRotationEnabled(false);
    }

    private void updateMiniChartData() {
        // We use the already observed totals to update the chart
        model.getTransactionsIncome().observe(getViewLifecycleOwner(), income -> {
            model.getTransactionsEpense().observe(getViewLifecycleOwner(), expense -> {
                float inc = income != null ? income.floatValue() : 0f;
                float exp = expense != null ? expense.floatValue() : 0f;

                ArrayList<PieEntry> entries = new ArrayList<>();
                if (inc > 0) entries.add(new PieEntry(inc, "Income"));
                if (exp > 0) entries.add(new PieEntry(exp, "Expense"));
                
                if (entries.isEmpty()) {
                    binding.miniPieChart.clear();
                    return;
                }

                PieDataSet dataSet = new PieDataSet(entries, "");
                // White for Income, Blue for Expense
                dataSet.setColors(new int[]{Color.WHITE, Color.parseColor("#0D6EFD")});
                dataSet.setDrawValues(false);

                PieData data = new PieData(dataSet);
                binding.miniPieChart.setData(data);
                binding.miniPieChart.invalidate();
            });
        });
    }

    private void showAll() {
        updateButtonColors(0);
        observeTransactions(model.getTransactionsWithCategory());
    }

    private void showMonthly() {
        updateButtonColors(1);
        observeTransactions(model.getMonthlyTransactions());
    }

    private void showYearly() {
        updateButtonColors(2);
        observeTransactions(model.getYearlyTransactions());
    }

    private void observeTransactions(LiveData<List<TransactionWithCategory>> liveData) {
        if (currentLiveData != null) {
            currentLiveData.removeObservers(getViewLifecycleOwner());
        }

        currentLiveData = liveData;
        currentLiveData.observe(getViewLifecycleOwner(), list -> {
            transactionsList.clear();
            if (list != null) {
                transactionsList.addAll(list);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void updateButtonColors(int selection) {
        int selectedColor = Color.parseColor("#16C1A2");
        int unselectedColor = Color.parseColor("#DDF5E0");

        binding.showAllBtn.setBackgroundTintList(ColorStateList.valueOf(selection == 0 ? selectedColor : unselectedColor));
        binding.monthSortBtn.setBackgroundTintList(ColorStateList.valueOf(selection == 1 ? selectedColor : unselectedColor));
        binding.yearSortBtn.setBackgroundTintList(ColorStateList.valueOf(selection == 2 ? selectedColor : unselectedColor));
    }
}
