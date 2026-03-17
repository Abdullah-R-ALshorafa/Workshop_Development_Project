package com.example.workshop_development_project.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.workshop_development_project.Onboardring.onBord_screen1_fragment;
import com.example.workshop_development_project.Onboardring.onBord_screen2_fragment;
import com.example.workshop_development_project.Onboardring.onBord_screen3_fragment;

public class OnboardingAdapter extends FragmentStateAdapter {

    public OnboardingAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new onBord_screen1_fragment();
            case 1: return new onBord_screen2_fragment();
            case 2: return new onBord_screen3_fragment();
            default: return new onBord_screen1_fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // total number of onboarding screens
    }
}
