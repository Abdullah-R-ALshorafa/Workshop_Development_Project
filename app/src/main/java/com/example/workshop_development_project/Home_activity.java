package com.example.workshop_development_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.workshop_development_project.Adapter.MainPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_activity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupViewPager();
        setupNavigation();
    }

    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupViewPager() {
        MainPagerAdapter adapter = new MainPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Synchronize ViewPager2 with BottomNavigationView (Swipe support)
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateBottomNavigation(position);
            }
        });
    }

    private void setupNavigation() {
        // Synchronize BottomNavigationView with ViewPager2
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.home) {
                viewPager.setCurrentItem(0, true);
            } else if (itemId == R.id.transaction) {
                viewPager.setCurrentItem(1, true);
            } else if (itemId == R.id.chars) {
                viewPager.setCurrentItem(2, true);
            } else if (itemId == R.id.settings) {
                viewPager.setCurrentItem(3, true);
            }
            return true;
        });
    }

    private void updateBottomNavigation(int position) {
        switch (position) {
            case 0:
                bottomNavigationView.setSelectedItemId(R.id.home);
                break;
            case 1:
                bottomNavigationView.setSelectedItemId(R.id.transaction);
                break;
            case 2:
                bottomNavigationView.setSelectedItemId(R.id.chars);
                break;
            case 3:
                bottomNavigationView.setSelectedItemId(R.id.settings);
                break;
        }
    }
}
