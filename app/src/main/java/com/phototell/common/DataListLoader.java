package com.phototell.common;

import android.support.annotation.NonNull;
import java.util.List;

/*
* interface for loading a list of objects
*/
public interface DataListLoader<D> {

    void loadData(OnDataLoadedListener listener);

    interface OnDataLoadedListener<D> {
        void onSuccess(@NonNull List<D> data);
        void onFailure(String errorMessage);//TODO improve
    }
}
