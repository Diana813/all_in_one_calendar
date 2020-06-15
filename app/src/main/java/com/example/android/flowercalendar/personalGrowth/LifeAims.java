package com.example.android.flowercalendar.personalGrowth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.flowercalendar.BuildConfig;
import com.example.android.flowercalendar.utils.AppUtils;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.ImagePath;
import com.example.android.flowercalendar.database.ImagePathDao;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static com.example.android.flowercalendar.database.CalendarDatabase.getDatabase;

public class LifeAims extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private ImageView aimImage;
    private ImageView addImage;
    private ImageView addPhoto;
    private ImageView searchImage;
    private Bitmap selectedImageBitmap;
    private String absolutePath;
    private int layout;
    private AppUtils appUtils = new AppUtils();

    public LifeAims() {
        // Required empty public constructor
    }

    public void setContent(int layout) {
        this.layout = layout;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (layout == 0) {
            layout = R.layout.activity_life_aims;
        }
        ViewGroup rootView = (ViewGroup) inflater.inflate(layout, container, false);

        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.LifeAims));

        aimImage = rootView.findViewById(R.id.aimImage);
        addImage = rootView.findViewById(R.id.addImage);
        addPhoto = rootView.findViewById(R.id.addPhoto);
        searchImage = rootView.findViewById(R.id.searchPhoto);

        appUtils.displayImageFromDB(aimImage);
        askPermissions();
        checkCameraHardware(getActivity());
        setAddImageClickListener();
        setAddPhotoOnClickListener();
        setSearchImageOnClickListener();
        getImageFromInternet();
        return rootView;
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

    private void checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            addPhoto.setVisibility(View.VISIBLE);
        } else {
            addPhoto.setVisibility(View.GONE);
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

    private void saveToInternalStorage(Bitmap bitmapImage) {

        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File aimImagePath = new File(directory, "aimImage.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(aimImagePath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 10, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        absolutePath = directory.getAbsolutePath();
    }


    private void setAddPhotoOnClickListener() {
        addPhoto.setOnClickListener(v -> captureFromCamera());
    }

    private void captureFromCamera() {

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Objects.requireNonNull(getContext()), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {

        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        absolutePath = image.getAbsolutePath();
        return image;
    }

    private void setSearchImageOnClickListener() {
        searchImage.setOnClickListener(v -> pickImageFromInternet());
    }

    private void pickImageFromInternet() {

        Uri uri = Uri.parse("https://www.google.com/search?safe=active&biw=1246&bih=541&tbm=isch&sxsrf=ACYBGNTfJAop7LfaDGQ1xomMicNQswQIOA%3A1574256284909&sa=1&ei=nD7VXfiKN8X66QTigKeQDA&q=beautiful+places+to+live&oq=beautiful&gs_l=img.1.0.35i39j0i67j0l2j0i67l2j0l4.503857.507707..509686...1.0..2.85.1125.18......0....1..gws-wiz-img.....10..35i362i39.5z0co5-nbtg");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    private void getImageFromInternet() {

        if (getArguments() == null) {
            return;
        }
        if (getArguments().getParcelable("image") != null) {
            Uri receivedUri = getArguments().getParcelable("image");
            aimImage.setImageURI(receivedUri);
            Picasso.get().load(
                    receivedUri).into(target);
        }

    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(() -> {

                try {
                    @SuppressLint("SimpleDateFormat")
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = File.createTempFile(
                            imageFileName,
                            ".jpg",
                            storageDir

                    );
                    FileOutputStream ostream = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();

                    absolutePath = image.getAbsolutePath();
                    saveImagePathToDb(absolutePath);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (reqCode) {

                case CAMERA_REQUEST_CODE:
                    aimImage.setImageURI(Uri.parse(absolutePath));
                    saveImagePathToDb(absolutePath);
                    break;
                case GALLERY_REQUEST_CODE:
                    try {
                        Uri imageUri = data.getData();
                        assert imageUri != null;
                        final InputStream imageStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(imageUri);
                        selectedImageBitmap = BitmapFactory.decodeStream(imageStream);
                        aimImage.setImageBitmap(selectedImageBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                    saveToInternalStorage(selectedImageBitmap);
                    saveImagePathToDb(absolutePath);


                    break;
            }
        } else {
            Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void saveImagePathToDb(String path) {

        ImagePathDao imagePathDao = getDatabase(getContext()).imagePathDao();
        ImagePath imagePathToUpdate = imagePathDao.findLastImage();

        if (imagePathToUpdate != null) {
            if (!imagePathToUpdate.getImagePath().equals(path)) {
                imagePathToUpdate.setImagePath(path);
                imagePathDao.update(imagePathToUpdate);
            }
        } else {
            imagePathDao.insert(new ImagePath(0, path));
        }
    }

}