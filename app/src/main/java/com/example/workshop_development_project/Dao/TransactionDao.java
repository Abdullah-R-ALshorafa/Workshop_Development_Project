package com.example.workshop_development_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert
    void insertTransaction();
    @Update
    void updateTransaction();
    @Delete
    void deleteTransaction();
    @Query("SELECT * FROM `transaction` ORDER BY DATE DESC ")
    LiveData<List<com.example.workshop_development_project.Model.Transaction>> getAllTransaction();
    @Query("SELECT * FROM `transaction` WHERE type = :type")
    LiveData<List<com.example.workshop_development_project.Model.Transaction>> getTransactionsByType(String type);

    @Query("SELECT * FROM `transaction` WHERE categoryId = :categoryId")
    LiveData<List<com.example.workshop_development_project.Model.Transaction>> getTransactionsByCategory(int categoryId);
}
