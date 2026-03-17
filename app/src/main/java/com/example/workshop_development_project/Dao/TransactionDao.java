package com.example.workshop_development_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.workshop_development_project.Model.TransactionWithCategory;
import com.example.workshop_development_project.Model.Transactions;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert
    void insertTransaction(Transactions transaction);

    @Update
    void updateTransaction(Transactions transaction);

    @Delete
    void deleteTransaction(Transactions transaction);

    @Transaction
    @Query("SELECT * FROM transactions")
    LiveData<List<TransactionWithCategory>> getTransactionsWithCategory();

    @Transaction
    @Query("SELECT * FROM transactions WHERE strftime('%m-%Y', date / 1000, 'unixepoch') = strftime('%m-%Y', 'now')")
    LiveData<List<TransactionWithCategory>> getMonthlyTransactions();

    @Transaction
    @Query("SELECT * FROM transactions WHERE strftime('%Y', date / 1000, 'unixepoch') = strftime('%Y', 'now')")
    LiveData<List<TransactionWithCategory>> getYearlyTransactions();

    @Query("SELECT * FROM Transactions ORDER BY DATE DESC ")
    LiveData<List<Transactions>> getAllTransaction();


    @Query("SELECT * FROM  Transactions WHERE type = :type")
    LiveData<List<Transactions>> getTransactionsByType(String type);

    @Query("SELECT * FROM Transactions WHERE categoryId = :categoryId")
    LiveData<List<Transactions>> getTransactionsByCategory(int categoryId);

    @Query("SELECT SUM(amount) FROM transactions WHERE type='INCOME'")
    LiveData<Double> getTransactionsIncome();

    @Query("SELECT SUM(amount) FROM transactions WHERE type='EXPENSE'")
    LiveData<Double> getTransactionsExpence();

    @Query("SELECT SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) FROM transactions")
    LiveData<Double> getBalance();

}
