package com.example.workshop_development_project.Onboardring;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workshop_development_project.R;
import com.example.workshop_development_project.databinding.FragmentOnBordScreen1FragmentBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link onBord_screen1_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class onBord_screen1_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public onBord_screen1_fragment() {
        // Required empty public constructor
    }



    public static onBord_screen1_fragment newInstance() {
        onBord_screen1_fragment fragment = new onBord_screen1_fragment();
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
        FragmentOnBordScreen1FragmentBinding binding = FragmentOnBordScreen1FragmentBinding.inflate(inflater, container,false);
        binding.nextBtn.setOnClickListener(v -> {
            // Create the second fragment
            ViewPager2 viewPager = binding.getRoot().findViewById(R.id.onboardingViewPager);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1); // move to next page
        });

        return binding.getRoot();
    }
}