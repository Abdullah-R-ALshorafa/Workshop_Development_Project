package com.example.workshop_development_project;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.workshop_development_project.MainScreens.Chares_fragment;
import com.example.workshop_development_project.MainScreens.Transaction_fragment;
import com.example.workshop_development_project.MainScreens.home_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new home_fragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            Fragment fragment = null;
            if (menuItem.getItemId() == R.id.home) {
                fragment = new home_fragment();
            }

            if (menuItem.getItemId() == R.id.transaction) {
                fragment = new Transaction_fragment();
            }

            if (menuItem.getItemId() == R.id.chars) {
                fragment = new Chares_fragment();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();



            return true;
        });

    }
}