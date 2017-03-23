package com.phototell.model;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.phototell.data.Photo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by user on 21/03/2017.
 */

public class PhotosLoader {

    private DatabaseHelper helper;

    public PhotosLoader(Context context) {
        helper = new DatabaseHelper(context);
    }

    public void addPhoto(Photo photo) {

        try {
            Log.i(Photo.class.getName(), "Show list again");
            Dao<Photo, Integer> dao = helper.getPhotoDao();
            dao.create(photo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Photo> getPhotos() {
        List<Photo> list = null;

        try {
            Log.i(Photo.class.getName(), "Show list again");
            Dao<Photo, Integer> dao = helper.getPhotoDao();
            QueryBuilder<Photo, Integer> builder = dao.queryBuilder();
            builder.orderBy(Photo.DATE_FIELD_NAME, false);//.limit(30L);
            list = dao.query(builder.prepare());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
