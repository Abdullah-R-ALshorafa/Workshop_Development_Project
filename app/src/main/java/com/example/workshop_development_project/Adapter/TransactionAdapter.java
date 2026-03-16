package com.example.workshop_development_project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop_development_project.Model.Transactions;
import com.example.workshop_development_project.R;
import com.example.workshop_development_project.databinding.TranscationViewRecyclerBinding;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    ArrayList<Transactions> transactions;

    public TransactionAdapter(ArrayList<Transactions> transactions) {
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

        Transactions t = transactions.get(position);

        holder.icon.setImageResource(R.drawable.salary_icon);
        holder.title.setText(String.valueOf(t.getType()));
        holder.date.setText(String.valueOf(t.getDate()));
        holder.category.setText(String.valueOf(t.getCategoryId()));
        holder.amount.setText(String.valueOf(t.getAmount()));
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
