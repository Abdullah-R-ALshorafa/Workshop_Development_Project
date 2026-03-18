package com.example.workshop_development_project.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop_development_project.Model.TransactionWithCategory;
import com.example.workshop_development_project.Model.Transactions;
import com.example.workshop_development_project.R;
import com.example.workshop_development_project.TransactionDetailActivity;
import com.example.workshop_development_project.databinding.TranscationViewRecyclerBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    ArrayList<TransactionWithCategory> transactions;

    public TransactionAdapter(ArrayList<TransactionWithCategory> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TranscationViewRecyclerBinding binding = TranscationViewRecyclerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionWithCategory item = transactions.get(position);
        Transactions transaction = item.transaction;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transaction.getDate());
        String date = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);

        holder.title.setText(String.valueOf(transaction.getType()));
        holder.date.setText(date);
        holder.amount.setText(String.format("$%.2f", transaction.getAmount()));
        holder.category.setText(item.category.getName());

        if (item.category.getImage() != null) {
            holder.icon.setImageBitmap(item.category.getImage());
        } else {
            holder.icon.setImageResource(R.drawable.salary_icon);
        }

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TransactionDetailActivity.class);
            intent.putExtra("id", transaction.getId());
            intent.putExtra("amount", transaction.getAmount());
            intent.putExtra("type", transaction.getType().name());
            intent.putExtra("category", item.category.getName());
            intent.putExtra("categoryId", transaction.getCategoryId());
            intent.putExtra("date", transaction.getDate().getTime());
            intent.putExtra("note", transaction.getNote());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title, date, category, amount;

        public ViewHolder(@NonNull TranscationViewRecyclerBinding binding) {
            super(binding.getRoot());
            icon = binding.transactionIconIv;
            title = binding.titleTv;
            date = binding.dateTv;
            category = binding.categoryTv;
            amount = binding.amountTv;
        }
    }
}
