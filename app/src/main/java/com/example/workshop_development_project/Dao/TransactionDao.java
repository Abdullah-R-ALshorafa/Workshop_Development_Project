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
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<TransactionWithCategory>> getTransactionsWithCategory();

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date ASC")
    LiveData<List<TransactionWithCategory>> getTransactionsWithCategoryAsc();

    @Transaction
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date ASC")
    LiveData<List<TransactionWithCategory>> getTransactionsWithCategoryByTypeAsc(String type);

    @Transaction
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    LiveData<List<TransactionWithCategory>> getTransactionsWithCategoryByType(String type);

    @Transaction
    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId ORDER BY date DESC")
    LiveData<List<TransactionWithCategory>> getTransactionsWithCategoryByCategory(int categoryId);

    @Transaction
    @Query("SELECT * FROM transactions WHERE strftime('%Y-%m-%d', date / 1000, 'unixepoch') = :dateStr ORDER BY date DESC")
    LiveData<List<TransactionWithCategory>> getTransactionsByDate(String dateStr);

    @Transaction
    @Query("SELECT transactions.* FROM transactions LEFT JOIN categorys ON transactions.categoryId = categorys.id WHERE transactions.note LIKE '%' || :query || '%' OR categorys.name LIKE '%' || :query || '%' ORDER BY transactions.date DESC")
    LiveData<List<TransactionWithCategory>> searchTransactions(String query);

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
