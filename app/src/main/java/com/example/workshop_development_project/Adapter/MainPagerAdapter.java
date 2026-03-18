package com.example.workshop_development_project.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.workshop_development_project.MainScreens.Chares_fragment;
import com.example.workshop_development_project.MainScreens.Settings_fragment;
import com.example.workshop_development_project.MainScreens.Transaction_fragment;
import com.example.workshop_development_project.MainScreens.home_fragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new home_fragment();
            case 1:
                return new Transaction_fragment();
            case 2:
                return new Chares_fragment();
            case 3:
                return new Settings_fragment();
            default:
                return new home_fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
