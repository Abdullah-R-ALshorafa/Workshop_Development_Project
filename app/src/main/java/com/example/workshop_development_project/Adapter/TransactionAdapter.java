package com.example.workshop_development_project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop_development_project.Database.FinanceViewModel;
import com.example.workshop_development_project.Model.TransactionWithCategory;
import com.example.workshop_development_project.Model.Transactions;
import com.example.workshop_development_project.R;
import com.example.workshop_development_project.databinding.TranscationViewRecyclerBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        holder.amount.setText(String.valueOf(transaction.getAmount()));

        // show category name
        holder.category.setText(item.category.getName());

        // show category image
        if(item.category.getImage() != null){
            holder.icon.setImageBitmap(item.category.getImage());
        }else{
            holder.icon.setImageResource(R.drawable.salary_icon);
        }
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
