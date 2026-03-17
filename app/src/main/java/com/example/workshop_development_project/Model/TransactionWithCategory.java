package com.example.workshop_development_project.Model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TransactionWithCategory {

    @Embedded
    public Transactions transaction;

    @Relation(
            parentColumn = "categoryId",
            entityColumn = "id"
    )
    public Categorys category;
}
