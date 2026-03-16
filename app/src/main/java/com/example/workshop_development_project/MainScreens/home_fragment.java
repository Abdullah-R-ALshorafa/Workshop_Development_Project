package com.example.workshop_development_project.MainScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.workshop_development_project.Adapter.TransactionAdapter;
import com.example.workshop_development_project.Adapter.TransactionAdapter;
import com.example.workshop_development_project.Database.FinanceRoomDatabase;
import com.example.workshop_development_project.Database.FinanceViewModel;
import com.example.workshop_development_project.Helper.TransactionType;
import com.example.workshop_development_project.Model.Categorys;
import com.example.workshop_development_project.Model.Transactions;
import com.example.workshop_development_project.R;
import com.example.workshop_development_project.databinding.FragmentHomeFragmentBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_fragment extends Fragment {
    RecyclerView recyclerView;
    TransactionAdapter adapter;
    FragmentHomeFragmentBinding binding;
    ArrayList<Transactions> transactions;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static home_fragment newInstance(String param1, String param2) {
        home_fragment fragment = new home_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        binding = FragmentHomeFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Transactions> transactions = new ArrayList<>();
        adapter = new TransactionAdapter(transactions);
        recyclerView.setAdapter(adapter);

        FinanceViewModel model = new ViewModelProvider(this).get(FinanceViewModel.class);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.groceries_icon);
        model.insertCategory(new Categorys("buy a house", Color.CYAN,bitmap));
        model.insertTransaction(new Transactions(550, TransactionType.INCOME,2,new Date(),"hararh"));


        model.getTransactionsEpense().observe(getViewLifecycleOwner(), total -> {

            if(total == null) total = 0.0;

            binding.tvExpense.setText(String.valueOf(total));

        });

        model.gitBalance().observe(getViewLifecycleOwner(), balance -> {

            if(balance == null) balance = 0.0;

            binding.tvBalance.setText("$" + balance);

        });
        model.getTransactionsIncome().observe(getViewLifecycleOwner(), balance -> {

            if(balance == null) balance = 0.0;

            binding.revenueValue.setText("$" + balance);

        });


        model.getAllTransaction().observe(getViewLifecycleOwner(), list -> {

            transactions.clear();
            transactions.addAll(list);

            adapter.notifyDataSetChanged();

        });
    }
}