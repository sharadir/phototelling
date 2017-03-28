package com.phototell.common;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/*
base class for AsyncTasks data loaders.
D: data type.
*/
public abstract class AsyncLoader<D> implements DataLoader<D> {

    private  OnDataLoadedListener listener;

    protected abstract D fetchDataInBackground(DataLoadTask task);

    @Override
    public void loadData(@NonNull OnDataLoadedListener listener) {
        this.listener = listener;
        DataLoadTask task = new DataLoadTask();
        task.execute();
    }

    private void onDataLoaded(@Nullable D data) {
        if (listener != null && data != null) {
            listener.onSuccess(data);
        }
    }

    protected void onFailure(String errorMessage) {
        if (listener != null) {
            listener.onFailure(errorMessage);
        }
    }

    protected class DataLoadTask extends AsyncTask<Void, Void, D> {

        @Override
        protected D doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return fetchDataInBackground(this);
        }

        @Override
        protected void onPostExecute(@Nullable D result) {
            if (isCancelled()) {
                return;
            }
            onDataLoaded(result);
        }
    }
}