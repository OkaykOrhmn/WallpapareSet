package com.example.wallpapareset.fragments.layouts;

import android.Manifest;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wallpapareset.R;
import com.example.wallpapareset.api.ResponceSearch;
import com.example.wallpapareset.database.SqlFav;
import com.example.wallpapareset.databinding.FragmentPhotoPageBinding;
import com.example.wallpapareset.fragments.home.ListsViewModel;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class PhotoPageFragment extends Fragment {

    private static final String TAG = "Kia--PhotoPageFr----> ";
    private FragmentPhotoPageBinding binding;
    private Boolean hide = false;
    private Boolean heart = false;
    private SqlFav sqlDatabase;
    private int photoId;
    private String url;
    private PhotoPageViewModel photoViewModel;

    private static int REQUEST_CODE = 100;
    OutputStream outputStream;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPhotoPageBinding.inflate(inflater, container, false);
        photoViewModel = new ViewModelProvider(requireActivity()).get(PhotoPageViewModel.class);

        assert getArguments() != null;
        url = getArguments().getString("url");
        photoId = getArguments().getInt("id");

        sqlDatabase = new SqlFav(getContext());


        sqlDatabase.getData();

        binding.share.setOnClickListener(view -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);

            // type of the content to be shared
            sharingIntent.setType("text/plain");

            // Body of the content
            String shareBody = url;

            // subject of the content. you can share anything
            String shareSubject = "برای به اشتراک گذاری عکس انتخاب کنید:";

            // passing body of the content
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

            // passing subject of the content
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        });

        binding.back.setOnClickListener(view -> {
            Navigation.findNavController(view).popBackStack();
        });

        if (sqlDatabase.getById(photoId)) {
            binding.heart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_favorite_24));
            heart = true;
        } else {

            binding.heart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_favorite_border_24));
            heart = false;
        }


        binding.downloadButtom.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                saveImage(url);
            } else {
                askPermision();
            }

        });

        binding.hide.setOnClickListener(view -> {
            if (hide) {
                binding.heart.setVisibility(View.VISIBLE);
                binding.downloadButtom.setVisibility(View.VISIBLE);
                binding.buttonImage.setVisibility(View.VISIBLE);
                hide = false;

            } else {
                binding.heart.setVisibility(View.INVISIBLE);
                binding.downloadButtom.setVisibility(View.INVISIBLE);
                binding.buttonImage.setVisibility(View.INVISIBLE);
                hide = true;
            }
        });

        binding.heart.setOnClickListener(view -> {
            if (heart) {
                binding.heart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_favorite_border_24));
                sqlDatabase.delete(photoId);
                heart = false;
            } else {
                binding.heart.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_favorite_24));
                sqlDatabase.Insert(photoId, url);
                heart = true;
            }
        });

        binding.buttonImage.setOnClickListener(view -> {

            Dialog dialog = new Dialog(getContext(), R.style.Dialog);
            dialog.setContentView(R.layout.set_wallpapare_dialog);
            dialog.show();
            MaterialButton homeScrn = dialog.findViewById(R.id.home_screen_set);
            MaterialButton lockScrn = dialog.findViewById(R.id.lock_screen_set);
            MaterialButton bothScrn = dialog.findViewById(R.id.both_screen_set);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
//            Drawable drawable = ContextCompat.getDrawable(getContext(), url);
//            assert drawable != null;
//            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();



            homeScrn.setOnClickListener(view1 -> {
//                 set on home
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        wallpaperManager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_SYSTEM);
                    setImagePicasso(wallpaperManager, WallpaperManager.FLAG_SYSTEM);
                }

                dialog.dismiss();

            });

            lockScrn.setOnClickListener(view12 -> {
                //set lock
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        wallpaperManager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK);
                    setImagePicasso(wallpaperManager, WallpaperManager.FLAG_LOCK);

                }

                dialog.dismiss();
            });

            bothScrn.setOnClickListener(view13 -> {
                //setBoth
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        wallpaperManager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK);
//                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                    setImagePicasso(wallpaperManager, 0);
                }

                dialog.dismiss();

            });


//
//


//
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoViewModel.getImage(photoId);

        photoViewModel.namePhoto.observe(getViewLifecycleOwner(), s -> {
            try {
                Picasso.get().load(s).into(binding.imageMain);

            }catch (Exception e){
                binding.allLayout.setVisibility(View.INVISIBLE);
            }
        });

        photoViewModel.isLoading.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.allLayout.setVisibility(View.INVISIBLE);
                binding.spinKit.setVisibility(View.VISIBLE);
            } else {
                binding.spinKit.setVisibility(View.INVISIBLE);
            }

        });

        photoViewModel.isConnect.observe(getViewLifecycleOwner(), aBoolean -> {
            Dialog dialog = new Dialog(getContext(), R.style.Dialog);
            if(!aBoolean){
                binding.allLayout.setVisibility(View.INVISIBLE);
                dialog.setContentView(R.layout.lost_connection_dialog);
                dialog.show();

                MaterialButton again = dialog.findViewById(R.id.again);
                MaterialButton exit = dialog.findViewById(R.id.exit);
                exit.setText("بازگشت");
                again.setClickable(true);

                again.setOnClickListener(view1 -> {
                    photoViewModel.getImage(photoId);
                    again.setClickable(false);
                    dialog.dismiss();

                });

                exit.setOnClickListener(view12 -> {

                    Navigation.findNavController(getView()).popBackStack();
                    dialog.dismiss();
                });
            }
        });

        photoViewModel.isSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                binding.allLayout.setVisibility(View.VISIBLE);
                binding.errorText.setVisibility(View.INVISIBLE);
            }else{

                binding.allLayout.setVisibility(View.INVISIBLE);
                binding.spinKit.setVisibility(View.INVISIBLE);
                binding.errorText.setVisibility(View.VISIBLE);


            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        photoViewModel.namePhoto.setValue("");
    }

    @Override
    public void onResume() {
        super.onResume();
        photoViewModel.getImage(photoId);

    }

    private void askPermision() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);


        Toast.makeText(getContext(), "برای دانلود عکس باید مجوز دسترسی فعال باشد.", Toast.LENGTH_SHORT).show();

    }

    private void saveImage(String link) {
        String name = url.replaceAll("https://python.aks14.com/media/images/", "");
        String imageName = name + ".PNG";

        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/DCIM", "wall");
        dir.mkdirs();
        File file = new File(dir, imageName);

        File[] listFile = file.listFiles();

        File result = new File(dir + "/" + imageName);

        Dialog dialog = new Dialog(getContext(), R.style.Dialog);
        dialog.setContentView(R.layout.loading_dialog);


        if (result.exists()) {
            Toast.makeText(getContext(), "فایل موجوده.", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            Picasso.get().load(link).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    try {
                        outputStream = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        dialog.dismiss();

                        Toast.makeText(getContext(), "عکس با موفقیت دانلود شد.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                        dialog.dismiss();
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Loading image failed", Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.d(TAG, "onPrepareLoad: ");

                }
            });

        }


    }

    //WhitePicasso
    private void setImagePicasso(WallpaperManager wallpaperManager, int witch) {
        Picasso.get().load(url).into(new Target() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    if(witch == 0){
                        wallpaperManager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK);
                        wallpaperManager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_SYSTEM);

                    }else{
                        wallpaperManager.setBitmap(bitmap, null, false, witch);

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Log.d("TAG", "onBitmapLoaded: ");
                switch(witch){
                    case WallpaperManager.FLAG_SYSTEM:
                        Toast.makeText(getContext(), "عکس با موفقیت برای صفحه اصلی انتخاب شد", Toast.LENGTH_SHORT).show();
                        break;
                    case WallpaperManager.FLAG_LOCK:
                        Toast.makeText(getContext(), "عکس با موفقیت برای صفحه قفل انتخاب شد", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        Toast.makeText(getContext(), "عکس با موفقیت برای صفحه اصلی و قفل انتخاب شد", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
                Toast.makeText(getContext(), "Loading image failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

                Log.d("TAG", "Prepare Load");
                Toast.makeText(getContext(), "درحال بارگذاری", Toast.LENGTH_SHORT).show();
            }

        });
    }


}