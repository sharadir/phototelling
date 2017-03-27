package com.phototell.common;

import android.support.annotation.NonNull;
import java.util.List;

/*
*
*/
public interface DataLoader<D> {

    void loadData(OnDataLoadedListener listener);

    interface OnDataLoadedListener<D> {
        void onSuccess(@NonNull List<D> data);
        void onFailure(String errorMessage);//TODO improve
    }
}
