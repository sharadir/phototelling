package com.phototell.model;

import android.support.annotation.WorkerThread;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.phototell.PhotoTellApplication;
import com.phototell.common.AsyncListLoader;
import com.phototell.data.Photo;
import java.util.List;

/**
 *  data loader for photos.
 *  once photos are stored in the server side, fetchDataInBackground function is to be changed.
 */
class PhotosLoader<D> extends AsyncListLoader<Photo> {

    @Override
    @WorkerThread
    protected List<Photo> fetchDataInBackground(AsyncListLoader<Photo>.DataLoadTask task) {
        DatabaseHelper  helper = new DatabaseHelper(PhotoTellApplication.getContext());
        helper.close();
        Log.i(Photo.class.getName(), "Show list");
        List<Photo> list = null;
        try {
            Log.i(Photo.class.getName(), "Show list again");
            Dao<Photo, Integer> dao = helper.getPhotoDao();
            QueryBuilder<Photo, Integer> builder = dao.queryBuilder();
            builder.orderBy(Photo.DATE_FIELD_NAME, false);//.limit(30L);
            list = dao.query(builder.prepare());

        } catch (Exception e) {
            onFailure(e.toString());//TODO improve
        }
        return list;
    }
}