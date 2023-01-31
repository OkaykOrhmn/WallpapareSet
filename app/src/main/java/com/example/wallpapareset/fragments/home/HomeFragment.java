package com.example.wallpapareset.fragments.home;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.wallpapareset.R;
import com.example.wallpapareset.models.responce.Categorize;
import com.example.wallpapareset.models.responce.ResponceImagesList;
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
    private int page;
    long currentVisiblePosition = 0;
    private ArrayList<Wallpapares> wallpaparesArrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeViewModel = new ViewModelProvider(requireActivity()).get(ListsViewModel.class);


        Wallpapares one = new Wallpapares(0, title);
        wallpaparesArrayList.add(one);
        homeViewModel.getHome();







        binding.swipHome.setColorSchemeColors(getResources().getColor(R.color.mainOrange), getResources().getColor(R.color.mainGray));
        binding.swipHome.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.mainBlack));
        binding.swipHome.setOnRefreshListener(() -> {
            wallpaparesArrayList.clear();
            wallpaparesArrayList.add(one);
            binding.includedAllPhotos.progressHorizontal.setVisibility(View.VISIBLE);
            homeViewModel.getAll(1);



        });

        binding.editSearch.onEditorAction(EditorInfo.IME_ACTION_DONE);

        binding.editSearch.setOnEditorActionListener((v, keyCode, event) -> {
            Toast.makeText(getContext(), "edittext.getText()", Toast.LENGTH_SHORT).show();
            hideSoftKeyboard(requireActivity());


            return true;
        });

        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() == 0){
                    binding.recSugg.setVisibility(View.VISIBLE);


                }else{
                    binding.recSugg.setVisibility(View.GONE);


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.allMainHome.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == binding.allMainHome.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    if(isNextPage){
                        homeViewModel.getAll(page);

                    }else{
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

        homeViewModel.wallpaparesMutableLiveData.observe(getViewLifecycleOwner(), wallpapares -> {
            if (wallpapares.isEmpty()){
                binding.includedAllPhotos.getRoot().setVisibility(View.INVISIBLE);
                binding.errorText.setVisibility(View.VISIBLE);
            }else {

                for (int i = 0; i < wallpapares.size(); i++) {
                    wallpaparesArrayList.add(wallpapares.get(i));
                }



                recycleImages(wallpaparesArrayList);
            }
        });

        homeViewModel.categorizeMutableLiveData.observe(getViewLifecycleOwner(), categorizes -> {
            recycleCategorys(categorizes);
        });

        homeViewModel.isLoading.observe(getViewLifecycleOwner(), aBoolean -> {
            binding.recSugg.setVisibility(View.INVISIBLE);
            binding.includedAllPhotos.getRoot().setVisibility(View.INVISIBLE);
            binding.spinKit.setVisibility(View.VISIBLE);

//            Handler h = new Handler();
//            Runnable r = () -> {
            if(aBoolean){
                binding.recSugg.setVisibility(View.INVISIBLE);
                binding.includedAllPhotos.getRoot().setVisibility(View.INVISIBLE);
                binding.spinKit.setVisibility(View.VISIBLE);
            }else{

                binding.recSugg.setVisibility(View.VISIBLE);
                binding.includedAllPhotos.getRoot().setVisibility(View.VISIBLE);
                binding.spinKit.setVisibility(View.INVISIBLE);

            }
                binding.swipHome.setRefreshing(aBoolean);
//            };
//            h.postDelayed(r,1000);

        });

        homeViewModel.isConnect.observe(getViewLifecycleOwner(), aBoolean -> {
            Dialog dialog = new Dialog(getContext(), R.style.Dialog);
            if(!aBoolean){
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
        });

        homeViewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                binding.allMainHome.setVisibility(View.VISIBLE);
                binding.errorText.setVisibility(View.INVISIBLE);


            }else{

                binding.allMainHome.setVisibility(View.INVISIBLE);
                binding.spinKit.setVisibility(View.INVISIBLE);
                binding.errorText.setVisibility(View.VISIBLE);


            }
        });

        homeViewModel.isEmpty.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                binding.errorText.setVisibility(View.VISIBLE);
                binding.allMainHome.setVisibility(View.INVISIBLE);
            }else {
                binding.errorText.setVisibility(View.INVISIBLE);
                binding.allMainHome.setVisibility(View.VISIBLE);
            }
        });

        homeViewModel.arrayListMutableLiveData.observe(getViewLifecycleOwner(), responceImagesList -> {
            page = responceImagesList.page_next;
            isNextPage = responceImagesList.has_next;
        });

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public void recycleImages(ArrayList<Wallpapares> responceImagesLists){
        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setReverseLayout(false);
        binding.includedAllPhotos.recAll.setLayoutManager(staggeredGridLayoutManager);
        allAdapter = new AllAdapter(responceImagesLists);
        binding.includedAllPhotos.recAll.setAdapter(allAdapter);
        allAdapter.notifyDataSetChanged();
    }

    public void recycleCategorys(ArrayList<Categorize> categorizeArrayList){
        LinearLayoutManager horiz = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        binding.recSugg.setLayoutManager(horiz);
        suggestionsAdapter = new SuggestionsAdapter(categorizeArrayList);
        binding.recSugg.setAdapter(suggestionsAdapter);
        suggestionsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();


    }
}