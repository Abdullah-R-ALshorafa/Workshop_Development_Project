package com.example.workshop_development_project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    
    private int transactionId = -1; // -1 means adding new, otherwise editing


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        
        // Check if we are in Edit mode
        if (getIntent().hasExtra("id")) {
            transactionId = getIntent().getIntExtra("id", -1);
            binding.saveBtn.setText("Update");
            loadTransactionData();
        }

        viewModel.getAllCategory().observe(this, categorys -> {
            categoryList = categorys;
            List<String> names = new ArrayList<>();
            for (Categorys c : categorys) {
                names.add(c.getName());
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.categorySpinner.setAdapter(adapter);
            
            // If editing, set the spinner to correct category after list loads
            if (transactionId != -1) {
                int categoryId = getIntent().getIntExtra("categoryId", -1);
                for (int i = 0; i < categoryList.size(); i++) {
                    if (categoryList.get(i).getId() == categoryId) {
                        binding.categorySpinner.setSelection(i);
                        break;
                    }
                }
            }
        });

        binding.saveBtn.setOnClickListener(v -> {
            String amountText = binding.amountEt.getText().toString();
            String note = binding.noteEt.getText().toString();
            String dateString = binding.dateEt.getText().toString();

            if(amountText.isEmpty()){
                binding.amountEt.setError("Enter amount");
                return;
            }

            double amount = Double.parseDouble(amountText);

            TransactionType type = binding.incomeRb.isChecked() ? TransactionType.INCOME : TransactionType.EXPENSE;

            int position = binding.categorySpinner.getSelectedItemPosition();
            if (position < 0) return;
            int categoryId = categoryList.get(position).getId();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                date = new Date();
            }

            Transactions transaction = new Transactions(
                    amount,
                    type,
                    categoryId,
                    date,
                    note
            );

            if (transactionId != -1) {
                transaction.setId(transactionId);
                viewModel.updateTransaction(transaction);
                Toast.makeText(this, "Transaction Updated", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.insertTransaction(transaction);
                Toast.makeText(this, "Transaction Added", Toast.LENGTH_SHORT).show();
            }

            finish();
        });

        binding.dateEt.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            // If date exists, set it in picker
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date d = sdf.parse(binding.dateEt.getText().toString());
                if (d != null) calendar.setTime(d);
            } catch (Exception ignored) {}

            new DatePickerDialog(this, (view, year, month, day) -> {
                binding.dateEt.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void loadTransactionData() {
        binding.amountEt.setText(String.valueOf(getIntent().getDoubleExtra("amount", 0.0)));
        binding.noteEt.setText(getIntent().getStringExtra("note"));
        
        long timestamp = getIntent().getLongExtra("date", 0);
        if (timestamp != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            binding.dateEt.setText(sdf.format(new Date(timestamp)));
        }

        String type = getIntent().getStringExtra("type");
        if ("INCOME".equalsIgnoreCase(type)) {
            binding.incomeRb.setChecked(true);
        } else {
            binding.expenseRb.setChecked(true);
        }
    }
}
