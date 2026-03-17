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
import com.example.workshop_development_project.Model.TransactionWithCategory;
import com.example.workshop_development_project.R;
import com.example.workshop_development_project.databinding.FragmentHomeFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class home_fragment extends Fragment {
    RecyclerView recyclerView;
    TransactionAdapter adapter;
    FragmentHomeFragmentBinding binding;
    ArrayList<TransactionWithCategory> transactionsList = new ArrayList<>();
    FinanceViewModel model;

    // To keep track of current observer
    private LiveData<List<TransactionWithCategory>> currentLiveData;

    public home_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(this).get(FinanceViewModel.class);

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
            binding.tvExpense.setText("-$" + total);
        });

        model.gitBalance().observe(getViewLifecycleOwner(), balance -> {
            if (balance == null) balance = 0.0;
            binding.tvBalance.setText("$" + balance);
        });

        model.getTransactionsIncome().observe(getViewLifecycleOwner(), income -> {
            if (income == null) income = 0.0;
            binding.revenueValue.setText("$" + income);
        });

        // Click Listeners
        binding.showAllBtn.setOnClickListener(v -> showAll());
        binding.monthSortBtn.setOnClickListener(v -> showMonthly());
        binding.yearSortBtn.setOnClickListener(v -> showYearly());

        // Default: Show all
        showAll();
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
        // Remove previous observer if any to prevent duplicates
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
        // 0: All, 1: Month, 2: Year
        int selectedColor = Color.parseColor("#16C1A2");
        int unselectedColor = Color.parseColor("#DDF5E0");

        binding.showAllBtn.setBackgroundTintList(ColorStateList.valueOf(selection == 0 ? selectedColor : unselectedColor));
        binding.monthSortBtn.setBackgroundTintList(ColorStateList.valueOf(selection == 1 ? selectedColor : unselectedColor));
        binding.yearSortBtn.setBackgroundTintList(ColorStateList.valueOf(selection == 2 ? selectedColor : unselectedColor));
    }
}
