package com.example.workshop_development_project.MainScreens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.workshop_development_project.Database.FinanceViewModel;
import com.example.workshop_development_project.Helper.Utils;
import com.example.workshop_development_project.databinding.FragmentSettingsBinding;

public class Settings_fragment extends Fragment {

    private FragmentSettingsBinding binding;
    private FinanceViewModel viewModel;
    private SharedPreferences sharedPreferences;

    public Settings_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        sharedPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        setupClickListeners();
        loadSettings();
    }

    private void loadSettings() {
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        binding.themeSwitch.setChecked(isDarkMode);
        
        String currency = sharedPreferences.getString("currency", "USD ($)");
        binding.currentCurrencyTv.setText(currency);
    }

    private void setupClickListeners() {
        // Add Default Categories
        binding.addDefaultCategoriesBtn.setOnClickListener(v -> {
            Utils.insertDefaultCategories(viewModel, requireContext());
            Toast.makeText(getContext(), "Default categories added", Toast.LENGTH_SHORT).show();
        });

        // Currency Selector
        binding.currencySetting.setOnClickListener(v -> {
            String[] currencies = {"USD ($)", "EUR (€)", "GBP (£)", "JPY (¥)"};
            new AlertDialog.Builder(requireContext())
                    .setTitle("Select Currency")
                    .setItems(currencies, (dialog, which) -> {
                        String selected = currencies[which];
                        binding.currentCurrencyTv.setText(selected);
                        sharedPreferences.edit().putString("currency", selected).apply();
                        Toast.makeText(getContext(), "Currency changed to " + selected, Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });

        // Theme Switch (Dark/Light Mode)
        binding.themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Reset Data
        binding.resetDataBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Reset All Data")
                    .setMessage("This will permanently delete all your transactions and categories. Are you sure?")
                    .setPositiveButton("Reset", (dialog, which) -> {
                        viewModel.deleteAllData();
                        Toast.makeText(getContext(), "All data has been cleared", Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
