package com.example.wallpapareset.fragments.home;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wallpapareset.R;
import com.example.wallpapareset.models.responce.Categorize;
import com.example.wallpapareset.models.responce.Wallpapares;
import com.example.wallpapareset.databinding.FragmentHomeBinding;
import com.example.wallpapareset.fragments.home.adapter.AllAdapter;
import com.example.wallpapareset.fragments.home.adapter.SuggestionsAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {


    private static final String TAG = "Kia----HomeFr----> ";
    private FragmentHomeBinding binding;
    private SuggestionsAdapter suggestionsAdapter;
    private AllAdapter allAdapter;
    private String title = "جدیدترین ها";
    private ListsViewModel homeViewModel;
    private boolean isNextPage;
//    private boolean isSearch = false;
    private int page;
    private ArrayList<Wallpapares> wallpaparesArrayList = new ArrayList<>();
    private ArrayList<Wallpapares> searchArrayList = new ArrayList<>();
    Wallpapares one = new Wallpapares(0, title);


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeViewModel = new ViewModelProvider(requireActivity()).get(ListsViewModel.class);

        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", getContext().MODE_PRIVATE);

        boolean a = sh.getBoolean("clear", false);
        if(a){
            wallpaparesArrayList.clear();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

            myEdit.putBoolean("clear", false);

            myEdit.commit();
        }


        binding.search.setOnClickListener(view -> {
           Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_searchFragment);
        });
        binding.swipHome.setColorSchemeColors(getResources().getColor(R.color.mainOrange), getResources().getColor(R.color.mainGray));
        binding.swipHome.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.mainBlack));
        binding.swipHome.setOnRefreshListener(() -> {
            wallpaparesArrayList.clear();
                wallpaparesArrayList.add(one);
                binding.includedAllPhotos.progressHorizontal.setVisibility(View.VISIBLE);
                homeViewModel.getAll(1);
                homeViewModel.isAdd = true;


        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.allMainHome.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == binding.allMainHome.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.

                        if (isNextPage) {
                            homeViewModel.getAll(page);
                            homeViewModel.isAdd = true;

                        } else {
                            binding.includedAllPhotos.progressHorizontal.setVisibility(View.INVISIBLE);
//                        //if progress is gone
//                        float scale = getContext().getResources().getDisplayMetrics().density;
//                        int dpAsPixels = (int) (64*scale + 0.5f);
//                        binding.includedAllPhotos.getRoot().setPadding(0,0,0,dpAsPixels);
//                        binding.includedAllPhotos.getRoot().requestLayout();
                        }
                }
            });
        }


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        wallpaparesArrayList.add(one);
        homeViewModel.getSavedData();

        homeViewModelsProgress();
        homeViewModelsListAndCat();





    }

    @Override
    public void onPause() {
        super.onPause();

        homeViewModel.isAdd = false;


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void homeViewModelsProgress() {

        homeViewModel.isLoading.observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                binding.spinKit.setVisibility(View.VISIBLE);
                binding.allMainHome.setVisibility(View.INVISIBLE);
            }else{

                binding.spinKit.setVisibility(View.INVISIBLE);
                binding.allMainHome.setVisibility(View.VISIBLE);

            }
            binding.swipHome.setRefreshing(aBoolean);

        });

        homeViewModel.isConnect.observe(getViewLifecycleOwner(), aBoolean -> {

            if (!aBoolean) {
                dialogConnection();

            }
        });

        homeViewModel.isSuccessList.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                binding.allMainHome.setVisibility(View.VISIBLE);
                binding.errorText.setVisibility(View.INVISIBLE);
            }else{
                binding.allMainHome.setVisibility(View.INVISIBLE);
                binding.errorText.setVisibility(View.VISIBLE);
            }
        });

        homeViewModel.isSuccessCat.observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                binding.recSugg.setVisibility(View.VISIBLE);
            }else{
                binding.recSugg.setVisibility(View.GONE);

            }
        });

//        homeViewModel.isSuccessSearch.observe(getViewLifecycleOwner(), aBoolean -> {
//            if(aBoolean){
//                binding.includedAllPhotos.getRoot().setVisibility(View.VISIBLE);
//                binding.errorText.setVisibility(View.INVISIBLE);
//            }else{
//                binding.includedAllPhotos.getRoot().setVisibility(View.INVISIBLE);
//                binding.errorText.setVisibility(View.VISIBLE);
//            }
//        });

    }



    private void homeViewModelsListAndCat() {

        homeViewModel.wallpaparesMutableLiveData.observe(getViewLifecycleOwner(), wallpapares -> {
            if (wallpapares.isEmpty()) {
                binding.includedAllPhotos.getRoot().setVisibility(View.INVISIBLE);
                binding.errorText.setVisibility(View.VISIBLE);
            } else {

                if (homeViewModel.isAdd) {
                    for (int i = 0; i < wallpapares.size(); i++) {
                        wallpaparesArrayList.add(wallpapares.get(i));
                    }
                }


                recycleImages(wallpaparesArrayList);
            }
        });

        homeViewModel.categorizeMutableLiveData.observe(getViewLifecycleOwner(), categorizes -> {
            recycleCategorys(categorizes);
        });
        homeViewModel.responceImageList.observe(getViewLifecycleOwner(), responceImagesList -> {
            page = responceImagesList.page_next;
            isNextPage = responceImagesList.has_next;
        });

    }




    private void dialogConnection() {
        Dialog dialog = new Dialog(getContext(), R.style.Dialog);
        dialog.setContentView(R.layout.lost_connection_dialog);
        dialog.show();
        MaterialButton again = dialog.findViewById(R.id.again);
        MaterialButton exit = dialog.findViewById(R.id.exit);
        again.setClickable(true);
        again.setOnClickListener(view1 -> {
            homeViewModel.getAll(1);
            again.setClickable(false);
            dialog.dismiss();

        });
        exit.setOnClickListener(view12 -> {
            requireActivity().finish();
        });
    }


    public void recycleImages(ArrayList<Wallpapares> responceImagesLists) {
        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setReverseLayout(false);
        binding.includedAllPhotos.recAll.setLayoutManager(staggeredGridLayoutManager);
        allAdapter = new AllAdapter(responceImagesLists);
        binding.includedAllPhotos.recAll.setAdapter(allAdapter);
        allAdapter.notifyDataSetChanged();
    }

    public void recycleCategorys(ArrayList<Categorize> categorizeArrayList) {
        LinearLayoutManager horiz = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        binding.recSugg.setLayoutManager(horiz);
        suggestionsAdapter = new SuggestionsAdapter(categorizeArrayList);
        binding.recSugg.setAdapter(suggestionsAdapter);
        suggestionsAdapter.notifyDataSetChanged();

    }


}