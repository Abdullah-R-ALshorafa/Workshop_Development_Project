package com.example.workshop_development_project.Onboardring;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.workshop_development_project.Adapter.OnboardingAdapter;
import com.example.workshop_development_project.R;

public class onBording extends AppCompatActivity {

    private ViewPager2 onboardingViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_bording);

        onboardingViewPager = findViewById(R.id.onboardingViewPager);
        OnboardingAdapter adapter = new OnboardingAdapter(this);
        onboardingViewPager.setAdapter(adapter);

    }
}