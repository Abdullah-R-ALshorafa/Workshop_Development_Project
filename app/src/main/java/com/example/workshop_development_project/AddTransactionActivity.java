package com.example.workshop_development_project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.workshop_development_project.Database.FinanceViewModel;
import com.example.workshop_development_project.Helper.TransactionType;
import com.example.workshop_development_project.Model.Categorys;
import com.example.workshop_development_project.Model.Transactions;
import com.example.workshop_development_project.databinding.ActivityAddTransactionBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    ActivityAddTransactionBinding binding;
    Calendar calendar;
    FinanceViewModel viewModel;
    ArrayAdapter<String> adapter;
    List<Categorys> categoryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        viewModel.getAllCategory().observe(this, categorys -> {
            categoryList = categorys;

            List<String> names = new ArrayList<>();
            for (Categorys c : categorys) {
                names.add(c.getName());
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            binding.categorySpinner.setAdapter(adapter);
        });

        binding.saveBtn.setOnClickListener(v -> {

            String amountText = binding.amountEt.getText().toString();
            String note = binding.noteEt.getText().toString();
            String dateText = binding.dateEt.getText().toString();

            if(amountText.isEmpty()){
                binding.amountEt.setError("Enter amount");
                return;
            }

            double amount = Double.parseDouble(amountText);

            TransactionType type;
            if(binding.incomeRb.isChecked()){
                type = TransactionType.INCOME;
            }else{
                type = TransactionType.EXPENSE;
            }

            int position = binding.categorySpinner.getSelectedItemPosition();
            Categorys selectedCategory = categoryList.get(position);
            int categoryId = selectedCategory.getId();

            String dateString  = binding.dateEt.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());            Date date = new Date(); // fallback in case parsing fails
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Transactions transaction = new Transactions(
                    amount,
                    type,
                    categoryId,
                    date,
                    note
            );

            viewModel.insertTransaction(transaction);

            Toast.makeText(this,"Transaction Added",Toast.LENGTH_SHORT).show();

            finish();

        });

        binding.dateEt.setOnClickListener(v -> {
            calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(
                    AddTransactionActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        String date = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                selectedDay,
                                selectedMonth + 1,
                                selectedYear);
                        binding.dateEt.setText(date);

                    },
                    year,
                    month,
                    day
            );

            datePicker.show();

        });

    }
}