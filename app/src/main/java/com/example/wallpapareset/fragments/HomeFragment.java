package com.example.wallpapareset.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wallpapareset.adapter.AllAdapter;
import com.example.wallpapareset.R;
import com.example.wallpapareset.adapter.Suggestions;
import com.example.wallpapareset.adapter.SuggestionsAdapter;
import com.example.wallpapareset.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private SuggestionsAdapter suggestionsAdapter;
    private AllAdapter allAdapter;
    private ArrayList<Suggestions> suggestions = new ArrayList<>();
    private ArrayList<Integer> all = new ArrayList<>();
    private String title = "جدیدترین ها";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.swipHome.setColorSchemeColors(getResources().getColor(R.color.mainOrange), getResources().getColor(R.color.mainGray));
        binding.swipHome.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.mainBlack));
        binding.swipHome.setOnRefreshListener(() -> {
            binding.swipHome.setRefreshing(false);

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

        suggestions.clear();
        Suggestions suggestions0 = new Suggestions("ماشین ها", R.drawable.icons_cars);
        Suggestions suggestions1 = new Suggestions("ورزشی", R.drawable.icons_soccer_ball);
        Suggestions suggestions2 = new Suggestions("منظره", R.drawable.icons_forest);
        Suggestions suggestions3 = new Suggestions("تکنولوژی", R.drawable.icons_electronics);

        Suggestions suggestions4 = new Suggestions("ماشین ها", R.drawable.icons_cars);
        Suggestions suggestions5 = new Suggestions("ورزشی", R.drawable.icons_soccer_ball);
        Suggestions suggestions6 = new Suggestions("منظره", R.drawable.icons_forest);
        Suggestions suggestions7 = new Suggestions("تکنولوژی", R.drawable.icons_electronics);

        suggestions.add(suggestions0);
        suggestions.add(suggestions1);
        suggestions.add(suggestions2);
        suggestions.add(suggestions3);
        suggestions.add(suggestions4);
        suggestions.add(suggestions5);
        suggestions.add(suggestions6);
        suggestions.add(suggestions7);



        LinearLayoutManager horiz = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true);
        binding.recSugg.setLayoutManager(horiz);
        suggestionsAdapter = new SuggestionsAdapter(suggestions);
        binding.recSugg.setAdapter(suggestionsAdapter);
        suggestionsAdapter.notifyDataSetChanged();



        all.clear();
//        all.add(R.drawable.image1);
//        all.add(R.drawable.image2);
//        all.add(R.drawable.image4);
//        all.add(R.drawable.image5);
//        all.add(R.drawable.image6);
//
//        all.add(R.drawable.image5);
//        all.add(R.drawable.image1);
//        all.add(R.drawable.image4);
//        all.add(R.drawable.image6);
//        all.add(R.drawable.image2);

        all.add(R.color.white);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);
        all.add(R.drawable.image5);










        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setReverseLayout(false);
        binding.includedAllPhotos.recAll.setLayoutManager(staggeredGridLayoutManager);
        allAdapter = new AllAdapter(all, title);
        binding.includedAllPhotos.recAll.setAdapter(allAdapter);
        allAdapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        return binding.getRoot();
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

}