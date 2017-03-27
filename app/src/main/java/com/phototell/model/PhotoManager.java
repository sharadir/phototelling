package com.phototell.model;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Toast;

import com.phototell.PhotoTellApplication;
import com.phototell.common.DataLoader;
import com.phototell.data.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 21/03/2017.
 */

public class PhotoManager {

    private final List<Photo> photos;
    private boolean isLoaded;

    private static class InstanceHolder {
        private static PhotoManager INSTANCE;
    }

    public static PhotoManager getInstance() {
        if (InstanceHolder.INSTANCE == null) {
            InstanceHolder.INSTANCE = new PhotoManager();
        }
        return InstanceHolder.INSTANCE;
    }

    private static PhotoHendler handler;

    private PhotoManager() {
        photos = new ArrayList<>();
        handler = new PhotoHendler(PhotoTellApplication.getContext());
    }

    public void addPhoto(Photo photo) {
        try {
            handler.addPhoto(photo);
            photos.add(photo);
        } catch (RuntimeException e) {
	        //TODO improve design to have onFailur
	        int duration = Toast.LENGTH_SHORT;
	        Toast toast = Toast.makeText(PhotoTellApplication.getContext().getApplicationContext(), "there was an error saving the image" + e.toString(), duration);
	        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
	        toast.show();

        }
    }

    public void updatePhoto(Photo photo) {
        try {
            handler.updatePhoto(photo);
        } catch (RuntimeException e) {
            //TODO improve design to have onFailure
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

    public List<Photo> cachedList() {
        return photos;
    }

    public void loadData(@NonNull final DataLoader.OnDataLoadedListener<Photo> listener) {
        if (isLoaded) {
            listener.onSuccess(photos);
        } else {
            handler.loadData(new DataLoader.OnDataLoadedListener<Photo>() {

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
        }
    }

    public List<Photo> filterPhotos(String textIndescription) {
        if(textIndescription == null || textIndescription.equals("")){
            //x for cancel search was clicked
           return photos;
        }List<Photo> filteredPhotos = new ArrayList<>();
        for (Photo photo : photos) {
            if (textIndescription != null && photo.getDescription() != null && photo.getDescription().contains(textIndescription)) {
                filteredPhotos.add(photo);
            }
        }
        return filteredPhotos;
    }
}