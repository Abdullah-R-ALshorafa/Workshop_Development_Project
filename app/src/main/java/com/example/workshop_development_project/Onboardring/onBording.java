package com.example.workshop_development_project.Onboardring;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workshop_development_project.R;

public class onBording extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_bording);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_onBord, onBord_screen1_fragment.newInstance()).commit();

    }
}