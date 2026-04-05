package com.example.workshop_development_project;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    private int selectedIconResId = R.drawable.salary_icon; // Default icon


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

        binding.addCategoryIv.setOnClickListener(v -> showAddCategoryDialog());

        binding.saveBtn.setOnClickListener(v -> {
            String amountText = binding.amountEt.getText().toString();
            String note = binding.noteEt.getText().toString();
            String dateString = binding.dateEt.getText().toString();

            if (amountText.isEmpty()) {
                binding.amountEt.setError("Enter amount");
                return;
            }

            double amount = Double.parseDouble(amountText);

            TransactionType type = binding.incomeRb.isChecked() ? TransactionType.INCOME : TransactionType.EXPENSE;

            int position = binding.categorySpinner.getSelectedItemPosition();
            if (position < 0) {
                Toast.makeText(this, "Please select or add a category", Toast.LENGTH_SHORT).show();
                return;
            }
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
            } catch (Exception ignored) {
            }

            new DatePickerDialog(this, (view, year, month, day) ->
                    binding.dateEt.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year))
                    , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        EditText nameEt = dialogView.findViewById(R.id.categoryNameEt);
        LinearLayout iconsContainer = dialogView.findViewById(R.id.iconsContainer);

        selectedIconResId = R.drawable.salary_icon; // Default

        // Set up icon selection logic
        for (int i = 0; i < iconsContainer.getChildCount(); i++) {
            View child = iconsContainer.getChildAt(i);
            if (child instanceof ImageView) {
                child.setOnClickListener(v -> {
                    // Reset backgrounds
                    for (int j = 0; j < iconsContainer.getChildCount(); j++) {
                        iconsContainer.getChildAt(j).setBackgroundResource(R.drawable.notification_shape);
                    }
                    // Highlight selected
                    v.setBackgroundResource(R.drawable.grean_dot_shape);

                    // Store selection based on tag
                    String tag = v.getTag().toString();
                    switch (tag) {
                        case "salary_icon":
                            selectedIconResId = R.drawable.salary_icon;
                            break;
                        case "groceries_icon":
                            selectedIconResId = R.drawable.groceries_icon;
                            break;
                        case "rent_icon":
                            selectedIconResId = R.drawable.rent_icon;
                            break;
                        case "food_icon":
                            selectedIconResId = R.drawable.food_icon;
                            break;
                        case "car_icon":
                            selectedIconResId = R.drawable.car_icon;
                            break;
                    }
                });
            }
        }

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameEt.getText().toString().trim();
            if (!name.isEmpty()) {
                Bitmap icon = getBitmapFromDrawable(selectedIconResId);
                Categorys newCat = new Categorys(name, ContextCompat.getColor(this, R.color.green), icon);
                viewModel.insertCategory(newCat);
                Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private Bitmap getBitmapFromDrawable(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);
        if (drawable == null) return null;

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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
