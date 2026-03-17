package com.example.workshop_development_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.workshop_development_project.Database.FinanceViewModel;
import com.example.workshop_development_project.Helper.TransactionType;
import com.example.workshop_development_project.Model.Transactions;
import com.example.workshop_development_project.databinding.ActivityTransactionDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionDetailActivity extends AppCompatActivity {

    private ActivityTransactionDetailBinding binding;
    private FinanceViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);

        binding.backIv.setOnClickListener(v -> finish());

        // Get data from intent
        int id = getIntent().getIntExtra("id", -1);
        double amount = getIntent().getDoubleExtra("amount", 0.0);
        String type = getIntent().getStringExtra("type");
        String category = getIntent().getStringExtra("category");
        int categoryId = getIntent().getIntExtra("categoryId", -1);
        long dateTimestamp = getIntent().getLongExtra("date", 0);
        String note = getIntent().getStringExtra("note");

        // Set data to views
        binding.amountDetailTv.setText(String.format(Locale.getDefault(), "$%.2f", amount));
        binding.typeDetailTv.setText(type);
        binding.categoryDetailTv.setText(category);
        
        if (note != null && !note.isEmpty()) {
            binding.noteDetailTv.setText(note);
        }

        if (dateTimestamp != 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            binding.dateDetailTv.setText(sdf.format(new Date(dateTimestamp)));
        }

        // Color coding for income/expense
        if ("INCOME".equalsIgnoreCase(type)) {
            binding.typeDetailTv.setTextColor(Color.parseColor("#2AAE8F"));
        } else {
            binding.typeDetailTv.setTextColor(Color.RED);
        }

        // Delete button logic
        binding.deleteIv.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Transaction")
                    .setMessage("Are you sure you want to delete this transaction?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Create a dummy transaction object with the ID to delete
                        Transactions transaction = new Transactions(amount, TransactionType.valueOf(type), categoryId, new Date(dateTimestamp), note);
                        transaction.setId(id);
                        viewModel.deleteTransaction(transaction);
                        Toast.makeText(this, "Transaction Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Edit button logic
        binding.editIv.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionDetailActivity.this, AddTransactionActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("amount", amount);
            intent.putExtra("type", type);
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("date", dateTimestamp);
            intent.putExtra("note", note);
            startActivity(intent);
            finish();
        });
    }
}
