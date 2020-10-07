package com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth;

import android.app.Application;

import com.dianaszczepankowska.AllInOneCalendar.android.database.CalendarDatabase;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePath;
import com.dianaszczepankowska.AllInOneCalendar.android.database.ImagePathDao;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PicturesViewModel extends AndroidViewModel {
    private ImagePathDao imagePathDao;
    private LiveData<List<ImagePath>> imagePathLiveData;

    public PicturesViewModel(@NonNull Application application) {
        super(application);
        imagePathDao = CalendarDatabase.getDatabase(application).imagePathDao();
        imagePathLiveData = imagePathDao.findAllImages();
    }

    public LiveData<List<ImagePath>> getPicturesList() {
        return imagePathLiveData;
    }

    public void insert(ImagePath imagePath) {
        imagePathDao.insert(imagePath);
    }

    public void update(ImagePath imagePath) {
        imagePathDao.update(imagePath);
    }

    public void deleteAll() {
        imagePathDao.deleteAll();
    }

}

