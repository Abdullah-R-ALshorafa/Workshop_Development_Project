package com.example.workshop_development_project.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "transaction"
        ,foreignKeys = @ForeignKey(entity = Category.class,parentColumns = "id", childColumns = "categoryId"))
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double amount;
    private String type;
    private int categoryId;
    private Date date;
    private String note;

    @Ignore
    public Transaction(int id, Date date, String note, int categoryId, String type, double amount) {
        this.id = id;
        this.date = date;
        this.note = note;
        this.categoryId = categoryId;
        this.type = type;
        this.amount = amount;
    }

    public Transaction(double amount, String type, int categoryId, Date date, String note) {
        this.amount = amount;
        this.type = type;
        this.categoryId = categoryId;
        this.date = date;
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
