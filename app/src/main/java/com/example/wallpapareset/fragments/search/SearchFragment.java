package com.example.wallpapareset.fragments.search;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.example.wallpapareset.databinding.FragmentSearchBinding;
import com.example.wallpapareset.fragments.home.adapter.AllAdapter;
import com.example.wallpapareset.models.responce.Wallpapares;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private static final String TAG = "Kia----SearchFr----> ";
    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;
    private AllAdapter allAdapter;
    private String titleSearch = "";
    private ArrayList<Wallpapares> wallpaparesArrayList = new ArrayList<>();
    private boolean isFirst = true;


    private boolean isNextPageSearch;

    private int pageSearch;


    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        binding.editSearch.setText("");




        ////////////////////----------~~ <onBackPress> ~~----------////////////////////

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().getViewModelStore().clear();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref",getContext().MODE_PRIVATE);

                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit.putBoolean("clear", true);

                myEdit.commit();
                Navigation.findNavController(requireView()).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
//
//        ////////////////////----------~~ <onBackPress> ~~----------////////////////////


        if (binding.editSearch.getText().toString().length() == 0) {
            binding.includedAllPhotos.progressHorizontal.setVisibility(View.INVISIBLE);
            binding.errorText.setVisibility(View.VISIBLE);
            binding.errorText.setText("کلمه مورد نظر رو وارد کنید");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.allLayout.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY == binding.allLayout.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.

                    if (isNextPageSearch) {
                        searchViewModel.getAllSearch(titleSearch, pageSearch);
                        searchViewModel.isAdd = true;


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


        binding.editSearch.requestFocus();

        showSoftKeyboard(requireActivity());
        binding.editSearch.onEditorAction(EditorInfo.IME_ACTION_DONE);
            binding.editSearch.setOnEditorActionListener((v, keyCode, event) -> {
                hideSoftKeyboard(requireActivity());


                return true;
            });







                binding.editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (searchViewModel.isAdd) {
                            if (charSequence.toString().length() != 0) {
                                binding.includedAllPhotos.progressHorizontal.setVisibility(View.VISIBLE);
                                wallpaparesArrayList.clear();
                                Wallpapares one = new Wallpapares(0, charSequence.toString());
                                wallpaparesArrayList.add(one);
                                searchViewModel.getAllSearch(charSequence.toString(), 1);
                                titleSearch = charSequence.toString();
                                Log.d(TAG, "onTextChanged: ");
                            }
                        }


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {



                    }
                });





        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModelsSearch();
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        }
    }

    private static void showSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void homeViewModelsSearch() {

        searchViewModel.responceSearch.observe(getViewLifecycleOwner(), responceImagesList -> {
            pageSearch = responceImagesList.page_next;
            isNextPageSearch = responceImagesList.has_next;
        });

        searchViewModel.searchMutableLiveData.observe(getViewLifecycleOwner(), wallpapares -> {
            if (wallpapares.isEmpty()) {
                binding.includedAllPhotos.getRoot().setVisibility(View.INVISIBLE);
                binding.errorText.setText("لیست خالی است");
                binding.errorText.setVisibility(View.VISIBLE);
            } else {
                binding.includedAllPhotos.getRoot().setVisibility(View.VISIBLE);
                binding.errorText.setVisibility(View.INVISIBLE);

                if (searchViewModel.isAdd) {
                    for (int i = 0; i < wallpapares.size(); i++) {
                        wallpaparesArrayList.add(wallpapares.get(i));
                    }
                }


                recycleImages(wallpaparesArrayList);
            }

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

    @Override
    public void onPause() {
        super.onPause();
        searchViewModel.isAdd = false;
        getActivity().getViewModelStore().clear();

    }

}