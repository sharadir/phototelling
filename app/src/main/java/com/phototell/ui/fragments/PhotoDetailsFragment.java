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
import com.phototell.common.DataLoader;
import com.phototell.data.Photo;
import com.phototell.model.PhotoManager;
import com.phototell.ui.views.PhotoCard;
import com.phototell.util.Threads;

import java.lang.ref.WeakReference;

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
        showNoItems(false);
        showLoading(true);
        if (arguments != null) {
            Integer photoId = arguments.getInt(PHOTO_ID_KEY);
            PhotoManager.getInstance().loadPhoto(photoId, new PhotoListener(this));
        }
        return view;
    }

    public void setPhoto(@NonNull Photo photo) {
        if (photoCard != null) {
            photoCard.bind(photo);
            photoCard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected View inflateDataView(ViewStub dataWrapper) {
        dataWrapper.setLayoutResource(R.layout.photo_card_scroll);
        View view = dataWrapper.inflate();
        photoCard = (PhotoCard) view.findViewById(R.id.card_item);
        return photoCard;
    }

    @Override
    public String getNoItemsMessage() {
        return getResources().getString(R.string.no_pic_details);
    }

    private static class PhotoListener implements DataLoader.OnDataLoadedListener<Photo> {

        private WeakReference<PhotoDetailsFragment> fragmentWeakReference;

        public PhotoListener(PhotoDetailsFragment fragment) {
            this.fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onSuccess(@NonNull Photo photo) {
            PhotoDetailsFragment fragment = fragmentWeakReference.get();
            if (fragment == null || fragment.getActivity() == null) {
                return;
            }
            fragment.setPhoto(photo);
            fragment.showLoading(false);
            fragment.showNoItems(false);
        }

        @Override
        public void onFailure(final String errorMessage) {
            Threads.postOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PhotoDetailsFragment fragment = fragmentWeakReference.get();
                    if (fragment == null || fragment.getActivity() == null) {
                        return;
                    }
                    fragment.toast(errorMessage);
                    fragment.showNoItems(true);
                    fragment.showLoading(false);
                }
            });
        }
    }
}
