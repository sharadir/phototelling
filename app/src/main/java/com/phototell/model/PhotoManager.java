package com.phototell.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.phototell.PhotoTellApplication;
import com.phototell.common.DataListLoader;
import com.phototell.common.DataLoader;
import com.phototell.data.Photo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by user on 21/03/2017.
 */

public class PhotoManager {

    private final List<Photo> photos;
    private boolean isLoaded;

    private final DatabaseHelper helper;

    private static class InstanceHolder {
        private static PhotoManager INSTANCE;
    }

    public static PhotoManager getInstance() {
        if (InstanceHolder.INSTANCE == null) {
            InstanceHolder.INSTANCE = new PhotoManager();
        }
        return InstanceHolder.INSTANCE;
    }

    private PhotoManager() {
        photos = new ArrayList<>();
        helper = new DatabaseHelper(PhotoTellApplication.getContext());
    }

    public void addPhoto(Photo photo) {
        try {
            Log.i(Photo.class.getName(), "Add photo");
            Dao<Photo, Integer> dao = helper.getPhotoDao();
            dao.create(photo);
            photos.add(photo);
        } catch (SQLException e) {
            //TODO improve design to have onFailure
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(PhotoTellApplication.getContext().getApplicationContext(), "there was an error saving the image" + e.toString(), duration);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        }
    }

    public void updatePhoto(Photo photo) {
        try {
            Log.i(Photo.class.getName(), "Update photo");
            Dao<Photo, Integer> dao = helper.getPhotoDao();
            dao.update(photo);
        } catch (Exception e) {
            //TODO improve design to have onFailure
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(PhotoTellApplication.getContext().getApplicationContext(), "there was an error saving the image" + e.toString(), duration);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        }
    }


    public List<Photo> cachedList() {
        return photos;
    }

    public void loadPhotos(@NonNull final DataListLoader.OnDataLoadedListener<Photo> listener) {
        if (isLoaded) {
            listener.onSuccess(photos);
        } else {
            try {
                PhotosLoader loader = new PhotosLoader();
                loader.loadData(new DataListLoader.OnDataLoadedListener<Photo>() {

                    @Override
                    public void onSuccess(@NonNull List<Photo> data) {
                        photos.clear();
                        photos.addAll(data);
                        isLoaded = true;
                        listener.onSuccess(data);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        listener.onFailure(errorMessage);
                    }
                });
            } catch (Exception e) {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(PhotoTellApplication.getContext().getApplicationContext(), "there was an error saving the image" + e.toString(), duration);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
                toast.show();
            }
        }
    }

    public void loadPhoto(int photoId, @NonNull final DataLoader.OnDataLoadedListener<Photo> listener) {
        try {
           final Photo photo = getPhoto(photoId);
            Bitmap bitmap = photo.getImageBitMap();
            if (bitmap == null) {// TODO flag
                //fetch in the background
                PhotoLoader photoLoader = new PhotoLoader(photoId);

                photoLoader.loadData(new DataLoader.OnDataLoadedListener<Photo>() {
                    @Override
                    public void onSuccess(@NonNull Photo data) {
                        ///TODO update list?
                        photo.setImageBitMap(data.getImageBitMap());
                        listener.onSuccess(photo);
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                        listener.onFailure(errorMessage);
                    }
                });
            }
            else{
                listener.onSuccess(photo);
            }
        } catch (Exception e) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(PhotoTellApplication.getContext().getApplicationContext(), "there was an error saving the image" + e.toString(), duration);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.show();
        }
    }

    public Photo getPhoto(int photoId) {
        Photo res = null;
        for (Photo photo : photos) {
            if (photo.getId() == photoId) {
                res = photo;
                break;
            }
        }
        return res;
    }


    public List<Photo> filterPhotos(String text) {
        if (text == null || text.equals("")) {
            //x for cancel search was clicked
            return photos;
        }
        List<Photo> filteredPhotos = new ArrayList<>();
        for (Photo photo : photos) {
            if (photo.getDescription() != null && photo.getDescription().contains(text)) {
                filteredPhotos.add(photo);
            }
        }
        return filteredPhotos;
    }
}