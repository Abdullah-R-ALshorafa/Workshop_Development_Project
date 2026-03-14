package com.example.workshop_development_project.Model;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.workshop_development_project.Database.Converters;

@Entity(tableName = "categorys")
public class Categorys {
    @TypeConverters(Converters.class)
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Bitmap image;
    private int color;
    @Ignore
    public Categorys(int id, int color, Bitmap image, String name) {
        this.id = id;
        this.color = color;
        this.image = image;
        this.name = name;
    }

    public Categorys(int color, Bitmap image, String name) {
        this.color = color;
        this.image = image;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
