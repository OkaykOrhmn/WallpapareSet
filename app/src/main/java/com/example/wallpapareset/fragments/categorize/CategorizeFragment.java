package com.example.wallpapareset.fragments.categorize;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallpapareset.R;
import com.example.wallpapareset.models.responce.Categorize;
import com.example.wallpapareset.fragments.home.ListsViewModel;
import com.example.wallpapareset.databinding.FragmentCategorizeBinding;
import com.example.wallpapareset.fragments.categorize.adapter.CategorizeAdapter;
import com.example.wallpapareset.fragments.categorize.adapter.ColorAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class CategorizeFragment extends Fragment {

    private FragmentCategorizeBinding binding;
    private CategorizeAdapter categorizeAdapter;
    private ColorAdapter colorAdapter;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ListsViewModel listsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategorizeBinding.inflate(inflater, container, false);
        listsViewModel = new ViewModelProvider(requireActivity()).get(ListsViewModel.class);

        binding.includedCat.progressHorizontal.setVisibility(View.INVISIBLE);

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


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listsViewModel.categorizeMutableLiveData.observe(getViewLifecycleOwner(), categorizes -> {
//            ArrayList<Categorize> categorizeArrayList = new ArrayList<>();
//            Categorize categorize = new Categorize(5, "dadsfa");
//            Categorize categorize1 = new Categorize(6, "fsafsa");
//            Categorize categorize2 = new Categorize(7, "fsafs");
//            Categorize categorize3 = new Categorize(8, "sfasfa");
//            Categorize categorize4 = new Categorize(9, "fsfa");
//            categorizeArrayList.add(categorize);
//            categorizeArrayList.add(categorize1);
//            categorizeArrayList.add(categorize2);
//            categorizeArrayList.add(categorize3);
//            categorizeArrayList.add(categorize4);
//            categorizeArrayList.addAll(categorizes);
            recycleCat(categorizes);
        });

        listsViewModel.isConnect.observe(getViewLifecycleOwner(), aBoolean -> {
            Dialog dialog = new Dialog(getContext(), R.style.Dialog);
            if (!aBoolean) {
                dialog.setContentView(R.layout.lost_connection_dialog);
                dialog.show();

                MaterialButton again = dialog.findViewById(R.id.again);
                MaterialButton exit = dialog.findViewById(R.id.exit);
                again.setClickable(true);

                again.setOnClickListener(view1 -> {
                    listsViewModel.getAll(1);
                    again.setClickable(false);
                    dialog.dismiss();

                });

                exit.setOnClickListener(view12 -> {
                    requireActivity().finish();
                });
            }
        });

        listsViewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.includedCat.getRoot().setVisibility(View.VISIBLE);
                binding.errorText.setVisibility(View.INVISIBLE);


            } else {

                binding.includedCat.getRoot().setVisibility(View.INVISIBLE);
                binding.spinKit.setVisibility(View.INVISIBLE);
                binding.errorText.setVisibility(View.VISIBLE);


            }
        });

        listsViewModel.isLoading.observe(getViewLifecycleOwner(), aBoolean -> {
            binding.includedCat.getRoot().setVisibility(View.INVISIBLE);
            binding.spinKit.setVisibility(View.VISIBLE);

            Handler h = new Handler();
            Runnable r = () -> {
                if (aBoolean) {
                    binding.includedCat.getRoot().setVisibility(View.INVISIBLE);
                    binding.spinKit.setVisibility(View.VISIBLE);
                } else {

                    binding.includedCat.getRoot().setVisibility(View.VISIBLE);
                    binding.spinKit.setVisibility(View.INVISIBLE);

                }
            };
            h.postDelayed(r, 1000);

        });

    }

    private void recycleCat(ArrayList<Categorize> categorizes) {
        GridLayoutManager horizCat = new GridLayoutManager(getContext(), 2);
        binding.includedCat.recAll.setLayoutManager(horizCat);
        categorizeAdapter = new CategorizeAdapter(categorizes);
        binding.includedCat.recAll.setAdapter(categorizeAdapter);
        categorizeAdapter.notifyDataSetChanged();
    }
}