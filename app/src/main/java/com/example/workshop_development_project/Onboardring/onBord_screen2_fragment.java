package com.example.workshop_development_project.Onboardring;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view. View;
import android.view.ViewGroup;

import com.example.workshop_development_project.MainActivity;
import com.example.workshop_development_project.R;
import com.example.workshop_development_project.databinding.FragmentOnBordScreen2FragmentBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link onBord_screen2_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class onBord_screen2_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public onBord_screen2_fragment() {
        // Required empty public constructor
    }


    public static onBord_screen2_fragment newInstance() {
        onBord_screen2_fragment fragment = new onBord_screen2_fragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentOnBordScreen2FragmentBinding binding = FragmentOnBordScreen2FragmentBinding.inflate(inflater, container, false);
        binding.nextBtn.setOnClickListener(v -> {
            ViewPager2 viewPager = getActivity().findViewById(R.id.onboardingViewPager);
            if (viewPager != null) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true); // move to next page
            }
        });
        binding.skipBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });

        return binding.getRoot();

    }

}