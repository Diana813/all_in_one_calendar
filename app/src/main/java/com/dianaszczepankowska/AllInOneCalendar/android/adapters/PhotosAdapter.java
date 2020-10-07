package com.dianaszczepankowska.AllInOneCalendar.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.dianaszczepankowska.AllInOneCalendar.android.R;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePath;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePathDao;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase.getDatabase;
import static com.dianaszczepankowska.AllInOneCalendar.android.utils.DownloadImageUtils.displayImageFromDB;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private LayoutInflater layoutInflater;
    private List<ImagePath> photosList;

    public PhotosAdapter(Context context) {
        if (context != null) {
            this.layoutInflater = LayoutInflater.from(context);
            PhotosAdapter.context = context;
        }
    }

    public static Context getContext() {
        return context;
    }

    public void setPhotossList(List<ImagePath> photosList) {
        this.photosList = photosList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.photos_recycler_item, parent, false);
        return new PhotosViewHolder(itemView);
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull PhotosViewHolder holder, int position) {
        if (photosList == null) {
            return;
        }
        final ImagePath imagePath = photosList.get(position);
        displayImageFromDB(holder.aimPhoto, imagePath);

        //Delete photo
        holder.aimPhoto.setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), holder.aimPhoto);
            popupMenu.getMenuInflater().inflate(R.menu.delete_menu, popupMenu.getMenu());
            popupMenu.getMenu().findItem(R.id.action_delete_all_entries).setTitle(context.getString(R.string.deleteOne));
            popupMenu.setOnMenuItemClickListener(item -> {
                File file = new File(imagePath.getImagePath());
                boolean deleted = file.delete();
                if (!deleted) {
                    boolean deleted2 = false;
                    try {
                        deleted2 = file.getCanonicalFile().delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!deleted2) {
                        boolean deleted3 = Objects.requireNonNull(context).deleteFile(file.getName());
                    }
                }
                boolean exist = file.exists();
                System.out.println(exist);
                ImagePathDao imagePathDao = getDatabase(context).imagePathDao();
                imagePathDao.deleteByPath(imagePath.getImagePath());
                return true;
            });
            popupMenu.show();
            return true;
        });
    }


    @Override
    public int getItemCount() {
        if (photosList == null) {
            return 0;
        } else {
            return photosList.size();
        }
    }

    static class PhotosViewHolder extends RecyclerView.ViewHolder {
        private ImageView aimPhoto;

        PhotosViewHolder(View itemView) {
            super(itemView);
            aimPhoto = itemView.findViewById(R.id.aimImage);

        }
    }

}

