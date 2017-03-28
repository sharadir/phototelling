package com.phototell.common;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/*
base class for AsyncTasks data list loaders.
D: data type.
*/
public abstract class AsyncListLoader<D> implements DataListLoader<D> {

    private OnDataLoadedListener listener;

    protected abstract List<D> fetchDataInBackground(DataLoadTask task);

    @Override
    public void loadData(@NonNull OnDataLoadedListener listener) {
        this.listener = listener;
        DataLoadTask task = new DataLoadTask();
        task.execute();
    }

    private void onDataLoaded(@Nullable List<D> data) {
        if (listener != null && data != null) {
            listener.onSuccess(data);
        }
    }

    protected void onFailure(String errorMessage) {
        if (listener != null) {
            listener.onFailure(errorMessage);
        }
    }

    protected class DataLoadTask extends AsyncTask<Void, Void, List<D>> {

        @Override
        protected List<D> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return fetchDataInBackground(this);
        }

        @Override
        protected void onPostExecute(@Nullable List<D> result) {
            if (isCancelled()) {
                return;
            }
            result = result == null ? Collections.<D>emptyList() : result;
            onDataLoaded(result);
        }
    }
}
