package com.example.workshop_development_project.MainScreens;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workshop_development_project.Adapter.TransactionAdapter;
import com.example.workshop_development_project.AddTransactionActivity;
import com.example.workshop_development_project.Database.FinanceViewModel;
import com.example.workshop_development_project.Helper.Utils;
import com.example.workshop_development_project.Model.Categorys;
import com.example.workshop_development_project.Model.TransactionWithCategory;
import com.example.workshop_development_project.databinding.FragmentTransactionFragmentBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Transaction_fragment extends Fragment {

    private FragmentTransactionFragmentBinding binding;
    private TransactionAdapter adapter;
    private FinanceViewModel model;
    private ArrayList<TransactionWithCategory> transactionsList = new ArrayList<>();
    private LiveData<List<TransactionWithCategory>> currentLiveData;
    private boolean isAscending = false;

    public Transaction_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionFragmentBinding.inflate(inflater, container, false);

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

        adapter = new TransactionAdapter(transactionsList);
        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewHome.setAdapter(adapter);

        // Update Totals
        model.gitBalance().observe(getViewLifecycleOwner(), balance -> {
            if (balance == null) balance = 0.0;
            binding.balanceMoneyTv.setText("$" + balance);
        });

        model.getTransactionsIncome().observe(getViewLifecycleOwner(), income -> {
            if (income == null) income = 0.0;
            binding.incomeTv.setText("$" + income);
        });

        model.getTransactionsEpense().observe(getViewLifecycleOwner(), expense -> {
            if (expense == null) expense = 0.0;
            binding.expenseTv.setText("$" + expense);
        });

        // Click Listeners for Filtering
        binding.cardTotalMoney.setOnClickListener(v -> showAll());
        binding.cardIncome.setOnClickListener(v -> showIncome());
        binding.cardExpense.setOnClickListener(v -> showExpense());

        // Chip Filters
        binding.sortAssAndDesBtn.setOnClickListener(v -> toggleSortOrder());
        binding.categoryBtn.setOnClickListener(v -> showCategoryFilterDialog());
        binding.DateBtn.setOnClickListener(v -> showDatePicker());

        // Search Implementation
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    observeTransactions(model.searchTransactions(query));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    observeTransactions(model.searchTransactions(newText));
                } else {
                    showAll();
                }
                return true;
            }
        });

        // Default view
        showAll();
    }

    private void toggleSortOrder() {
        isAscending = !isAscending;
        String sortText = isAscending ? "Sort: Oldest" : "Sort: Newest";
        binding.sortAssAndDesBtn.setText(sortText);
        showAll(); // Re-apply current main view with new sort
    }


    private void showCategoryFilterDialog() {
        model.getAllCategory().observe(getViewLifecycleOwner(), categories -> {
            if (categories == null || categories.isEmpty()) return;
            String[] categoryNames = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                categoryNames[i] = categories.get(i).getName();
            }

            new AlertDialog.Builder(getContext())
                    .setTitle("Select Category")
                    .setItems(categoryNames, (dialog, which) -> {
                        Categorys selected = categories.get(which);
                        observeTransactions(model.getTransactionsWithCategoryByCategory(selected.getId()));
                    }).show();
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth - 1);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateStr = sdf.format(calendar.getTime());
            observeTransactions(model.getTransactionsByDate(dateStr));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showAll() {
        updateCardColors(0);
        if (isAscending) {
            observeTransactions(model.getTransactionsWithCategoryAsc());
        } else {
            observeTransactions(model.getTransactionsWithCategory());
        }
    }

    private void showIncome() {
        updateCardColors(1);
        if (isAscending) {
            observeTransactions(model.getTransactionsWithCategoryByTypeAsc("INCOME"));
        } else {
            observeTransactions(model.getTransactionsWithCategoryByType("INCOME"));
        }
    }

    private void showExpense() {
        updateCardColors(2);
        if (isAscending) {
            observeTransactions(model.getTransactionsWithCategoryByTypeAsc("EXPENSE"));
        } else {
            observeTransactions(model.getTransactionsWithCategoryByType("EXPENSE"));
        }
    }

    private void observeTransactions(LiveData<List<TransactionWithCategory>> liveData) {
        if (currentLiveData != null) {
            currentLiveData.removeObservers(getViewLifecycleOwner());
        }
        currentLiveData = liveData;
        currentLiveData.observe(getViewLifecycleOwner(), list -> {
            transactionsList.clear();
            if (list != null && !list.isEmpty()) {
                transactionsList.addAll(list);
                binding.recyclerViewHome.setVisibility(View.VISIBLE);
                binding.emptyStateLayout.setVisibility(View.GONE);
            } else {
                binding.recyclerViewHome.setVisibility(View.GONE);
                binding.emptyStateLayout.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void updateCardColors(int selection) {
        int selectedColor = Color.parseColor("#16C1A2");
        int unselectedColor = Color.parseColor("#E9EFEA");
        int whiteColor = Color.parseColor("#FFFFFF");

        binding.cardTotalMoney.setCardBackgroundColor(selection == 0 ? selectedColor : whiteColor);
        binding.cardIncome.setCardBackgroundColor(selection == 1 ? selectedColor : unselectedColor);
        binding.cardExpense.setCardBackgroundColor(selection == 2 ? selectedColor : unselectedColor);

        int whiteText = Color.WHITE;
        int darkText = Color.parseColor("#1F1F1F");

        binding.balanceTotalTv.setTextColor(selection == 0 ? whiteText : darkText);
        binding.balanceMoneyTv.setTextColor(selection == 0 ? whiteText : darkText);

        binding.incomeLabelTv.setTextColor(selection == 1 ? whiteText : darkText);
        binding.incomeTv.setTextColor(selection == 1 ? whiteText : darkText);

        binding.expenseLabelTv.setTextColor(selection == 2 ? whiteText : darkText);
        binding.expenseTv.setTextColor(selection == 2 ? whiteText : Color.parseColor("#2979FF"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
