package com.phototell.model;

import android.support.annotation.WorkerThread;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.phototell.PhotoTellApplication;
import com.phototell.common.AsyncLoader;
import com.phototell.data.Photo;
import com.phototell.util.PhotoUtility;


/**
 *  data loader for photo.
 *  once photos are stored in the server side, fetchDataInBackground function is to be changed.
 */
class PhotoLoader<D> extends AsyncLoader<Photo> {

    private int photoId;
    PhotoLoader(int photoId) {
        this.photoId = photoId;
    }

    @Override
    @WorkerThread
    protected Photo fetchDataInBackground(DataLoadTask task) {
        DatabaseHelper helper = new DatabaseHelper(PhotoTellApplication.getContext());
        Log.i(Photo.class.getName(), "Fetch Photo");
        try {
            Dao<Photo, Integer> dao = helper.getPhotoDao();
            Photo photo = dao.queryForId(photoId);
            photo.setImageBitMap(PhotoUtility.getImage(photo.getPath()));
            return photo;
        } catch (Exception e) {
            onFailure(e.toString());//TODO improve
            return null;
        }
        finally {
	        helper.close();
        }
    }
}