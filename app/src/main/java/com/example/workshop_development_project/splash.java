package com.example.workshop_development_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash extends AppCompatActivity {

    ProgressBar progressBar;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBar);

        Handler handler = new Handler();

        new Thread(() -> {

            while (progress <= 100) {

                try {
                    Thread.sleep(30); // speed of loading
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                progress++;

                handler.post(() -> progressBar.setProgress(progress));
            }

            Intent intent = new Intent(splash.this, MainActivity.class);
            startActivity(intent);
            finish();

        }).start();
    }
}