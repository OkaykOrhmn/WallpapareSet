package com.example.wallpapareset.fragments.layouts;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.Log;
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
import java.util.Objects;

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
        wallpaparesArrayList.clear();
        wallpaparesArrayList.add(wallpapares1);

        //////////////////----------~~ <onBackPress> ~~----------////////////////////

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                listsViewModel.isAdd = true;
                Navigation.findNavController(getView()).popBackStack();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        ////////////////////----------~~ <onBackPress> ~~----------////////////////////

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.a.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == binding.a.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    if (isNextPage) {
                        listsViewModel.getAllList(title, page);
                        listsViewModel.isAdd = true;


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

        listsViewModel.getAll(title);

        listsViewModel.wallpaparesMutableLiveData.observe(getViewLifecycleOwner(), wallpapares -> {
            if (wallpapares.isEmpty()){
                binding.includedList.getRoot().setVisibility(View.INVISIBLE);
                binding.errorText.setText("لیست خالی است");
                binding.errorText.setVisibility(View.VISIBLE);
            }else {

                if(listsViewModel.isAdd){
                    for (int i = 0; i < wallpapares.size(); i++) {
                        wallpaparesArrayList.add(wallpapares.get(i));
                    }
                }else{
                    listsViewModel.isAdd = true;
                }




                recCategorys(wallpaparesArrayList);
            }
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
                binding.errorText.setText("خطا در بارگیری اطلاعات لطفا دوباره امتحان کنید!!");
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

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onStop() {
        super.onStop();
        listsViewModel.isAdd = false;
        requireActivity().getViewModelStore().clear();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref",getContext().MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putBoolean("clear", true);

        myEdit.commit();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listsViewModel.isAdd = true;

    }
}