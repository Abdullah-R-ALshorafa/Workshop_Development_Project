package com.example.workshop_development_project.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workshop_development_project.Model.Categorys;
import com.example.workshop_development_project.Model.Transactions;

import java.util.List;

public class FinanceViewModel extends AndroidViewModel {
    FinanceRepository repository;

    public FinanceViewModel(@NonNull Application application) {
        super(application);
        repository = new FinanceRepository(application);
    }


    public void insertTransaction(Transactions transaction) {
        repository.insertTransaction(transaction);
    }

    public void updateTransaction(Transactions transaction) {
        repository.updateTransaction(transaction);
    }

    public void deleteTransaction(Transactions transaction) {
        repository.deleteTransaction(transaction);
    }

    public LiveData<List<Transactions>> getAllTransaction() {
        return repository.getAllTransaction();
    }

    public LiveData<List<Transactions>> getTransactionsByType(String type) {
        return repository.getTransactionsByType(type);
    }

    public LiveData<List<Transactions>> getTransactionsByCategory(int categoryId) {
        return repository.getTransactionsByCategory(categoryId);
    }

    public LiveData<Double> getTransactionsIncome( ){
        return repository.getTransactionsIncome();
    }
    public LiveData<Double> getTransactionsEpense(){
        return repository.getTransactionsEpense();
    }
    public LiveData<Double> gitBalance(){
        return repository.getBalance();
    }


    public void insertCategory(Categorys categorys) {
        repository.insertCategory(categorys);
    }

    public void updateCategory(Categorys categorys) {
        repository.updateCategory(categorys);
    }

    public void deleteCategory(Categorys categorys) {
        repository.deleteCategory(categorys);
    }

    public LiveData<List<Categorys>> getAllCategory() {
        return repository.getAllCategory();
    }
}
