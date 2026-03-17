package com.example.workshop_development_project.Helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.example.workshop_development_project.Database.FinanceViewModel;
import com.example.workshop_development_project.Model.Categorys;
import com.example.workshop_development_project.R;

public class Utils {
    public static void insertDefaultCategories(FinanceViewModel viewModel, Context context){


        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.salary_icon);
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.groceries_icon);
        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.rent_icon);


        viewModel.insertCategory(new Categorys("Food", Color.RED, bitmap));
        viewModel.insertCategory(new Categorys("Transport", Color.BLUE, bitmap));
        viewModel.insertCategory(new Categorys("Shopping", Color.GREEN, bitmap1));
        viewModel.insertCategory(new Categorys("Bills", Color.YELLOW, bitmap1));
        viewModel.insertCategory(new Categorys("Salary", Color.CYAN, bitmap2));
        viewModel.insertCategory(new Categorys("Health", Color.MAGENTA, bitmap2));

    }
}
