package com.phototell.ui.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.phototell.DetailScreen;
import com.phototell.PhotoTellApplication;
import com.phototell.data.Photo;
import com.phototell.ui.fragments.PhotoDetailsFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoListItemCard extends PhotoCard {
    public PhotoListItemCard(Context context) {
        super(context);
    }

    public PhotoListItemCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoListItemCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        super.init();
        setClickable(true);
        setFocusable(true);
        uploadDescription.setVisibility(GONE);
        imageView.setVisibility(GONE);
        thumbnailView.setVisibility(VISIBLE);
    }

    @Override
    public void bind(@NonNull final Photo photo) {
        final Context context = PhotoTellApplication.getContext();

        description.setText(photo.getDescription());
        Date creationDate = photo.getCreationDate();
        String newDateFormat = new SimpleDateFormat("dd-MM-yyyy").format(creationDate);
        if (newDateFormat != null) {
            createdAt.setText(newDateFormat);
        }
        thumbnailView.setImageBitmap(photo.getThumbnailBBitMap());
    }
}