package com.phototell.common;

import android.support.annotation.NonNull;

/*
* interface for loading a single object
*/
public interface DataLoader<D> {

    void loadData(OnDataLoadedListener listener);

    interface OnDataLoadedListener<D> {
        void onSuccess(@NonNull D data);
        void onFailure(String errorMessage);//TODO improve
    }
}
