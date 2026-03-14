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


    void insertTransaction(Transactions transaction) {
        repository.insertTransaction(transaction);
    }

    void updateTransaction(Transactions transaction) {
        repository.updateTransaction(transaction);
    }

    void deleteTransaction(Transactions transaction) {
        repository.deleteTransaction(transaction);
    }

    LiveData<List<Transactions>> getAllTransaction() {
        return repository.getAllTransaction();
    }

    LiveData<List<Transactions>> getTransactionsByType(String type) {
        return repository.getTransactionsByType(type);
    }

    LiveData<List<Transactions>> getTransactionsByCategory(int categoryId) {
        return repository.getTransactionsByCategory(categoryId);
    }


    void insertCategory(Categorys categorys) {
        repository.insertCategory(categorys);
    }

    void updateCategory(Categorys categorys) {
        repository.updateCategory(categorys);
    }

    void deleteCategory(Categorys categorys) {
        repository.deleteCategory(categorys);
    }

    LiveData<List<Categorys>> getAllCategory() {
        return repository.getAllCategory();
    }
}
