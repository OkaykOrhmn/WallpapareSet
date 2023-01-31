package com.example.wallpapareset.fragments.layouts;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallpapareset.R;
import com.example.wallpapareset.models.responce.ResponceCategorysList;
import com.example.wallpapareset.models.responce.Wallpapares;
import com.example.wallpapareset.fragments.categorize.CategorizeListViewModel;
import com.example.wallpapareset.fragments.home.adapter.AllAdapter;
import com.example.wallpapareset.databinding.FragmentListBinding;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private static final String TAG = "Kia---ListFr----> ";
    private FragmentListBinding binding;
    private AllAdapter allAdapter;
    private ArrayList<Wallpapares> all = new ArrayList<>();
    private String title;
    private int background;
    private CategorizeListViewModel listsViewModel;
    private boolean isNextPage;
    private int page;
    private ArrayList<Wallpapares> wallpaparesArrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);
        listsViewModel = new ViewModelProvider(requireActivity()).get(CategorizeListViewModel.class);
        assert getArguments() != null;
        title = getArguments().getString("title");
//        title = "";

        Wallpapares wallpapares1 = new Wallpapares(0, title);
        wallpaparesArrayList.add(wallpapares1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.a.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == binding.a.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    if (isNextPage) {
                        listsViewModel.getAllList(title, page);

                    } else {
                        binding.includedList.progressHorizontal.setVisibility(View.GONE);
//                        //if progress is gone
//                        float scale = getContext().getResources().getDisplayMetrics().density;
//                        int dpAsPixels = (int) (32*scale + 0.5f);
//                        binding.includedList.recAll.setPadding(0,0,0,dpAsPixels);
//                        binding.includedList.recAll.requestLayout();
                    }
                }
            });
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listsViewModel.getAllList(title, 1);

        listsViewModel.wallpaparesMutableLiveData.observe(getViewLifecycleOwner(), wallpapares -> {

            wallpaparesArrayList.addAll(wallpapares);
            recCategorys(wallpaparesArrayList);
        });

        listsViewModel.isLoading.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.includedList.getRoot().setVisibility(View.INVISIBLE);
                binding.errorText.setVisibility(View.INVISIBLE);
                binding.spinKit.setVisibility(View.VISIBLE);
            } else {
                binding.includedList.getRoot().setVisibility(View.VISIBLE);
                binding.spinKit.setVisibility(View.INVISIBLE);
            }
        });

        listsViewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.a.setVisibility(View.VISIBLE);
                binding.errorText.setVisibility(View.INVISIBLE);

            } else {

                binding.a.setVisibility(View.INVISIBLE);
                binding.errorText.setVisibility(View.VISIBLE);

            }
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
                    listsViewModel.getAllList(title, 1);
                    again.setClickable(false);
                    dialog.dismiss();

                });

                exit.setOnClickListener(view12 -> {
                    requireActivity().finish();
                });
            }
        });

        listsViewModel.responceCategorysListMutableLiveData.observe(getViewLifecycleOwner(), responceCategorysList -> {
            isNextPage = responceCategorysList.has_next;
            page = responceCategorysList.page_next;
        });

    }

    private void recCategorys(ArrayList<Wallpapares> wallpapares) {
        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setReverseLayout(false);
        binding.includedList.recAll.setLayoutManager(staggeredGridLayoutManager);
        allAdapter = new AllAdapter(wallpapares);
        binding.includedList.recAll.setAdapter(allAdapter);
        allAdapter.notifyDataSetChanged();
    }
}