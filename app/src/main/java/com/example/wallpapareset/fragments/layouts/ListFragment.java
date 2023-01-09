package com.example.wallpapareset.fragments.layouts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallpapareset.R;
import com.example.wallpapareset.fragments.home.adapter.AllAdapter;
import com.example.wallpapareset.databinding.FragmentListBinding;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String TAG = "Kia---ListFr----> ";
    private FragmentListBinding binding;
    private AllAdapter allAdapter;
    private ArrayList<Integer> all = new ArrayList<>();
    private String title ;
    private int background ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            if (getArguments().getString("title") != null){
                title = getArguments().getString("title");
                background = R.color.white;

            }else if(getArguments().get("back") != null){
                background = getArguments().getInt("back");
                title = "رنگ";
            }
        }else{
            title = "";
            background = R.color.white;
        }

        Log.d(TAG, " background is -> "+ background);


        all.clear();
        all.add(background);
        for (int i = 0; i < 22; i++) {
            all.add(R.drawable.image5);

        }

        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setReverseLayout(false);
        binding.includedList.recAll.setLayoutManager(staggeredGridLayoutManager);
        allAdapter = new AllAdapter(all, title);
        binding.includedList.recAll.setAdapter(allAdapter);
        allAdapter.notifyDataSetChanged();


        return binding.getRoot();
    }
}