package com.phototell.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phototell.R;
import com.phototell.common.DataLoader;
import com.phototell.data.Photo;
import com.phototell.model.PhotoManager;
import com.phototell.ui.views.PhotoListItemCard;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */

public class PhotoListFragment extends BaseDataListFragment {
    public static final String TAG = "PhotoListFragment";

    private OnPhotoSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnPhotoSelectedListener {
        public void onPhotoSelected(Photo photo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPhotoSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        showNoItems(false);
        showLoading(true);
        PhotoManager.getInstance().loadData(new PhotoListener(this));
        return view;
    }

    @Override
    public void onResume() {

        adapter.setItems(PhotoManager.getInstance().cachedList());

        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public String getNoItemsMessage() {
        return getResources().getString(R.string.no_pics);
    }

    @Override
    protected Map<Class, Class> getDataViewClasses() {
        HashMap hashMap = new HashMap<Class, Class>();
        hashMap.put(Photo.class, PhotoListItemCard.class);
        return hashMap;
    }

    @Override
    public void onItemClick(int position, View v) {
        Object item = adapter.getItem(position);
        if (item instanceof Photo) {
            mCallback.onPhotoSelected((Photo) item);
        }
    }

    @Override
    public void onItemLongClick(int position, View v) {

    }

    public void setItems(@Nullable List<Photo> photos) {
        adapter.setItems(photos == null || photos.size() == 0 ? Collections.<Photo>emptyList() : photos);
    }

    private static class PhotoListener implements DataLoader.OnDataLoadedListener<Photo> {

        private WeakReference<PhotoListFragment> fragmentWeakReference;

        public PhotoListener(PhotoListFragment fragment) {
            this.fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onSuccess(@NonNull List<Photo> data) {
            PhotoListFragment fragment = fragmentWeakReference.get();
            if (fragment == null || fragment.getActivity() == null) {
                return;
            }
            fragment.adapter.setItems(data);
            fragment.showLoading(false);
            fragment.showNoItems(data.isEmpty());
        }

        @Override
        public void onFailure(String errorMessage) {
            PhotoListFragment fragment = fragmentWeakReference.get();
            if (fragment == null || fragment.getActivity() == null) {
                return;
            }
            fragment.toast(errorMessage);
        }
    }
}