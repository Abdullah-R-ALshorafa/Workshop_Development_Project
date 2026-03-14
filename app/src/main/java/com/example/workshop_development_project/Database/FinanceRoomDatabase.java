package com.example.workshop_development_project.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.workshop_development_project.Dao.CategoryDao;
import com.example.workshop_development_project.Dao.TransactionDao;
import com.example.workshop_development_project.Model.Categorys;
import com.example.workshop_development_project.Model.Transactions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Transactions.class, Categorys.class}, version = 1, exportSchema = false)
public abstract class FinanceRoomDatabase extends RoomDatabase {

    public abstract TransactionDao transactionDao();
    public abstract CategoryDao categoryDao();

    private static volatile FinanceRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static FinanceRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FinanceRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FinanceRoomDatabase.class, "finance_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
