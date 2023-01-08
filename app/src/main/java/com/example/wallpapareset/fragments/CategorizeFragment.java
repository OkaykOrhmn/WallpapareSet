package com.example.wallpapareset.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallpapareset.R;
import com.example.wallpapareset.adapter.AllAdapter;
import com.example.wallpapareset.adapter.CategorizeAdapter;
import com.example.wallpapareset.adapter.ColorAdapter;
import com.example.wallpapareset.adapter.Suggestions;
import com.example.wallpapareset.adapter.SuggestionsAdapter;
import com.example.wallpapareset.databinding.FragmentCategorizeBinding;
import com.example.wallpapareset.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class CategorizeFragment extends Fragment {

    private FragmentCategorizeBinding binding;
    private CategorizeAdapter categorizeAdapter;
    private ColorAdapter colorAdapter;
    private ArrayList<Suggestions> suggestions = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategorizeBinding.inflate(inflater, container, false);

        colors.clear();
        colors.add(R.drawable.background_sugg_1);
        colors.add(R.drawable.background_sugg_2);
        colors.add(R.drawable.background_sugg_3);
        colors.add(R.drawable.background_sugg_4);
        colors.add(R.drawable.background_sugg_5);

        LinearLayoutManager horizColors = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        binding.recColors.setLayoutManager(horizColors);
        colorAdapter = new ColorAdapter(colors);
        binding.recColors.setAdapter(colorAdapter);
        colorAdapter.notifyDataSetChanged();


        suggestions.clear();
        Suggestions suggestions0 = new Suggestions("ماشین ها", R.drawable.image5);
        Suggestions suggestions1 = new Suggestions("ورزشی", R.drawable.image5);
        Suggestions suggestions2 = new Suggestions("منظره", R.drawable.image5);
        Suggestions suggestions3 = new Suggestions("تکنولوژی", R.drawable.image5);

        Suggestions suggestions4 = new Suggestions("ماشین ها", R.drawable.image5);
        Suggestions suggestions5 = new Suggestions("ورزشی",R.drawable.image5);
        Suggestions suggestions6 = new Suggestions("منظره", R.drawable.image5);
        Suggestions suggestions7 = new Suggestions("تکنولوژی", R.drawable.image5);

        suggestions.add(suggestions0);
        suggestions.add(suggestions1);
        suggestions.add(suggestions2);
        suggestions.add(suggestions3);
        suggestions.add(suggestions4);
        suggestions.add(suggestions5);
        suggestions.add(suggestions6);
        suggestions.add(suggestions7);



        GridLayoutManager horizCat = new GridLayoutManager(getContext(), 2);
        binding.includedCat.recAll.setLayoutManager(horizCat);
        categorizeAdapter = new CategorizeAdapter(suggestions);
        binding.includedCat.recAll.setAdapter(categorizeAdapter);
        categorizeAdapter.notifyDataSetChanged();


        return binding.getRoot();
    }
}