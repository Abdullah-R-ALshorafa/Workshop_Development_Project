package com.example.workshop_development_project.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workshop_development_project.Dao.CategoryDao;
import com.example.workshop_development_project.Dao.TransactionDao;
import com.example.workshop_development_project.Model.Categorys;
import com.example.workshop_development_project.Model.Transactions;

import java.util.List;

public class FinanceRepository {
    private TransactionDao transactionDao;
    private CategoryDao categoryDao;


    public FinanceRepository(Application application) {
        FinanceRoomDatabase database = FinanceRoomDatabase.getDatabase(application);
        transactionDao = database.transactionDao();
        categoryDao = database.categoryDao();
    }

    void insertTransaction(Transactions transaction){
        FinanceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                transactionDao.insertTransaction(transaction);
            }
        });
    }
    void updateTransaction(Transactions transaction){
        FinanceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                transactionDao.updateTransaction(transaction);
            }
        });
    }
    void deleteTransaction(Transactions transaction){
        FinanceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                transactionDao.deleteTransaction(transaction);
            }
        });
    }
    LiveData<List<Transactions>> getAllTransaction(){
        return transactionDao.getAllTransaction();
    }
    LiveData<List<Transactions>> getTransactionsByType(String type){
        return transactionDao.getTransactionsByType(type);
    }

    LiveData<List<Transactions>> getTransactionsByCategory(int categoryId){
        return transactionDao.getTransactionsByCategory(categoryId);
    }


    void insertCategory(Categorys categorys){
        FinanceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.insertCategory(categorys);
            }
        });
    }
    void updateCategory(Categorys categorys){
        FinanceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.updateCategory(categorys);
            }
        });
    }
    void deleteCategory(Categorys categorys){
        FinanceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDao.deleteCategory(categorys);
            }
        });
    }
    LiveData<List<Categorys>> getAllCategory(){
        return categoryDao.getAllCategory();
    }
}
