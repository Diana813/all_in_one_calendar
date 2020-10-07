package com.dianaszczepankowska.AllInOneCalendar.android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePath;
import com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.LifeAims;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.LifeAims.absolutePath;
import static com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.LifeAims.context;

public class DownloadImageUtils {


    public static void checkCameraHardware(Context context, View addPhoto) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            addPhoto.setVisibility(View.VISIBLE);
        } else {
            addPhoto.setVisibility(View.GONE);
        }
    }


    public static void displayImageFromDB(ImageView view, ImagePath imagePath) {
        if (imagePath != null) {
            String aimImagePath = imagePath.getImagePath();
            if (aimImagePath.contains("imageDir")) {
                loadImageFromStorage(aimImagePath, view);
            } else if (aimImagePath.contains("JPEG_")) {
                view.setImageURI(Uri.parse(aimImagePath));
            }
        }
    }


    private static void loadImageFromStorage(String path, ImageView view) {

        try {
            File file = new File(path, "aimImage.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            view.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void saveToInternalStorage(Bitmap bitmapImage, Context context) {

        ContextWrapper cw = new ContextWrapper(context);
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


    public static File createImageFile(Context context) throws IOException {

        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        absolutePath = image.getAbsolutePath();
        return image;
    }


    public static Target target = new Target() {
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(() -> {

                try {
                    @SuppressLint("SimpleDateFormat")
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = File.createTempFile(
                            imageFileName,
                            ".jpg",
                            storageDir

                    );
                    FileOutputStream ostream = new FileOutputStream(image);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();

                    absolutePath = image.getAbsolutePath();
                    DatabaseUtils.saveImagePathToDb(absolutePath, context);

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


    public static void getImageFromInternet(Uri uriFromInternet) {
        LifeAims.uri = uriFromInternet;
        Picasso.get().load(
                LifeAims.uri).into(target);
    }

}
