package com.example.workshop_development_project.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workshop_development_project.Model.Categorys;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(Categorys categorys);
    @Update
    void updateCategory(Categorys categorys);
    @Delete
    void deleteCategory(Categorys categorys);
    @Query("SELECT * FROM categorys")
    LiveData<List<Categorys>> getAllCategory();
}
