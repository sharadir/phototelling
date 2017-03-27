package com.phototell.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.phototell.R;
import com.phototell.data.Photo;
import com.phototell.model.PhotoManager;
import com.phototell.ui.views.PhotoCard;

import static android.view.View.GONE;

/**
 *
 *
 */

public class PhotoDetailsFragment extends BaseDataFragment {
    public static final String PHOTO_ID_KEY = "photo_id";
    public static final String TAG = "PhotoDetailsFragment";

    private PhotoCard photoCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Bundle arguments = getArguments();
        photoCard.setVisibility(GONE);
        showNoItems(true);
        if (arguments != null) {
            Integer photoId = arguments.getInt(PHOTO_ID_KEY);
            if (photoId != null) {
                Photo photo = PhotoManager.getInstance().getPhoto(photoId);
                if (photo != null) {
                    photoCard.bind(photo);
                    photoCard.setVisibility(View.VISIBLE);
                    showNoItems(false);
                }
            }
        }
        return view;
    }

    public void setPhoto(@NonNull Photo photo){
       if(photoCard != null){
           photoCard.bind(photo);
       }
    }

    @Override
    protected View inflateDataView(ViewStub dataWrapper) {
        dataWrapper.setLayoutResource(R.layout.photo_card);
        photoCard = (PhotoCard) dataWrapper.inflate();
        return photoCard;
    }

    @Override
    public String getNoItemsMessage() {
        return getResources().getString(R.string.no_pic_details);
    }
}
