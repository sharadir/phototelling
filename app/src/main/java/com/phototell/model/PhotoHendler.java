package com.phototell.model;

import android.content.Context;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.phototell.common.AsyncDataLoader;
import com.phototell.data.Photo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by user on 21/03/2017.
 */

class PhotoHendler extends AsyncDataLoader<Photo> {

    private final DatabaseHelper helper;

    public PhotoHendler(Context context) {
        helper = new DatabaseHelper(context);
    }

    public void addPhoto(Photo photo) {

        try {
            Log.i(Photo.class.getName(), "Add photo");
            Dao<Photo, Integer> dao = helper.getPhotoDao();
            dao.create(photo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePhoto(Photo photo) {

        try {
            Log.i(Photo.class.getName(), "Update photo");
            Dao<Photo, Integer> dao = helper.getPhotoDao();
            dao.update(photo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @WorkerThread
    @Override
    protected List<Photo> fetchDataInBackground(DataLoadTask task) {
        Log.i(Photo.class.getName(), "Show list");
        List<Photo> list = null;

        try {
            Log.i(Photo.class.getName(), "Show list again");
            Dao<Photo, Integer> dao = helper.getPhotoDao();
            QueryBuilder<Photo, Integer> builder = dao.queryBuilder();
            builder.orderBy(Photo.DATE_FIELD_NAME, false);//.limit(30L);
            list = dao.query(builder.prepare());

        } catch (SQLException e) {
            onFailure(e.toString());//TODO update: user friendly message (const)
        }
        return list;
    }
}
