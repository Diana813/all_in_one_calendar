package com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.dianaszczepankowska.AllInOneCalendar.android.BuildConfig;
import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.PhotosAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.statistics.StatisticsOfEffectiveness;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.DialogsUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DatabaseUtils.saveImagePathToDb;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DownloadImageUtils.checkCameraHardware;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DownloadImageUtils.createImageFile;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DownloadImageUtils.saveToInternalStorage;

public class LifeAims extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private FloatingActionButton addImage;
    private FloatingActionButton addPhoto;
    private FloatingActionButton searchImage;
    private Bitmap selectedImageBitmap;
    public static String absolutePath;
    private int layout;
    public static Uri uri;
    private static String message;
    private boolean isFABOpen;
    private PhotosAdapter photosAdapter;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public LifeAims() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        photosAdapter = new PhotosAdapter(context);
        LifeAims.context = context;
    }


    public void setContent(int layout) {
        this.layout = layout;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(layout, container, false);
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.LifeAims));

        findViews(rootView);
        setHasOptionsMenu(true);
        setFab();
        setAdapter();
        initData(this, photosAdapter);
        askPermissions();
        checkCameraHardware(getActivity(), addPhoto);
        setAddImageClickListener();
        setAddPhotoOnClickListener();
        setSearchImageOnClickListener();

        if (message != null && !message.equals("")) {
            DialogsUtils.showFillInThisFieldDialog(message, getContext());
        }
        return rootView;
    }


    private void findViews(View rootView) {
        addImage = rootView.findViewById(R.id.fabAddImage);
        addPhoto = rootView.findViewById(R.id.fabAddPhoto);
        searchImage = rootView.findViewById(R.id.fabSearchPhoto);
        fab = rootView.findViewById(R.id.fab);
        recyclerView = rootView.findViewById(R.id.list_of_photos);
    }


    private void setFab() {
        fab.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });
    }


    private void setAdapter() {
        recyclerView.setAdapter(photosAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void showFABMenu() {
        isFABOpen = true;
        addImage.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        addPhoto.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        searchImage.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }


    private void closeFABMenu() {
        isFABOpen = false;
        addImage.animate().translationY(0);
        addPhoto.animate().translationY(0);
        searchImage.animate().translationY(0);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.big_plan_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete_all_entries) {
            DialogsUtils.showDeleteConfirmationDialogImage(context);
            return true;

        } else if (item.getItemId() == R.id.statistics) {
            Intent intent = new Intent(getActivity(), StatisticsOfEffectiveness.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void askPermissions() {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };

        if (!hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), PERMISSIONS, PERMISSION_ALL);
        }
    }


    private void setAddImageClickListener() {
        addImage.setOnClickListener(v -> pickImageFromGallery());
    }


    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    private void setAddPhotoOnClickListener() {
        addPhoto.setOnClickListener(v -> captureFromCamera());
    }


    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Objects.requireNonNull(getContext()), BuildConfig.APPLICATION_ID + ".provider", createImageFile(context)));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void setSearchImageOnClickListener() {
        searchImage.setOnClickListener(v -> pickImageFromInternet());
    }


    private void pickImageFromInternet() {

        Uri uri = Uri.parse("https://www.google.com/search?safe=active&biw=1246&bih=541&tbm=isch&sxsrf=ACYBGNTfJAop7LfaDGQ1xomMicNQswQIOA%3A1574256284909&sa=1&ei=nD7VXfiKN8X66QTigKeQDA&q=beautiful+places+to+live&oq=beautiful&gs_l=img.1.0.35i39j0i67j0l2j0i67l2j0l4.503857.507707..509686...1.0..2.85.1125.18......0....1..gws-wiz-img.....10..35i362i39.5z0co5-nbtg");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (reqCode) {

                case CAMERA_REQUEST_CODE:
                    //aimImage.setImageURI(Uri.parse(absolutePath));
                    saveImagePathToDb(absolutePath, getContext());
                    break;
                case GALLERY_REQUEST_CODE:
                    try {
                        Uri imageUri = data.getData();
                        assert imageUri != null;
                        final InputStream imageStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(imageUri);
                        selectedImageBitmap = BitmapFactory.decodeStream(imageStream);
                        //aimImage.setImageBitmap(selectedImageBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    saveToInternalStorage(selectedImageBitmap, context);
                    saveImagePathToDb(absolutePath, getContext());
                    break;
            }
        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


    static void getMessage(String messageInfo) {
        message = messageInfo;
    }


    @SuppressLint("FragmentLiveDataObserve")
    private void initData(Fragment fragment, final PhotosAdapter adapter) {
        PicturesViewModel picturesViewModel = new ViewModelProvider(fragment).get(PicturesViewModel.class);
        picturesViewModel.getPicturesList().observe(fragment, adapter::setPhotossList);
    }
}