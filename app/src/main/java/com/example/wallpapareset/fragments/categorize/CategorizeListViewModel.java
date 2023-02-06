package com.example.wallpapareset.fragments.categorize;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.wallpapareset.models.responce.Categorize;
import com.example.wallpapareset.models.responce.ResponceCategorysList;
import com.example.wallpapareset.models.responce.Wallpapares;
import com.example.wallpapareset.repositorys.CategorysListRepository;
import com.example.wallpapareset.utils.Connectivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorizeListViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private final CategorysListRepository imagesListRepository = new CategorysListRepository();
    public MutableLiveData<ArrayList<Wallpapares>> wallpaparesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponceCategorysList> responceCategorysListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> isConnect = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    private ArrayList<Wallpapares> groupOfWall = new ArrayList<>();
    private ArrayList<Categorize> groupOfCat = new ArrayList<>();
    public boolean isAdd = true;
    public int wichPage = 1;
    public int pos = 0;





    public CategorizeListViewModel(@NonNull Application application) {
        super(application);
        this.context = application;

    }

    public void getAll(String cat) {

        if (groupOfWall.size() == 0 && groupOfCat.size() == 0) {
            //call api
            isLoading.setValue(true);
            if(Connectivity.isConnected(context)){
                isConnect.setValue(true);
                getList(cat,wichPage);
            }else{
                isConnect.setValue(false);
                isSuccess.setValue(false);

            }

            isLoading.setValue(false);

        } else {
            //return value
            isLoading.setValue(false);
            wallpaparesMutableLiveData.setValue(groupOfWall);

        }


    }




    public void getAllList(String cat, int page){
        if (Connectivity.isConnected(context)){
            isLoading.setValue(true);
            getList(cat,page);
            isLoading.setValue(false);
        }else{
            isLoading.setValue(false);
            isConnect.setValue(false);
            isSuccess.setValue(false);



        }
        }

    private void getList(String cat, int page){
                 Call<ResponceCategorysList> call = imagesListRepository.getAllCategorys(cat, page);
            call.enqueue(new Callback<ResponceCategorysList>() {
                @Override
                public void onResponse(Call<ResponceCategorysList> call, Response<ResponceCategorysList> response) {
                    if(response.isSuccessful()){
                        responceCategorysListMutableLiveData.setValue(response.body());
                        wallpaparesMutableLiveData.setValue(response.body().wallpapares);
                        groupOfWall.clear();
                        groupOfWall.addAll(response.body().wallpapares);
                        isSuccess.setValue(true);
                        if(response.body().has_next){
                            wichPage=response.body().page_next;
                        }
                    }else{
                        isSuccess.setValue(false);


                    }


                }

                @Override
                public void onFailure(Call<ResponceCategorysList> call, Throwable t) {
                    isSuccess.setValue(false);



                }
            });



    }

}
