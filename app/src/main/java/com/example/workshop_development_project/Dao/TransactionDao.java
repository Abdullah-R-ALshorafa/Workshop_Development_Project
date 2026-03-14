package com.example.workshop_development_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

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
    @Query("SELECT * FROM Transactions ORDER BY DATE DESC ")
    LiveData<List<Transactions>> getAllTransaction();
    @Query("SELECT * FROM  Transactions WHERE type = :type")
    LiveData<List<Transactions>> getTransactionsByType(String type);

    @Query("SELECT * FROM Transactions WHERE categoryId = :categoryId")
    LiveData<List<Transactions>> getTransactionsByCategory(int categoryId);
}
